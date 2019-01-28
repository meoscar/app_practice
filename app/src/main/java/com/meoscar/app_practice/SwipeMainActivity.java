package com.meoscar.app_practice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SwipeMainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeview;
    private int refreshCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_test);

        swipeview = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout3);
        swipeview.setOnRefreshListener(this);
        swipeview.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE, Color.RED, Color.CYAN);
        swipeview.setDistanceToTriggerSync(500);//how long the finger has to swipe on the screen to trigger the refreshment
        swipeview.setSize(SwipeRefreshLayout.DEFAULT);

    }

    @Override
    public void onRefresh() {
        swipeview.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView tv = findViewById(R.id.textView2);
                refreshCount++;
                tv.setText("Refresh count = " + refreshCount);
                swipeview.setRefreshing(false);//dismiss the refresh circle
            }
        }, 5000);
    }
}
