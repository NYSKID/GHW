package com.mediatek.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class GameplayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Keep the screen on while gameplay is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_gameplay);
    }
}
