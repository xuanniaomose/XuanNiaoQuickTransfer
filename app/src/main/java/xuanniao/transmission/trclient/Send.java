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
import java.util.List;

import static java.lang.Thread.sleep;

public class Send extends JobIntentService {
    private static final int JOB_ID = 3;
    public static String Tag = "Send";
    public static Socket socket;
    private static ArrayList<String> paths;
    private static int position;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Send.class, JOB_ID, work);
        paths = work.getStringArrayListExtra("path");
        position = work.getIntExtra("position",0);
    }

    @Override
    // 服务功能的设置
    protected void onHandleWork(@NonNull Intent intent) {
        int fileSize = 0;
        String fileName = "NONE";
        if (paths.size() > 1) {
            for (String path : paths) {
                File file = new File(path);
                fileSize += (int) file.length();
            }
        File file = new File(paths.get(0));
        fileName = file.getName();
        if (fileName.length() > 21) {
            fileName = fileName.substring(0,10) + "..." + fileName.substring(fileName.length()-8,fileName.length()) + "等" + paths.size() + "个文件";
            }
        }
        Message msg_s = MainActivity.handler_send.obtainMessage();
        msg_s.what = position;
        msg_s.arg1 = 1;
        msg_s.arg2 = fileSize;
        msg_s.obj = fileName;
        MainActivity.handler_send.sendMessage(msg_s);

        sendLoop(paths);

        Message msg_sf = MainActivity.handler_sendF.obtainMessage();
        msg_sf.what = position;
        msg_sf.arg1 = 1;
        msg_sf.obj = fileSize/1024;
        MainActivity.handler_sendF.sendMessage(msg_sf);
    }

    private static void sendLoop(ArrayList<String> paths) {
        try {
            int n = 0;
            for (String path : paths) {

                Connect Connect = new Connect();
                socket = Connect.getSocket();
                File file = new File(path);
                String fileName = file.getName();
                int fileSize = (int) file.length();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.write(("@FMark@" + fileName + "@FName@" + fileSize + "@FLen@").getBytes());

                Log.i(Tag, "文件信息已发送");
                dos.flush();
                n += fileSize/1024;
                sendF(file, n);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //MyService用ServiceHandler接收并处理来自于客户端的消息
    private static void sendF(File path, int n) {
        try{
            if (!String.valueOf(socket).equals("")) {
                Log.i(Tag, "开始发送");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                // 获取文件的字节流
                FileInputStream fis = new FileInputStream(path);
                // 向输出流加载数据
                int file_len_k = ((int) path.length()) / 1024;
                int len = -1;
                int change = 0;
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer, 0, 1024)) != -1) {
                    Log.d(Tag+" change和n", change+" / "+n);
                    if (n != change ) {
                        change = n;
                        Log.d(Tag+" change", change+" / ");
                        Message msg_sf = MainActivity.handler_sendF.obtainMessage();
                        msg_sf.what = position;
                        msg_sf.arg1 = 1;
                        msg_sf.obj = change;
                        MainActivity.handler_sendF.sendMessage(msg_sf);
                    }
                    n++;
                    dos.write(buffer, 0, len);
                    dos.flush();
                    if (n > file_len_k) {
                        fis.close();
                        break;
                    }
                }
                //关闭资源
                fis.close();
//                dos.close();
//                socket.close();
                sleep(1000);
            } else {
                Message msg_sf = MainActivity.handler_sendF.obtainMessage();
                msg_sf.what = 0;
                MainActivity.handler_send.sendMessage(msg_sf);
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
//            Log.i("输出线程", "此时应该结束任务");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int fileSizeChange(int k) {
        int result;
        int m = k / 1024;
        if (m == 0){
            result = k;
        } else {
            int g = m / 1024;
            if (g == 0){
                result = m;
            } else {
                result = g;
            }
        }
//        if (result/10 > 0) {
//            int r = result * 1024;
//        }
        return result;
    }
}