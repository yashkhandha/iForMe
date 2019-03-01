package edu.monash.iforme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.TaskExecutors;

/**
 * To manage Main Screen on start of the app
 */
public class SplashActivity extends AppCompatActivity {

    //UI references
    TextView tv;
    ImageView iv;

    /**
     * to load the layout initially
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView)findViewById(R.id.iv);

        //Githib library used for animation on image or logo
        YoYo.with(Techniques.ZoomIn).playOn(iv);

        //Start the Login activity after showing it for 3s
        final Intent i = new Intent(this,LoginActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //strat activity login and finish the current splash activity
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
