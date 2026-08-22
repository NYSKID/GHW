package com.mediatek.game;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.view.Surface;
import android.view.Display;
import java.lang.reflect.Method;
import android.graphics.Color;
import android.view.View;
import android.util.Log;

public final class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String ACTIVE_MSG = "Active while a supported game is running";
    private static final String INACTIVE_MSG = "Inactive — will be active during gameplay if enabled";

    // Keep references to views instead of digging into decor view children later.
    private Switch gameModeSwitch;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        // Dark window background and status bar
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                getWindow().setStatusBarColor(Color.BLACK);
            } catch (Exception e) {
                Log.w(TAG, "Unable to set status bar color", e);
            }
        }

        // Layout container
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // Note: padding is in pixels here; consider converting dp if needed.
        layout.setPadding(24, 24, 24, 24);
        layout.setBackgroundColor(Color.BLACK);

        TextView title = new TextView(this);
        title.setText("Game Helio G100\n\nAndroid 16 / API 36\n120 FPS frame-rate");
        title.setTextColor(Color.WHITE);
        title.setTextSize(18f);
        layout.addView(title);

        // Switch to enable/disable gameplay-only 120Hz mode
        gameModeSwitch = new Switch(this);
        gameModeSwitch.setText("Enable 120Hz Game Mode (gameplay only)");
        gameModeSwitch.setTextColor(Color.WHITE);

        // Status label to explain transient activation
        statusView = new TextView(this);
        statusView.setTextColor(Color.LTGRAY);
        statusView.setTextSize(14f);

        // Initialize switch state from persisted preference
        boolean persisted = PreferencesHelper.isHighRefreshEnabled(this);
        gameModeSwitch.setChecked(persisted);

        // Initialize status from transient active flag
        boolean active = PreferencesHelper.isGameModeActive(this);
        statusView.setText(active ? ACTIVE_MSG : INACTIVE_MSG);

        gameModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Persist the user's choice
                PreferencesHelper.setHighRefreshEnabled(MainActivity.this, isChecked);

                // Apply or remove transient game-mode behavior
                if (isChecked) {
                    GameModeController.enableGameMode(MainActivity.this);
                } else {
                    GameModeController.disableGameMode(MainActivity.this);
                }

                // Update status label after action (read the transient active flag)
                boolean nowActive = PreferencesHelper.isGameModeActive(MainActivity.this);
                statusView.setText(nowActive ? ACTIVE_MSG : INACTIVE_MSG);
            }
        });

        layout.addView(gameModeSwitch);
        layout.addView(statusView);

        // Keep previous fallback: try set surface frame rate for API 33+
        // This fallback is best-effort and uses reflection; failures are non-fatal.
        if (Build.VERSION.SDK_INT >= 33) {
            try {
                Display display = getWindow().getDecorView().getDisplay();
                if (display != null) {
                    // Some platforms expose a getSurface() method via Display; use reflection and guard thoroughly.
                    Method getSurfaceMethod = null;
                    try {
                        getSurfaceMethod = Display.class.getMethod("getSurface");
                    } catch (NoSuchMethodException ignored) {
                        // Method missing on some devices — ignore fallback.
                    }
                    if (getSurfaceMethod != null) {
                        Object maybeSurface = getSurfaceMethod.invoke(display);
                        if (maybeSurface instanceof Surface) {
                            Surface surface = (Surface) maybeSurface;
                            // setFrameRate exists on Surface on API 33+, call it safely
                            try {
                                surface.setFrameRate(120f, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT);
                            } catch (Exception e) {
                                Log.w(TAG, "Surface.setFrameRate failed", e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Surface.setFrameRate fallback failed", e);
            }
        }

        // Keep layout stable and draw behind system bars where supported.
        // Note: SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN is deprecated on newer SDKs, but kept for compatibility.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update status label on resume in case transient active state changed.
        // Use the stored reference rather than assuming decor view children order.
        try {
            boolean active = PreferencesHelper.isGameModeActive(this);
            if (statusView != null) {
                statusView.setText(active ? ACTIVE_MSG : INACTIVE_MSG);
            }
        } catch (Exception ignored) {
            // If something goes wrong, don't crash; initial text was set in onCreate.
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Perform transient cleanup (do not change user preference)
        GameModeController.onAppBackgrounded(this.getApplicationContext());
    }
}
