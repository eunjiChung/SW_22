package hello22.hellochat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by twih on 2016. 11. 26..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
