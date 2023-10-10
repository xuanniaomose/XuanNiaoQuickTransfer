package xuanniao.transmission.trclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.*;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static Handler handler_connect;
    public static Handler handler_check;
    public static Handler handler_send;
    public static Handler handler_sendF;
    public static Handler handler_recv_msg;
    public String Tag = "MAIN";
    SharedPreferences.Editor SPeditor;
    public static Socket socket;
    public String send_text;
    public EditText textview_send;
    private String time_chat;
    public MsgAdapter msg_adapter = new MsgAdapter();
    public static List<Msg> msgList;
    public List<String> fileList;
    public SharedPreferences SP;
    public TextView textview_ipv4num;
    private TextView textview_state0;
    private RecyclerView msgRecyclerView;
    private Button button_send;
    private boolean isRefuse;
    private Intent intent_SendFile;


    @SuppressLint({"CutPasteId", "WrongViewCast", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomActionBar();
        // 消息列表初始化
        initRecyclerView();
        // 读写权限检查
        checkReadWritePermission();
        // 网络权限检查
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //获取SharedPreferences对象
        SP = getSharedPreferences("config", Context.MODE_PRIVATE);
        SPeditor = SP.edit();
        // 获取当前时间
        SimpleDateFormat time_format =
                new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        long time_current = System.currentTimeMillis();
        time_chat = time_format.format(time_current);
        // 启动连接服务
        Log.i("主连接服务", "开启");
        Intent intent_Connect = new Intent(this, Connect.class);
        intent_Connect.putExtra("connect_order", 1);
        xuanniao.transmission.trclient.Connect.enqueueWork(MainActivity.this, intent_Connect);
        // 启动连接检查服务
        Log.i("连接检查服务", "开启");
        Intent intent_Check = new Intent(this, CheckConnect.class);
        intent_Check.putExtra("CheckConnect", 1);
        CheckConnect.enqueueWork(MainActivity.this, intent_Check);
        initView();
        // 设置网络状态提示
        handler_check = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                set_state(message);
            }
        };

        handler_connect = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                start_receive(message);
            }
        };

        handler_recv_msg = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                add_MsgList(message, msgList.size());
            }
        };

        handler_send = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    Toast.makeText(MainActivity.this,
                            "当前为离线模式，无法发送",Toast.LENGTH_SHORT).show();
                    Log.i(Tag,"当前为离线模式，无法发送");
                } else {
                    Log.d(Tag+" add_MsgList",message.arg1+" / "+message.arg2);
                    add_MsgList(message, message.what);
                    Log.i(Tag, "已发送" + message.obj);
                }
            }
        };

        handler_sendF = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    Toast.makeText(MainActivity.this,
                            "当前为离线模式，无法发送",Toast.LENGTH_SHORT).show();
                    Log.i(Tag,"当前为离线模式，无法发送");
                } else {
                    msg_adapter.notifyItemChanged(message.what, message.obj);
                }
            }
        };
    }

    private void initView() {
        textview_ipv4num = findViewById(R.id.textview_ipv4num);
        textview_state0 = findViewById(R.id.textview_state0);
        button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(sendBtn_Listener);
        textview_send = findViewById(R.id.edittext_send);
        Button button_music = findViewById(R.id.button_music);
        button_music.setOnClickListener(this::onClick);
        Button button_video = findViewById(R.id.button_video);
        button_video.setOnClickListener(this::onClick);
        Button button_image = findViewById(R.id.button_image);
        button_image.setOnClickListener(this::onClick);
        Button button_picture = findViewById(R.id.button_picture);
        button_picture.setOnClickListener(this::onClick);
        Button button_other = findViewById(R.id.button_other);
        button_other.setOnClickListener(this::onClick);
        textview_ipv4num.setText(SP.getString("ipv4", getString(R.string.ipv4)));
        intent_SendFile = new Intent(this, Send.class);
    }


    public Boolean checkReadWritePermission() {
        boolean isGranted = true;
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isGranted = false;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isGranted = false;
        }
        Log.i("读写权限获取"," ： "+isGranted);
        if (!isGranted) {
            this.requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    102);

        }
        return isGranted;
    }

    void openDocument(String path) {
        Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:" + path);
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    String path = "NULL";
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        fileList = new ArrayList<>();
        if (requestCode == 1 && data != null) {
            ClipData imageNames = data.getClipData();
            if (imageNames != null) {
                for (int i = 0; i < imageNames.getItemCount(); i++) {
                    uri = imageNames.getItemAt(i).getUri();
                    path = FileChooseUtil.getPath(this, uri);
                    fileList.add(path);
                    textview_send.append("@FileMark@" + uri + "@FileMarkEnd@");
                }
                Log.d("fileList", fileList.toString());
            } else {
                uri = data.getData();
                Log.d("uri", String.valueOf(uri));
                FileChooseUtil FileChooseUtil = new FileChooseUtil(this);
                path = FileChooseUtil.getPath(this, uri);
                textview_send.append("@FileMark@" + path + "@FileMarkEnd@");
                fileList.add(path);
                Log.d("fileList", fileList.toString());
            }
        } else if (data != null) {
                uri = data.getData();
                Log.d("uri", String.valueOf(uri));
                FileChooseUtil FileChooseUtil = new FileChooseUtil(this);
                path = FileChooseUtil.getPath(this, uri);
                textview_send.append("@FileMark@"+path+"@FileMarkEnd@");
                fileList.add(path);
                Log.d("fileList", fileList.toString());
        } else {
            Toast.makeText(MainActivity.this, "没有选择任何文件", Toast.LENGTH_SHORT).show();
        }
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
                Intent intent_Connect = new Intent(MainActivity.this, Connect.class);
                intent_Connect.putExtra("connect_order", 0);
                Connect.enqueueWork(MainActivity.this, intent_Connect);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showFalseWindow() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
            //.setIcon(R.mipmap.icon)//设置标题的图片
            .setTitle("连接失败")//设置对话框的标题
            .setMessage("是否重新连接")//设置对话框的内容
            //设置对话框的按钮
            .setNegativeButton("离线", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "离线模式下无法传输文件", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            })
            .setNeutralButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Setting.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            })
            .setPositiveButton("重连", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "开始重新连接", Toast.LENGTH_SHORT).show();
                    Intent intent_Connect = new Intent(MainActivity.this, Connect.class);
                    intent_Connect.putExtra("connect_order", 1);
                    xuanniao.transmission.trclient.Connect.enqueueWork(MainActivity.this, intent_Connect);
                    dialog.dismiss();
                }
            }).create();
        dialog.show();
    }

    private final View.OnClickListener sendBtn_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            send_text = textview_send.getText().toString();
            String send_msg;
            if (!"".equals(send_text)) {
                int mark = FileMark.check(send_text);
                Log.i("mark判定结果", String.valueOf(mark));
                if (mark == 1) {
                    // 如果连接已建立,标志为1,则开启文件传送服务
                    intent_SendFile.putStringArrayListExtra("path", (ArrayList<String>) fileList);
                    intent_SendFile.putExtra("position", msgList.size());
                    Send.enqueueWork(MainActivity.this, intent_SendFile);
//                    addMsgList(fileList.toString(),1);
                    textview_send.getText().clear();
                } else {
                    // 如果连接已建立,标志为其他,则发送字符串
                    Connect Connect = new Connect();
                    socket = Connect.getSocket();
                    if (!String.valueOf(socket).equals("null")) {
                        send_msg = send_text;
                        Send.sendM(send_text);
                        addMsgList(send_msg,0);
                    } else {
                        Toast.makeText(MainActivity.this,
                                "当前为离线模式，无法发送", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    };

    private void set_state(Message message) {
        if (message.what == 0) {
            textview_state0.setText("断开");
            textview_state0.setTextColor(0xffff0000);
            Log.i("网络状态", "断开");
        } else {
            textview_state0.setText("尝试");
            textview_state0.setTextColor(0xff0089fe);
            Log.i("网络状态","尝试连接");
        }
    }

    private void start_receive(Message message) {
        if (message.what == 1) {
            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            // 设置网络状态
            textview_state0.setText("连接");
            textview_state0.setTextColor(0xff008000);
            Intent intent_Receive = new Intent(MainActivity.this, Receive.class);
            intent_Receive.putExtra("Receive", 2);
            Receive.enqueueWork(MainActivity.this, intent_Receive);
        } else {
            showFalseWindow();
        }
    }

    private void add_MsgList(Message message, int position) {
        if (message.what == 0) {
            textview_state0.setText("断开");
            textview_state0.setTextColor(0xffff0000);
            Log.i("网络状态", "断开");
        } else {
            // 把新消息添加进msgList
            msgList.add(msgList.size(), new Msg((String)message.obj, message.what, time_chat, message.arg1, message.arg2));
            List<String> content_List2 = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
            List<Integer> type_List2 = msgList.stream().map(Msg::getRSType).collect(Collectors.toList());
            Log.i("MainSR消息列表2", content_List2.toString());
            Log.i("MainSR类型列表2", type_List2.toString());
            // msg_adapter通过局部更新方法添加尾项
            msg_adapter.notifyItemInserted(position);
            // msgRecyclerView跳转到尾项
            msgRecyclerView.scrollToPosition(position);
            Log.d(Tag+" add_MsgList",msgList.get(position).getFileType()+"");
        }
    }

    void onClick(View v) {
        String select_path;
        int id = v.getId();
        if (id == R.id.button_music) {
            select_path = "%2fMusic%2f";
        } else if (id == R.id.button_video) {
            select_path = "%2fDCIM%2fScreenrecorder%2f";
        } else if (id == R.id.button_image) {
            select_path = "%2fDCIM%2fCamera%2f";
        } else if (id == R.id.button_picture) {
            select_path = "%2fDCIM%2fScreenshots%2f";
        } else {
            select_path = "";
        }
        openDocument(select_path);
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

    // 初始化RecyclerView
    private void initRecyclerView() {
        msgRecyclerView = findViewById(R.id.recyclerView_chat);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(manager);
        msgRecyclerView.setAdapter(msg_adapter);
        msgList = new ArrayList<>();
        Log.i("MAIN", "初始化RecyclerView");
        SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        long time_current = System.currentTimeMillis();
        String time_chat = time_format.format(time_current);
        Msg msg1 = new Msg("欢迎使用玄鸟快传", 0, time_chat, 0, 0);
        msgList.add(msg1);
        msg_adapter.setMsgList(msgList);
    }

    void addMsgList(String send_msg, int fileType) {
        SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        long time_current = System.currentTimeMillis();
        String time_chat = time_format.format(time_current);
        msgList.add(new Msg(send_msg, 1, time_chat, fileType, 0)); //将输入的消息及其类型添加进消息数据列表中
        msg_adapter.notifyItemInserted(msgList.size());                 //为RecyclerView添加末尾子项
        msgRecyclerView.scrollToPosition(msgList.size()-1);             //跳转到当前位置
        textview_send.getText().clear();                                //清空输入框文本
        List<String> content_List2 = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
        List<Integer> type_List2 = msgList.stream().map(Msg::getRSType).collect(Collectors.toList());
        Log.i("MainSR消息列表2", content_List2.toString());
        Log.i("MainSR类型列表2", type_List2.toString());
    }
}