package edu.monash.iforme;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView pac;
    private ImageView purple;
    private ImageView golden;
    private ImageView spokes;
    private TextView bonusTextView;

    // Speed
    private int pacSpeed;
    private int purpleSpeed;
    private int goldenSpeed;
    private int spokesSpeed;

    // Position
    private int pacY;
    private int purpleX;
    private int purpleY;
    private int goldenX;
    private int goldenY;
    private int spokesX;
    private int spokesY;

    // Score
    private int score = 0;

    // Size
    private int frameHeight;
    private int pacSize;
    private int screenWidth;
    private int screenHeight;


    // to check the flags status
    private boolean action_flg = false;
    private boolean start_flg = false;


    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Pac Man");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        pac = (ImageView) findViewById(R.id.pac);
        purple = (ImageView) findViewById(R.id.purple);
        golden = (ImageView) findViewById(R.id.golden);
        spokes = (ImageView) findViewById(R.id.spokes);
        bonusTextView = (TextView)findViewById(R.id.bonusText);
        bonusTextView.setVisibility(View.GONE);

        // Get screen size.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        pacSpeed = Math.round(screenHeight / 60);
        purpleSpeed = Math.round(screenWidth / 60);
        goldenSpeed = Math.round(screenWidth / 60);
        spokesSpeed = Math.round(screenWidth / 45);



        // Move to out of screen.
        purple.setX(-80);
        purple.setY(-80);
        golden.setX(-80);
        golden.setY(-80);
        spokes.setX(-80);
        spokes.setY(-80);

        pacY = 500;

        scoreLabel.setText("Score : 0");
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            pacY = (int)pac.getY();

            // The pac is a square.(height and width are the same.)
            pacSize = pac.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 30);


        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    public void changePos() {

        hitCheck();

        // purple
        purpleX -= purpleSpeed;
        if (purpleX < 0) {
            purpleX = screenWidth + 20;
            purpleY = (int) Math.floor(Math.random() * (frameHeight - purple.getHeight()));
        }
        purple.setX(purpleX);
        purple.setY(purpleY);


        // spokes
        spokesX -= spokesSpeed;
        if (spokesX < 0) {
            spokesX = screenWidth + 10;
            spokesY = (int) Math.floor(Math.random() * (frameHeight - spokes.getHeight()));
        }
        spokes.setX(spokesX);
        spokes.setY(spokesY);


        // golden
        goldenX -= goldenSpeed;
        if (goldenX < 0) {
            goldenX = screenWidth + 1000;
            goldenY = (int) Math.floor(Math.random() * (frameHeight - golden.getHeight()));
        }
        golden.setX(goldenX);
        golden.setY(goldenY);


        // Move pac
        if (action_flg == true) {
            // Touching
            pacY -= pacSpeed;

        } else {
            // Releasing
            pacY += pacSpeed;
        }

        // Check pac position.
        if (pacY < 0) pacY = 0;

        if (pacY > frameHeight - pacSize)
                pacY = frameHeight - pacSize;

        pac.setY(pacY);

        scoreLabel.setText("Score : " + score);

    }

    public void hitCheck() {

        // If the center of the ball is in the pac, it counts as a hit.

        // purple
        int purpleCenterX = purpleX + purple.getWidth() / 2;
        int purpleCenterY = purpleY + purple.getHeight() / 2;

        // 0 <= purpleCenterX <= pacWidth
        // pacY <= purpleCenterY <= pacY + pacHeight

        if (0 <= purpleCenterX && purpleCenterX <= pacSize &&
                pacY <= purpleCenterY && purpleCenterY <= pacY + pacSize) {

            score += 20;
            purpleX = -10;

        }

        // golden
        int goldenCenterX = goldenX + golden.getWidth() / 2;
        int goldenCenterY = goldenY + golden.getHeight() / 2;

        if (0 <= goldenCenterX && goldenCenterX <= pacSize &&
                pacY <= goldenCenterY && goldenCenterY <= pacY + pacSize) {
            bonusTextView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeOutUp).playOn(bonusTextView);
            score += 50;
            goldenX = -10;

        }

        // spokes
        int spokesCenterX = spokesX + spokes.getWidth() / 2;
        int spokesCenterY = spokesY + spokes.getHeight() / 2;

        if (0 <= spokesCenterX && spokesCenterX <= pacSize &&
                pacY <= spokesCenterY && spokesCenterY <= pacY + pacSize) {

            // Stop Timer!!
            timer.cancel();
            timer = null;


            // Show Result
            finish();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            FirebaseDatabase.getInstance().getReference("GameScore")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(timeStamp)
                    .setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Toast.makeText(getApplicationContext(),"Score added successfully",
                          //      Toast.LENGTH_LONG).show();
                    }
                }
            });

            Intent intent = new Intent(getApplicationContext(), GameResult.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }

    }
}
