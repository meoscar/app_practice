package com.meoscar.app_practice;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "app_practice->Main";
    private static boolean leaveActivity = false;
    private static int count = 0;

    private static Button mButton1, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8;

    private static MyUIHandler handler;
    private static Looper uiLooper;
    private static Context mContext;

    private static class MyUIHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyUIHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // check if this weak reference obj has been GCed or not?
            MainActivity activity = mActivity.get();

            if(msg.what == 0x5566) {
                //Log.d(TAG, "handleMessage : " + msg.what);
                activity.refreshTest();
            }

            TextView ischanging = (TextView) activity.findViewById(R.id.textView3);

            if (msg.what == 0x1234) {
                ischanging.setText("is charging");
                ischanging.setBackgroundColor(Color.GREEN);
            }

            if (msg.what == 0x1236) {
                ischanging.setText("is not charging");
                ischanging.setBackgroundColor(Color.RED);

            }
            if(msg.what == 0x7788) {
                Log.d(TAG, "useHandlerUpdate : got it");
                TextView useHandlerUpdate = (TextView) activity.findViewById(R.id.useHandlerUpdate);
                useHandlerUpdate.setText("useHandlerUpdate : got it");
            }
        }
    };

    private void refreshTest() {
        TextView tv = (TextView) findViewById(R.id.handler_practice);
        tv.setText("Count : " + count);
    }

    private Intent getBatteryIntent() {
        Intent batteryIntent;
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryIntent = this.registerReceiver(null, intentfilter);
        return batteryIntent;
    }

    public boolean isCharging() {
        boolean charging = false;
        Intent batteryIntent = getBatteryIntent();
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        charging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);
        //Log.d(TAG, "isCharging : " + charging);
        return charging;
    }

    public static String Decimal2Hek(int d) {
        String digits = "0123456789ABCDEF";
        if (d == 0) return "0";
        String hex = "";
        while (d > 0) {
            int digit = d % 16;
            hex = digits.charAt(digit) + hex;
            d = d/16;
        }
        return hex;
    }

    /*
    // android.os.Looper.java

    public static void loop() {
        final Looper me = myLooper();
        final MessageQueue queue = me.mQueue;
        ...
        for (;;) {
            Message msg = queue.next(); // might block
            ...
            // This must be in a local variable, in case a UI event sets the logger
            final Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }
            ...
            msg.target.dispatchMessage(msg);
            ...
            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }
            ...
        }
    }

    public void setMessageLogging(@Nullable Printer printer) {
        mLogging = printer;
    }
    */

    //start +++
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        handler = new MyUIHandler(this);
        uiLooper = handler.getLooper();
        //we can pass the Printer object into the method to crreate customize log.
        uiLooper.setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                Log.d(TAG, "setMessageLogging (input) = " + x);

                final String START = ">>>>> Dispatching";
                final String END = "<<<<< Finished";

                String lastS = "default_msg.type";
                int lastI = 7533967;

                if (x.contains(START)) {
                    lastS = x.substring(x.lastIndexOf(" ") + 1 , x.length()); //lastS is the decimal number of the msg.what(Hex)
                    lastI = Integer.valueOf(lastS);
                    switch (Decimal2Hek(lastI)) {
                        //case "1234"://4660
                        case "5566"://21862
                            Log.d(TAG, "setMessageLogging :  Dispatching(input) = " + "count++");
                            break;
                        case "7788"://30600
                            Log.d(TAG, "setMessageLogging :  Dispatching(input) = " + "useHandlerUpdateUI");
                            break;
                    }
                } else if (x.contains(END)){
                    //Log.d(TAG, "setMessageLogging : Finished = " + "count++");
                }
            }
        });

        mButton1 = (Button) findViewById(R.id.goToListExample1);
        mButton2 = (Button) findViewById(R.id.goToListExample2);
        mButton3 = (Button) findViewById(R.id.uiUpdateTest);
        mButton4 = (Button) findViewById(R.id.uiUpdateTestReset);
        mButton5 = (Button) findViewById(R.id.gotoTabDemo);
        mButton6 = (Button) findViewById(R.id.gotoSwipeDemo);
        mButton7 = (Button) findViewById(R.id.startforegroundService);
        mButton8 = (Button) findViewById(R.id.stopforegroundService);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ListFruitActivity.class);
                startActivity(intent);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ListMobileActivity.class);
                startActivity(intent);
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "invoke useHandlerUpdateUI...");
                useHandlerUpdateUI();
                Log.d(TAG, "invoke useUIThreadUpdateUI...");
                useUIThreadUpdateUI();
            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiUpdateTestReset();
            }
        });

        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TabMainActivity.class);
                startActivity(intent);
            }
        });
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SwipeMainActivity.class);
                startActivity(intent);
            }
        });

        mButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestService.class);
                intent.setAction("meoscar.is.cool");
                mContext.startService(intent);
            }
        });
        mButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestService.class);
                mContext.stopService(intent);
            }
        });
    }

    private void useHandlerUpdateUI(){
        handler.sendEmptyMessage(0x7788);
    }

    /**

    Note: what's difference between post and sendMessage?? No difference to me

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

    public final boolean post(Runnable r) {
       return  sendMessageDelayed(getPostMessage(r), 0);
    }

    private static Message getPostMessage(Runnable r) {
            Message m = Message.obtain();
            m.callback = r;
            return m;
    }
    */
    private void useUIThreadUpdateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "useUIThreadUpdate : got it");
                TextView useUIThreadUpdate = (TextView) findViewById(R.id.useUIThreadUpdate);
                useUIThreadUpdate.setText("useUIThreadUpdate : got it");
            }
        });
    }

    private void uiUpdateTestReset(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "uiUpdateTestReset");
                TextView useUIThreadUpdate = (TextView) findViewById(R.id.useUIThreadUpdate);
                useUIThreadUpdate.setText("useUIThreadUpdate :");
                TextView useHandlerUpdate = (TextView) findViewById(R.id.useHandlerUpdate);
                useHandlerUpdate.setText("useHandlerUpdate :");
            }
        });
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        leaveActivity = false;
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "leaveActivity = " + leaveActivity);
                while (!leaveActivity) {
                    //deal with :
                    count++;
                    // Now we start to use runOnUiThread to refresh screen
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //keep this for setMessagelogger test
                            handler.sendEmptyMessage(0x5566);

                            //Log.d(TAG, "runOnUiThread");
                            try {
                                TextView ischanging = (TextView) findViewById(R.id.textView3);
                                if(isCharging()){
                                    ischanging.setText("is charging");
                                    ischanging.setBackgroundColor(Color.GREEN);

                                } else {
                                    ischanging.setText("is not charging");
                                    ischanging.setBackgroundColor(Color.RED);
                                }

                            } catch (Exception e ) {}
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        leaveActivity = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        leaveActivity = false;
    }
    //start ---

    //stop +++
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        //leaveActivity =true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        leaveActivity = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory");
        leaveActivity = true;
    }
    //stop ---
}
