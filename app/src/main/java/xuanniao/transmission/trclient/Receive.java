package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.net.SocketException;

public class Receive extends JobIntentService {

    String Tag = "Receive";
    private final int Time = 5000;    //时间间隔，单位 ms
    int N = 0;      //用来观测重复执行
    static final int JOB_ID = 2;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Receive.class, JOB_ID, work);
    }

    @SuppressLint({"ServiceCast", "WrongConstant", "UnspecifiedImmutableFlag"})
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Tag,"创建");
    }
    //  不要重写onBind方法，否则会报错

    @Override
    // 服务功能的设置
    protected void onHandleWork(@NonNull Intent intent) {
        // 每隔一段时间重复执行接收信息线程
        Log.i(Tag, "服务已开启");
        Looper.prepare();
        Handler handler_recv_time = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                handler_recv_time.postDelayed(this, Time);
                //每隔一段时间要重复执行的代码
                Connect Connect = new Connect();
                String receiveMsg = Connect.receiveMsg();
                if (receiveMsg != null) {
                    Log.d("接收消息函数标记3", receiveMsg);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = receiveMsg;
//                    if (receiveMsg.equals("准备接收")) {
//                        SendFile.handler_recv_msg.sendMessage(message);
//                    } else {
                    MainActivity.handler_recv_msg.sendMessage(message);
//                    }
                }
                N = N + 1;
                Log.i(Tag, "第" + N + "次执行");
            }
        };
        handler_recv_time.postDelayed(runnable, Time);    //启动计时器
        Looper.loop();
    }
}