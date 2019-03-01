package edu.monash.iforme;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private int[] layouts = {R.layout.slide_first,R.layout.slide_second,R.layout.slide_third,R.layout.slide_fourth};
    private ViewPagerAdapter adapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button mNextButton;
    private Button mSkipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new PreferenceManager(this).checkPreference()){
            loadMainActivity();
        }


        if(Build.VERSION.SDK_INT >19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        mViewPager = findViewById(R.id.viewPager);
        mNextButton = (Button)findViewById(R.id.nextButton);
        mSkipButton = (Button)findViewById(R.id.skipButton);

        mNextButton.setOnClickListener(this);
        mSkipButton.setOnClickListener(this);

        adapter = new ViewPagerAdapter(layouts,this);
        mViewPager.setAdapter(adapter);

        Dots_Layout = findViewById(R.id.dots);

        createDots(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                createDots(position);
                if(position == layouts.length-1){
                    mNextButton.setText("START");
                    mSkipButton.setVisibility(View.INVISIBLE);
                }
                else{
                    mNextButton.setText("NEXT");
                    mSkipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void createDots(int currentPosition){
        if(Dots_Layout != null){
            Dots_Layout.removeAllViews();
        }

        dots = new ImageView[layouts.length];

        for(int i =0; i<layouts.length; i++){
            dots[i] = new ImageView(this);
            if (i == currentPosition){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dots_active));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dots_inactive));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.nextButton:
                nextSlide();
                break;

            case R.id.skipButton:
                loadMainActivity();
                new PreferenceManager(this).writePreference();
                break;
        }
    }

    private void loadMainActivity(){
        startActivity(new Intent(this,MainActivity.class));
    }

    private void nextSlide(){
        int nextSlide = mViewPager.getCurrentItem()+1;

        if(nextSlide < layouts.length){
            mViewPager.setCurrentItem(nextSlide);
        }
        else{
            loadMainActivity();
            new PreferenceManager(this).writePreference();
        }
    }


}
