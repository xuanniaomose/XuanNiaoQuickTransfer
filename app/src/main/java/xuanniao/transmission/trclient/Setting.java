package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.io.*;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.PendingIntent.getActivity;

public class Setting extends AppCompatActivity {
    private static Socket socket;
    private ExecutorService mExecutorService = null;
    private PrintWriter printWriter=null;
    private BufferedReader in = null;
    private static final String TAG = "TAG";
    private String receiveMsg;
    private TextView mTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("设置", "已启动");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setCustomActionBar();

        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        TextView mTextView = (TextView) findViewById(R.id.textview_port_num);
        ExecutorService mExecutorService = Executors.newCachedThreadPool();

        // 连接开关
        TextView textview_state0 = (TextView) findViewById(R.id.textview_state0);
        TextView textview_state1 = (TextView) findViewById(R.id.textview_state1);
        Switch switch_connect = (Switch) findViewById(R.id.switch_connect);
        switch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Connect Connect = new Connect();

                if (isChecked) {
                    Connect.connect();
                    Log.i("连接", "已开启连接开关");
                    textview_state1.setText("开启");
                } else {
                    Log.i("socket.S", String.valueOf(socket));
                    Connect.disconnect(switch_connect);
                    Log.i("断开", "已关闭开关");
                    textview_state1.setText("断开");
                }
            }
        });


        class checkHandler extends Handler {
            private WeakReference<Activity> reference;

            public checkHandler(Activity activity) {
                reference = new WeakReference<Activity>(activity);
            }

            @Override
            public void handleMessage(Message msg) {
                if (reference.get() != null) {
                    switch (msg.what) {
                        case 0:
                            // do something...
                            break;
                        default:
                            Log.i("socket.S", String.valueOf(socket));
                            break;
                    }
                }
            }
        }
    }

    private class sendService implements Runnable {
        private String msg;

        sendService(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            printWriter.println(this.msg);
        }
    }

    private void receiveMsg() {
        try {
            while (true) {                                      //步骤三
                if ((receiveMsg = in.readLine()) != null) {
                    Log.d(TAG, "receiveMsg:" + receiveMsg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(receiveMsg + "\n\n" + mTextView.getText());
                        }
                    });
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "receiveMsg: ");
            e.printStackTrace();
        }
    }

    private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_setting, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // 点击返回跳转到主页面
        Button s_toolbar_return = findViewById(R.id.s_toolbar_return);
        s_toolbar_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Setting.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // 点击空白区域隐藏键盘.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        EditText sEditText = findViewById(R.id.edittext_ipv4_num);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Setting.this.getCurrentFocus() != null) {
                if (Setting.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(Setting.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    sEditText.clearFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}