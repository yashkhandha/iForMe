package edu.monash.iforme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * GameResult class to show the user final result of the game played
 */
public class GameResult extends AppCompatActivity {

    Button goBack;

    /**
     * onCreate method to load layout and initial references
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        //Textviews to show score to user
        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        //Get the score from Intent from GameActivity
        int score = getIntent().getIntExtra("SCORE", 0);
        //display score
        scoreLabel.setText(score + "");

        //on back button click finish the activity
        goBack = (Button)findViewById(R.id.goBackButton);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Store the latest score in the Sharedpreferences to manage history
        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);

            // Update High Score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();

        } else {
            highScoreLabel.setText("High Score : " + highScore);

        }
    }

    //Onn click of try again navigate back to the GameActivity.class
    public void tryAgain(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }



}
