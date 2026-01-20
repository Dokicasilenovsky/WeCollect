package com.example.wecollectfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Buttons extends AppCompatActivity {

    ViewPager SlideViewPager;
    LinearLayout DotLayout;
    Button btnBack, btnNext, btnSkip;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        btnBack = findViewById(R.id.backbtn);
        btnNext = findViewById(R.id.nextbtn);
        btnSkip = findViewById(R.id.skipButton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) > 0){
                    SlideViewPager.setCurrentItem(getitem(-1), true);

                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) < 2)
                    SlideViewPager.setCurrentItem(getitem(1),true);
                else {

                    Intent i = new Intent(Buttons.this,ItemView.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Buttons.this,ItemView.class);
                startActivity(i);
                finish();

            }
        });

        SlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        DotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        SlideViewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        SlideViewPager.addOnPageChangeListener(viewListener);

    }

    public void setUpindicator(int position){

        dots = new TextView[3];
        DotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.hotpink,getApplicationContext().getTheme()));
            DotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.hotpink,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                btnBack.setVisibility(View.VISIBLE);

            }else {

                btnBack.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return SlideViewPager.getCurrentItem() + i;
    }
}
