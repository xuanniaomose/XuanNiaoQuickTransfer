package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class Setting extends AppCompatActivity {
    private static final String Tag = "Setting";
    SharedPreferences.Editor SPeditor;
    TextView textview_ipv4;
    Button button_path;
    EditText editText_path;
    String ipv4;
    String receive_path;
    private boolean isRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Tag, "已启动");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setCustomActionBar();
        // 检查权限
        authority();
        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //获取SharedPreferences及其editor
        SharedPreferences SP = getSharedPreferences("config", Context.MODE_PRIVATE);
        SPeditor = SP.edit();
        ipv4 = SP.getString("ipv4", "192.168.0.0");
        receive_path = SP.getString("receive_path","/storage/emulated/0/");
        initView(ipv4, receive_path);
    }

    private void initView(String ipv4, String receive_path) {
        textview_ipv4 = findViewById(R.id.edittext_ipv4_num);
        button_path = findViewById(R.id.button_path);
        editText_path = findViewById(R.id.editText_path);
        textview_ipv4.setText(ipv4);
        textview_ipv4.addTextChangedListener(ipv4Listener);
        button_path.setOnClickListener(path_btn_Listener);
        editText_path.setText(receive_path);
        editText_path.addTextChangedListener(path_text_Listener);
    }

    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this)
                .inflate(R.layout.actionbar_setting, null);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.setClass(Setting.this,MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private TextWatcher ipv4Listener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // 逻辑开始前，先移出监听
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
    };

    private final View.OnClickListener path_btn_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent_F = new Intent();
            intent_F.setClass(Setting.this, FolderSelector.class);
            intent_F.putExtra("base_path", receive_path);
            startActivityForResult(intent_F, 1);
        }
    };

    String path = "NULL";
    @SuppressLint("SetTextI18n")
    @Override
    // 带回接收文件的路径
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText editText_path = findViewById(R.id.editText_path);
        //获取SharedPreferences对象
        SharedPreferences SP = getSharedPreferences(
                "config", Context.MODE_PRIVATE);
        //获取editor对象的引用
        SharedPreferences.Editor SPeditor = SP.edit();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Uri.parse(data.getStringExtra("data"));
            editText_path.setText(uri + "/");
            SPeditor.putString("receive_path", uri + "/");
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

    private final TextWatcher path_text_Listener = new TextWatcher() {
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
    };

    void authority(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }
    }

    // 点击空白区域隐藏键盘.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        EditText sEditText = findViewById(R.id.edittext_ipv4_num);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Setting.this.getCurrentFocus() != null) {
                if (Setting.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(
                            Setting.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    sEditText.clearFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}