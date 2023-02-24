package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static xuanniao.transmission.trclient.Msg.TYPE_RECEIVED;

public class MainActivity extends AppCompatActivity {

    public static Handler handler_recv_msg;
    public String receiveMsg;
    public String send_text;
    public EditText inputText;
    public static MsgAdapter msg_adapter;
    private Button button_send;
    private TextView textview_state0;
    private RecyclerView msgRecyclerView;
    private List<Msg> msgList;


    @SuppressLint({"CutPasteId", "WrongViewCast", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomActionBar();
        initRecyclerView();

        // 获取网络权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // 启动连接服务
        Connect Connect = new Connect();
        Log.i("连接", "开始");
        Connect.connect();
        // 启动接收服务
        Intent intent_Receive = new Intent(this, Receive.class);
        intent_Receive.putExtra("Receive",10111);
        Receive.enqueueWork(MainActivity.this,intent_Receive);
        Log.i("MAIN", "启动接收服务");
        handler_recv_msg = new Handler(){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    Log.i("消息列表长度", String.valueOf(msgList.size()));
                    // 把新消息添加进msgList
                    msgList.add(new Msg((String) message.obj, TYPE_RECEIVED));
                    msg_adapter.notifyItemInserted(msgList.size()-1);
                    // msg_adapter通过局部更新方法添加尾项
                    msg_adapter.notifyItemChanged(msgList.size()-1);
                    // msgRecyclerView跳转到尾项
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
            }
        };

        // 设置网络状态提示
//        textview_state0 = findViewById(R.id.textview_state0);
//        socket = ((Connect)getApplication()).getSocket();
//        if (socket == null) {
//            textview_state0.setText("连接");
//            textview_state0.setTextColor(0xff008000);
//        } else {
//            textview_state0.setText("断开");
//            textview_state0.setTextColor(0xffff0000);
//        }
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

        button_send = findViewById(R.id.button_send);
        inputText = findViewById(R.id.edittext_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_text = inputText.getText().toString();
                if (!"".equals(send_text)) {
                    Log.i("消息列表长度", String.valueOf(msgList.size()));
                    Connect Connect = new Connect();
                    Connect.send(send_text);
                    msgList.add(new Msg(send_text, Msg.TYPE_SENT));         //将输入的消息及其类型添加进消息数据列表中
                    Log.i("消息列表长度", String.valueOf(msgList.size()));
                    msg_adapter.notifyItemInserted(msgList.size() + 1);   //为RecyclerView添加末尾子项
                    Log.i("消息列表长度", String.valueOf(msgList.size()));
//                      msg_adapter.setMsgList(msgList);
                    msg_adapter.notifyItemChanged(msgList.size());
                    msgRecyclerView.scrollToPosition(msgList.size());       //跳转到当前位置
                    inputText.getText().clear();                            //清空输入框文本
                }
            }
        });
    }

    // 初始化RecyclerView
    private void initRecyclerView() {
        msgRecyclerView = findViewById(R.id.recyclerView);
        msgList = new ArrayList<>();
        Msg msg1 = new Msg("欢迎使用玄鸟快传", 0);
        msgList.add(msg1);
        msg_adapter = new MsgAdapter();
        msg_adapter.setMsgList(msgList);
        msg_adapter.notifyDataSetChanged();
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        // 设置布局管理器
        msgRecyclerView.setLayoutManager(manager);
        // 设置adapter
        msgRecyclerView.setAdapter(msg_adapter);
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