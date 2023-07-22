package xuanniao.transmission.trclient;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Send extends JobIntentService {
    private static final int JOB_ID = 3;
    public static String Tag = "Send";
    public static Socket socket;
    private static ArrayList<String> paths;
    private String msg;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Send.class, JOB_ID, work);
        paths = work.getStringArrayListExtra("path");
    }

    @Override
    // 服务功能的设置
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            for (int i=0; i < paths.size(); i++) {
                File path = new File(paths.get(i));
                Connect Connect = new Connect();
                socket = Connect.getSocket();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//                dos.writeUTF("@FMark@"+path.getName()+"@FName@"+(int)path.length()+"@FLen@");
                dos.write(("@FMark@" + path.getName() + "@FName@" + (int) path.length() + "@FLen@").getBytes());
                Log.i(Tag, "文件信息已发送");
                dos.flush();
                sendF(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Message msg_s = new Message();
            msg_s.what = 0;
            MainActivity.handler_send.sendMessage(msg_s);
        }
    }

    //MyService用ServiceHandler接收并处理来自于客户端的消息
    private static void sendF(File path) {
        try{
            if (!String.valueOf(socket).equals("")) {
                Log.i(Tag, "开始发送");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                // 获取文件的字节流
                FileInputStream fis = new FileInputStream(path);
                // 向输出流加载数据
                int file_len_k = ((int) path.length()) / 1024;
                int len = -1;
                int n = 0;
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer, 0, 1024)) != -1) {
                    n++;
//                    // 计算上传百分比
//                    int parseInt = Integer.parseInt(new String(buffer,0,len));
//                    if (parseInt == 0) {
//                        Log.d("已上传完成:",parseInt+"%");
//                    }else{
//                        Log.d("已上传完成:",PercentageUtils.division(parseInt, (int)path.length())+"%");
//                    }
                    dos.write(buffer, 0, len);
                    dos.flush();
                    if (n > file_len_k) {
                        fis.close();
                        break;
                    }
                }
//                Log.i(Tag,"发送完成");
//
//                //获取上传信息
//                num = in.read(bufIn);
//                System.out.println(new String(bufIn,0,num));
                //关闭资源
                fis.close();
//                dos.close();
//                socket.close();
                sleep(1000);
            } else {
                Message msg_s = new Message();
                msg_s.what = 0;
                MainActivity.handler_send.sendMessage(msg_s);
            }
            Log.i(Tag,"发送完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendM(String msg) {
        try {
            Connect Connect = new Connect();
            socket = Connect.getSocket();
            Log.i("socket.S", String.valueOf(socket));
            // 步骤1：从Socket 获得输出流对象OutputStream
            // 该对象作用：发送数据
            OutputStream outputStream = socket.getOutputStream();
            // 步骤2：写入需要发送的数据到输出流对象中
            outputStream.write((msg + "\n").getBytes("utf-8"));
            // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
            // 步骤3：发送数据到服务端
            outputStream.flush();
            // 步骤4：关闭输出流
//                outputStream.close();
            Log.i("输出线程", "此时应该结束任务");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}