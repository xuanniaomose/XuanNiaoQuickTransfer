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

public class DisConnect extends JobIntentService {
    public static Socket socket = null;
    public static String check;
    static int order = 1;
    private static final int JOB_ID = 0;
    private static final String Tag = "Connect";
    private SharedPreferences.Editor SPeditor;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Connect.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i(Tag, "关闭");
        disconnect();
    }

    private void disconnect() {
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

}