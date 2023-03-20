package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.io.*;

public class Setting extends AppCompatActivity {
    public static Handler handler_connect_on;
    private static final String Tag = "Setting";
    private boolean isRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Tag, "已启动");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setCustomActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }

        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取editor对象的引用
        SharedPreferences.Editor SPeditor = sharedPreferences.edit();

        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // 开启即连复选
        CheckBox checkBox1 = findViewById(R.id.checkBox1);
        int open_auto_connect = sharedPreferences.getInt("open_auto_connect",1);
        checkBox1.setChecked(open_auto_connect == 1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPeditor.putInt("open_auto_connect", 1);
                    SPeditor.apply();
                } else {
                    SPeditor.putInt("open_auto_connect", 0);
                    SPeditor.apply();
                }
            }
        });

        // 电脑端ipv4地址
        TextView textview_ipv4 = findViewById(R.id.edittext_ipv4_num);
        String ipv4 = sharedPreferences.getString("ipv4", "192.168.0.0");
        textview_ipv4.setText(ipv4);
        textview_ipv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //逻辑开始前，先移出监听
                textview_ipv4.removeTextChangedListener(this);
                // 在俩者区间进行逻辑处理
                String input_ipv4 = textview_ipv4.getText().toString();
                SPeditor.putString("ipv4", input_ipv4);
                SPeditor.apply();
                //逻辑结束后，在加入监听
                textview_ipv4.addTextChangedListener(this);
                Log.i(Tag,"设置ipv4:"+input_ipv4);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 连接开关
        TextView textview_state1 = findViewById(R.id.textview_state1);
        Switch switch_connect = findViewById(R.id.switch_connect);
//        handler_connect_on = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                if (message.what == 1) {
//                    switch_connect.setChecked(true);
//                    return true;
//                } else {
//                    switch_connect.setChecked(false);
//                    return false;
//                }
//            }
//        });
        int connect_status = sharedPreferences.getInt("connect_status", 0);
        if (connect_status == 1) {
            switch_connect.setChecked(true);
        } else {
            switch_connect.setChecked(false);
        }
        switch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent_Connect = new Intent(Setting.this, Connect.class);
                if (isChecked) {
                    intent_Connect.putExtra("connect_order", 1);
                    SPeditor.putInt("connect_status", 1);
                    SPeditor.apply();
                    textview_state1.setText("连接状态：开启");
                    Log.i("连接", "已开启开关");
                } else {
                    intent_Connect.putExtra("connect_order", 0);
                    SPeditor.putInt("connect_status", 0);
                    SPeditor.apply();
                    textview_state1.setText("连接状态：断开");
                    Log.i("断开", "已关闭开关");
                }
                Connect.enqueueWork(Setting.this, intent_Connect);
            }
        });

        Button button_path = findViewById(R.id.button_path);
        button_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");//想要展示的文件类型
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
                startActivityForResult(intent, 0);
            }
        });

        // 接收文件的路径
        EditText editText_path = findViewById(R.id.editText_path);
        String receive_path = sharedPreferences.getString("receive_path","/storage/emulated/0/Book/");
        editText_path.setText(receive_path);
        editText_path.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //逻辑开始前，先移出监听
                editText_path.removeTextChangedListener(this);
                // 在俩者区间进行逻辑处理
                String receive_path = editText_path.getText().toString();
                SPeditor.putString("receive_path", receive_path);
                SPeditor.apply();
                //逻辑结束后，在加入监听
                editText_path.addTextChangedListener(this);
                Log.i(Tag,"设置receive_path:"+receive_path);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receive_path = editText_path.getText().toString();
                SPeditor.putString("receive_path", receive_path);
                SPeditor.apply();
            }
        });
    }

    String path = "NULL";
    @SuppressLint("SetTextI18n")
    @Override
    // 带回授权结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText editText_path = findViewById(R.id.editText_path);
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取editor对象的引用
        SharedPreferences.Editor SPeditor = sharedPreferences.edit();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.i("Setting获取到的uri", String.valueOf(uri));
            FileChooseUtil FileChooseUtil = new FileChooseUtil(this);
            path = FileChooseUtil.getPath(this, uri);
            editText_path.setText(path);
            SPeditor.putString("receive_path", path);
            SPeditor.apply();
        }
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 检查是否有权限
            if (Environment.isExternalStorageManager()) {
                isRefuse = false;
                // 授权成功
            } else {
                isRefuse = true;
                // 授权失败
            }
        }
    }

    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
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