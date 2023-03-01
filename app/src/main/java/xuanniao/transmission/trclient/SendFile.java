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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import static android.app.PendingIntent.getService;
import static android.content.Intent.getIntent;
import static xuanniao.transmission.trclient.Connect.socket;

public class SendFile extends JobIntentService {

    String Tag = "Receive";
    private final int Time = 5000;    //时间间隔，单位 ms
    int N = 0;      //用来观测重复执行
    public Socket socket = Connect.socket;
    static final int JOB_ID = 3;
    private FileInputStream fis;
    private DataOutputStream dos;
    private static File path;
    private byte[] bytes;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SendFile.class, JOB_ID, work);
        path = new File(work.getStringExtra("path"));
    }

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
        Handler handler_send_file = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    while (true) {
//                        MainActivity.textview_sendstate.setText("发送中");
                        dos = new DataOutputStream(socket.getOutputStream());
                        fis = new FileInputStream(path);
                        bytes = new byte[(int)path.length()];
                        //传输文件名称
                        dos.writeUTF(path.getName());
                        dos.flush();
                        //传输文件长度
                        dos.writeLong((int)path.length());
                        dos.flush();
                        int len=-1;
                        // 将文件读取到字节数组
                        while ((len = fis.read(bytes)) != -1) {
                            // 把字节数组输出
                            dos.write(bytes);
                        }
                        dos.flush();
                        fis.close();
                        dos.close();
//                        MainActivity.progress.setText(100 + "%");
//                        MainActivity.filename.setText("文件("+file.getName()+")发送完毕");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                N = N + 1;
                Log.i(Tag, "第" + N + "次执行");
            }
        };
//        handler_recv_time.postDelayed(runnable, Time);	//启动计时器
        Looper.loop();
    }
}