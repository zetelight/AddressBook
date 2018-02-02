package com.example.rustybucket.addressbook422;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class splashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(splashScreen.this, MainActivity.class);
            splashScreen.this.startActivity(mainIntent);
            splashScreen.this.finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
