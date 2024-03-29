package xuanniao.transmission.trclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Receive extends JobIntentService {
    String Tag = "Receive";
    private final int Time = 5000;    //时间间隔，单位 ms
    int N = 0;      //用来观测重复执行
    static final int JOB_ID = 2;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Receive.class, JOB_ID, work);
    }

    @Override
    // 服务功能的设置
    protected void onHandleWork(@NonNull Intent intent) {
        // 每隔一段时间重复执行接收信息线程
        Log.i(Tag, "服务已开启");
        Looper.prepare();
        Handler handler_recv_time = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler_recv_time.postDelayed(this, Time);
                //每隔一段时间要重复执行的代码
                String receiveMsg = receiveMsg();
                if (receiveMsg != null) {
                    Log.d("接收消息函数标记3", receiveMsg);
                    int fmark = FileCheck(receiveMsg);
                    if (fmark == 1){
                        receiveMsg = WriteFile(receiveMsg);
                    }
                    Message message = new Message();
                    message.what = 0;
                    message.obj = receiveMsg;
                    MainActivity.handler_recv_msg.sendMessage(message);
                }
                N = N + 1;
                Log.i(Tag, "第" + N + "次执行");
            }
        };
        handler_recv_time.postDelayed(runnable, Time);    //启动计时器
        Looper.loop();
    }

    public String receiveMsg() {
        String receiveMsg = null;
        try {
            Connect Connect = new Connect();
            Socket socket = Connect.getSocket();
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            Log.d("接收消息函数标记1", String.valueOf(bufferedReader));
            byte[] buffer = new byte[1024];
            int temp = 0;
            try {
                temp = inputStream.read(buffer);
            } catch (SocketException e) {
                Message message = new Message();
                message.what = 2;
                message.obj = "对端掉线";
                Log.i("提示", "对端掉线");
                MainActivity.handler_recv_msg.sendMessage(message);
            }
            if (temp != -1) {
                receiveMsg = (new String(buffer, 0, temp));
                Log.d("接收消息函数标记2", "temp:"+ temp);
            } else {
                Log.d("接收消息函数标记2", "没有收到信息");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }

    public int FileCheck(String receiveMsg){
        int fmark;
        String mark = "M";
        Pattern regex = Pattern.compile("(?<=@).*(?=Mark@)", Pattern.DOTALL);
        Matcher mark_mat = regex.matcher(receiveMsg);
        if (mark_mat.find()) {
            try {
                mark = mark_mat.group(0);
            }catch (IllegalStateException e){
                Log.i(Tag, "No successful match so far");
            }
        } else {
            mark = "M";
        }
        if (Objects.equals(mark, "F")) {
            fmark = 1;
        } else {
            fmark = 0;
        }
        Log.i(Tag, String.valueOf(fmark));
        return fmark;
    }

    public String WriteFile(String receiveMsg) {
        String file_name = "错误";
        int file_len = 0;
        Pattern regex1 = Pattern.compile("(?<=@FMark@).*?(?=@FName@)", Pattern.DOTALL);
        Matcher file_n = regex1.matcher(receiveMsg);
        while (file_n.find()) {
            file_name = file_n.group(0);
        }
        Pattern regex2 = Pattern.compile("(?<=@FName@).*?(?=@FLen@)", Pattern.DOTALL);
        Matcher file_l = regex2.matcher(receiveMsg);
        while (file_l.find()) {
            file_len = Integer.parseInt(Objects.requireNonNull(file_l.group(0)));
        }
        Log.i(Tag, file_name);
        Log.i(Tag, String.valueOf(file_len));
        //获取SharedPreferences对象
        SharedPreferences SP = getSharedPreferences("config", Context.MODE_PRIVATE);
        String file_path = SP.getString("receive_path","/storage/emulated/0/");
        Log.i(Tag,file_path);
//        String file_path = "/storage/emulated/0/Book/";
        File f = new File(file_path+file_name);
        Log.i(Tag, file_path+file_name);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Connect Connect = new Connect();
            Socket socket = Connect.getSocket();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
//            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            FileOutputStream fos = new FileOutputStream(f, true); // true代表追加模式

            byte[] buffer = new byte[1024];
            int len = 0;
            int n = 0;
            int file_len_k = (file_len)/1024;
            Log.i(Tag, "准备写入");
            while ((len = dis.read(buffer)) != -1) {
                n = n + 1;
                Log.i(Tag, Arrays.toString(buffer));
                fos.write(buffer, 0, len);
                fos.flush();
                Log.i(Tag, String.valueOf(n));
//                Log.i("接收进度：",(n/file_len_k*100)+"%");
                if (n>file_len_k) {
                    fos.close();
                    Log.i("接收完成：","接收完成");
                    break;
                }
            }

//            BufferedReader br = new BufferedReader(new InputStreamReader(is, "gb2312"));
//
//            char[] char_buffer = new char[1024];
//            int len = 0;
//            int n = 0;
//            int file_len_k = (file_len)/1024;
//            Log.i(Tag, "准备写入");
//            while ((len = br.read(char_buffer, 0, 1024)) != -1) {
//                n ++;
//                byte[] buffer = CharByteKit.getBytes(char_buffer);
//                fos.write(buffer, 0, len);
//                fos.flush();
////                Log.i("接收进度：",(n/file_len_k*100)+"%");
//                if (n>file_len_k) {
//                    fos.close();
//                    break;
//                }
//            }
            Log.i("接收完成：","接收完成f");
            } catch (IOException e) {
                Log.i(Tag, "写入文件失败"+e);
            }
        return file_name;
    }
}