package xuanniao.transmission.trclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class Connect extends JobIntentService {
    public static Socket socket = null;
    public static String check;
    static int order = 1;
    private static final int JOB_ID = 0;
    private static final String Tag = "Connect";
    private SharedPreferences.Editor SPeditor;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Connect.class, JOB_ID, work);
        order = work.getIntExtra("connect_order",1);
        Log.i(Tag, "加入队列");
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i(Tag, "开启");
        //获取SharedPreferences对象
        SharedPreferences SP = getSharedPreferences("config", Context.MODE_PRIVATE);
        SPeditor = SP.edit();
        String HOST = SP.getString("ipv4", "192.168.0.0");
        int PORT = SP.getInt("port", 9999);
        if (order == 1) {
            order_connect(HOST, PORT);
        } else {
            order_disconnect();
        }
    }

    private void order_connect(String HOST, int PORT) {
        Log.i(Tag, "服务已开启");
        SPeditor.putInt("connect_on", 1);
        SPeditor.apply();
        try {
            // 建立新的socket连接
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
            // 设定连接超时时间
            socket.connect(socketAddress, 7000);
            // 判定是否连接成功
            check = String.valueOf(socket.isConnected());
            if (check.equals("true")) {
                setSocket(socket);
                Message msg_c = new Message();
                // 连接失败代号为0，成功为1
                msg_c.what = 1;
                MainActivity.handler_connect.sendMessage(msg_c);
                SPeditor.putInt("connect_status", 1);
                SPeditor.apply();
                Log.i(Tag,"连接成功"+socket);
            } else {
                Log.i(Tag,"尝试连接,失败");
            }
        } catch (Exception e) {
            Log.e(Tag, ("connectService:" + e.getMessage()));
            // 没连接上的处理方法
            Message message = new Message();
            // 连接失败代号为0，成功为1
            message.what = 0;
            message.obj = e.getMessage();
            MainActivity.handler_connect.sendMessage(message);
            SPeditor.putInt("connect_on", 0);
            SPeditor.apply();
        }
    }

    private void order_disconnect() {
        try {
            socket = getSocket();
            Log.i("socket.disconnect", String.valueOf(socket));
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write("@EndMark@".getBytes(StandardCharsets.UTF_8));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                System.out.print(new String(buf, 0, len, StandardCharsets.UTF_8));
                out.write(buf, 0, len);
            }
            out.write("\n end \n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            socket.shutdownInput();
            socket.shutdownOutput();
            SPeditor.putInt("connect_status", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 判断客户端和服务器是否已经断开连接
        Log.d("断开连接", String.valueOf(socket.isConnected()));
    }

    public Socket getSocket() {
        Log.i("socket.G", String.valueOf(socket));
        return socket;
    }

    public void setSocket(Socket socket) {
        Log.i("socket.C", String.valueOf(socket));
        Connect.socket = socket;
    }
}