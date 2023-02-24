package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private TextView mTextView;
    private static final String TAG = "TAG";
    private static final String HOST = "192.168.1.16";
    private static final int PORT = 9999;
    private ExecutorService mExecutorService = null;
    private String receiveMsg;

    private BufferedReader in = null;
    private PrintWriter printWriter=null;
    private List<Socket> mList = new ArrayList<Socket>();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mEditText = (EditText) findViewById(R.id.edittext_send);
        mTextView = (TextView) findViewById(R.id.textview_recive);
        mExecutorService = Executors.newCachedThreadPool();

        // 连接开关
        TextView textview_switch;
        textview_switch = (TextView) findViewById(R.id.textview_switch);
        Switch switch_connect;
        switch_connect = (Switch) findViewById(R.id.switch_connect);
        switch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    textview_switch.setText("连接开启");
                    connect(switch_connect);
                }else {
                    disconnect(switch_connect);
                    textview_switch.setText("连接断开");
                }
            }
        });

        // 监听输入框
        // 获取Shared Preferences对象
        SharedPreferences setInfo = getSharedPreferences("SetInfo", MODE_PRIVATE);
        // 获取Editor
        SharedPreferences.Editor editor = setInfo.edit();
        EditText edittext_send;
        edittext_send = findViewById(R.id.edittext_send);
        String sendMsg = String.valueOf(edittext_send);
        edittext_send.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    receiveMsg = textView.getText().toString();
                    mTextView.setText(mTextView.getText() + "\n\n" + receiveMsg);
                    editor.putString("upload_text", sendMsg);
                    editor.commit();
                    Log.i("上传之文本：", sendMsg);
                }
                return false;
            }
        });

        // 按发送键发送字符串
        Button button_upload;
        button_upload = (Button) findViewById(R.id.button_upload);
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("发送键", "onClick");
                send(button_upload);
                // 发送后清除输入框中内容
                mEditText.setText("");
            }
        });
    }

    public void connect(View view) {
        mExecutorService.execute(new connectService());  //在一个新的线程中请求 Socket 连接
    }

    public void send(View view) {
        String sendMsg = mEditText.getText().toString();
        mExecutorService.execute(new sendService(sendMsg));
    }

    public void disconnect(View view) {
        mExecutorService.execute(new sendService("0"));
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

    private class connectService implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(HOST, PORT);
                socket.setSoTimeout(60000);
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                receiveMsg();
            } catch (Exception e) {
                Log.e(TAG, ("connectService:" + e.getMessage()));
            }
        }
    }

    private void receiveMsg() {
        try {
            while (true) {
                if ((receiveMsg = in.readLine()) != null) {
                    Log.d(TAG, "receiveMsg:" + receiveMsg);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
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

    // 点击空白区域隐藏键盘.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (MainActivity.this.getCurrentFocus() != null) {
                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mEditText.clearFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}