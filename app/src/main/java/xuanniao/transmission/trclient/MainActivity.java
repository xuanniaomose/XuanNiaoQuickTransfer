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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.BreakIterator;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity {

    public static Handler handler_recv_msg;
    public static Handler handler_check;
    public String receiveMsg;
    public String send_text;
    public EditText inputText;
    public static MsgAdapter msg_adapter = new MsgAdapter();
    public static List<Msg> msgList;
    private TextView textview_state0;
    private RecyclerView msgRecyclerView;
    private boolean isRefuse;


    @SuppressLint({"CutPasteId", "WrongViewCast", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }

        msgRecyclerView = findViewById(R.id.recyclerView);
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        // 设置布局管理器
        msgRecyclerView.setLayoutManager(manager);
        // 设置adapter
        msgRecyclerView.setAdapter(msg_adapter);
        msgList = new ArrayList<>();
        initRecyclerView();

        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // 启动连接服务
        Connect Connect = new Connect();
        Log.i("连接", "开始");
        Connect.connect();
        // 启动连接检查服务
        Log.i("连接检查服务", "开启");
        Intent intent_Check = new Intent(this, CheckConnect.class);
        intent_Check.putExtra("CheckConnect", 2);
        CheckConnect.enqueueWork(MainActivity.this, intent_Check);
        Log.i("MAIN", "启动连接检查服务");
        // 设置网络状态提示
        textview_state0 = findViewById(R.id.textview_state0);
        handler_check = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.obj == "true") {
                    textview_state0.setText("连接");
                    textview_state0.setTextColor(0xff008000);
                    Log.i("网络状态","连接");
                } else {
                    textview_state0.setText("断开");
                    textview_state0.setTextColor(0xffff0000);
                    Log.i("网络状态","断开");
                }
            }
        };
        // 启动接收服务
        Log.i("接收服务", "开启");
        Intent intent_Receive = new Intent(this, Receive.class);
        intent_Receive.putExtra("Receive", 3);
        Receive.enqueueWork(MainActivity.this, intent_Receive);
        Log.i("MAIN", "启动接收服务");

        handler_recv_msg = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    // 把新消息添加进msgList
                    msgList.add(msgList.size(), new Msg((String) message.obj, message.what));
                    List<String> content_List2 = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
                    List<Integer> type_List2 = msgList.stream().map(Msg::getType).collect(Collectors.toList());
                    Log.i("MainSR消息列表2", content_List2.toString());
                    Log.i("MainSR类型列表2", type_List2.toString());
                    // msg_adapter通过局部更新方法添加尾项
                    msg_adapter.notifyItemInserted(msg_adapter.getItemCount() - 1);
                    // msgRecyclerView跳转到尾项
                    msgRecyclerView.scrollToPosition(msg_adapter.getItemCount() - 1);
                } else if (message.what == 2) {
                    textview_state0.setText("断开");
                    textview_state0.setTextColor(0xffff0000);
                    Log.i("网络状态","断开");
                }
            }
        };

        // 监听输入框
        SharedPreferences setInfo = getSharedPreferences("SetInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = setInfo.edit();
        EditText edittext_send = findViewById(R.id.edittext_send);
        send_text = String.valueOf(edittext_send);
        edittext_send.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
//                    edittext = textView.getText().toString();
                    editor.putString("上传文本", send_text);
                    editor.apply();
                    Log.i("上传之文本：", send_text);
                }
                return false;
            }
        });

        Button button_send = findViewById(R.id.button_send);
        inputText = findViewById(R.id.edittext_send);
        Intent intent_SendFile = new Intent(this, SendFile.class);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_text = inputText.getText().toString();
                String send_msg;
                if (!"".equals(send_text)) {
                    Connect Connect = new Connect();
                    int mark = FileMark.check(send_text);
                    Log.i("mark判定结果", String.valueOf(mark));
                    if (mark == 1) {
                        String fpath = FileMark.fpath(send_text);
                        Log.i("fpath", fpath);
                        String name = FileMark.name(fpath);
                        send_msg = name;
                        // 开启文件传送服务
//                        intent_SendFile.putExtra("path", fpath);
                        SendFile.send_file(new File(fpath));
                    } else {
                        send_msg = send_text;
                        Connect.send(send_text);
                    }
                    msgList.add(new Msg(send_msg, 1));         //将输入的消息及其类型添加进消息数据列表中
                    msg_adapter.notifyItemInserted(msgList.size()-1);   //为RecyclerView添加末尾子项
                    msgRecyclerView.scrollToPosition(msgList.size()-1);       //跳转到当前位置
                    inputText.getText().clear();                            //清空输入框文本
                    List<String> content_List2 = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
                    List<Integer> type_List2 = msgList.stream().map(Msg::getType).collect(Collectors.toList());
                    Log.i("MainSR消息列表2", content_List2.toString());
                    Log.i("MainSR类型列表2", type_List2.toString());
                }
            }
        });

        Button button_music = findViewById(R.id.button_music);
        button_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取音频文件
                String path = "%2fDCIM%2fMusic%2f";
                openDocument(path);
            }
        });

        Button button_video = findViewById(R.id.button_video);
        button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取视频文件
                String path = "%2fDCIM%2fScreenrecorder%2f";
                openDocument(path);
            }
        });

        Button button_image = findViewById(R.id.button_image);
        button_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // 获取图片文件
                String path = "%2fDCIM%2fCamera%2f";
                openDocument(path);
            }
        });

        Button button_picture = findViewById(R.id.button_picture);
        button_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取截图文件
                String path = "%2fDCIM%2fScreenshots%2f";
                openDocument(path);
            }
        });

        Button button_other = findViewById(R.id.button_other);
        button_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取其他文件
                String path = "";
                openDocument(path);
            }
        });
    }

    void openDocument(String path) {
        Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:" + path);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");//想要展示的文件类型
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, 0);
    }

    String path = "NULL";
    @SuppressLint("SetTextI18n")
    @Override
    // 带回授权结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.i("MAIN获取到的uri", String.valueOf(uri));
            FileChooseUtil FileChooseUtil = new FileChooseUtil(this);
            path = FileChooseUtil.getPath(this, uri);
            inputText.setText("@FileMark@" + path + "@FileMarkEnd@");
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

    // 初始化RecyclerView
    private void initRecyclerView() {
        Log.i("MAIN", "初始化RecyclerView");
        Msg msg1 = new Msg("欢迎使用玄鸟快传", 1);
        msgList.add(msg1);
        Msg msg2 = new Msg("服务器端已连接", 0);
        msgList.add(msg2);
        msg_adapter.setMsgList(msgList);

    }

    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar,
                new LinearLayout(this), true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // 点击设置跳转到设置页面
        Button a_toolbar_setting = findViewById(R.id.a_toolbar_setting);
        a_toolbar_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
        // 点击退出结束程序
        Button a_toolbar_return = findViewById(R.id.a_toolbar_return);
        a_toolbar_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect Connect = new Connect();
                Connect.disconnect(a_toolbar_return);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // 点击空白区域隐藏键盘 .
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        EditText edittext_send = findViewById(R.id.edittext_send);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (MainActivity.this.getCurrentFocus() != null) {
                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    edittext_send.clearFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}