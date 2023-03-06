package xuanniao.transmission.trclient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.*;
import java.net.Socket;

public class SendFile extends JobIntentService {
    private static final int JOB_ID = 3;
    public static String Tag = "SendFile";
    public static Socket socket;
    private static File path;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SendFile.class, JOB_ID, work);
        path = new File(work.getStringExtra("path"));
    }

    @Override
    // 服务功能的设置
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            Connect Connect = new Connect();
            socket = Connect.getSocket();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//            dos.writeUTF("@FMark@"+path.getName()+"@FName@"+(int)path.length()+"@FLen@");
            dos.write(("@FMark@"+path.getName()+"@FName@"+(int)path.length()+"@FLen@").getBytes());
            Log.i(Tag,"文件信息已发送");
//            dos.flush();
            FileInputStream fis = new FileInputStream(path);
            int file_len_k = ((int)path.length())/1024;
            int len = -1;
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer, 0, 1024)) != -1) {
                n ++;
                dos.write(buffer, 0, len);
                dos.flush();
                if (n>file_len_k) {
                    fis.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //MyService用ServiceHandler接收并处理来自于客户端的消息
    private static void send(File path) {
        try{
            Log.i(Tag,"开始发送");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // 获取文件的字节流
            FileInputStream fis = new FileInputStream(path);
            // 向输出流加载数据
            int file_len_k = ((int)path.length())/1024;
            int len = -1;
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer, 0, 1024)) != -1) {
                n ++;
                dos.write(buffer, 0, len);
                dos.flush();
                if (n>file_len_k) {
                    fis.close();
                    break;
                }
            }
            Log.i(Tag,"发送完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}