package xuanniao.transmission.trclient;

import android.app.Application;
import android.util.Log;
import android.view.View;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connect extends Application {
    // Socket变量
    public static Socket socket = null;

    // 输入流对象
    InputStream inputStream;

    // 输出流对象
    OutputStream outputStream;

    // 输入流读取器对象
    InputStreamReader inputStreamReader ;

    public static String check;
    private static final String TAG = "TAG";
    private static final String HOST = "192.168.1.16";
    private static final int PORT = 9999;
    private static final ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private PrintWriter printWriter=null;
    private final List<Socket> mList = new ArrayList<Socket>();



    public void connect() {
        mExecutorService.execute(new connectService());
    }

    public void disconnect(View view) {
        mExecutorService.execute(new disconnectService());
    }

    public void send(String send_text) {
        mExecutorService.execute(new sendService(send_text));
    }

    private class connectService implements Runnable {
        @Override
        public void run() {
            //可以考虑在此处添加一个while循环，结合下面的catch语句，实现Socket对象获取失败后的超时重连，直到成功建立Socket连接
            try {
                // 建立新的socket连接
                Log.i("连接","开始尝试");
                socket = new Socket(HOST, PORT);
                // 设定连接超时时间
                socket.setSoTimeout(10000);
                // 判定是否连接成功
                check = String.valueOf(socket.isConnected());
                if (check.equals("true")) {
                    Log.i("连接","成功");
                }
                setSocket(socket);
                Log.i("socket.connectService", String.valueOf(socket));
            } catch (Exception e) {
                Log.e(TAG, ("connectService:" + e.getMessage()));   //如果Socket对象获取失败，即连接建立失败，会走到这段逻辑
            }
        }
    }

    private class disconnectService implements Runnable {
        @Override
        public void run() {
            try {
                socket = getSocket();
                Log.i("socket.disconnect", String.valueOf(socket));
                // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                outputStream.close();
                // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
//            bufferedReader.close();
                // 最终关闭整个Socket连接
                if (socket != null) {
                    socket.close();
                }
                // 判断客户端和服务器是否已经断开连接
//                Log.d("断开连接", String.valueOf(socket.isConnected()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class sendService implements Runnable {
        private String msg;
        sendService(String msg) {
            this.msg = msg;
        }
        @Override
        public void run() {
//            printWriter.println(this.msg);
            try {
                socket = getSocket();
                Log.i("socket.S", String.valueOf(socket));
                // 步骤1：从Socket 获得输出流对象OutputStream
                // 该对象作用：发送数据
                outputStream = socket.getOutputStream();
                // 步骤2：写入需要发送的数据到输出流对象中
                outputStream.write((this.msg+"\n").getBytes("utf-8"));
                // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                // 步骤3：发送数据到服务端
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String receiveMsg() {
        String receiveMsg = null;
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            Log.d("接收消息函数标记1", String.valueOf(bufferedReader));
            byte[] buffer = new byte[1024 * 4];
            int temp = 0;
            // 从InputStream当中读取客户端所发送的数据
//            while ((receiveMsg = bufferedReader.readLine()) != null) {
//                Log.d("接收消息函数标记2", receiveMsg);
//            }
            if ((temp = inputStream.read(buffer)) != -1) {
                receiveMsg = (new String(buffer, 0, temp));
                Log.d("接收消息函数标记2", receiveMsg);
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

    public Socket getSocket() {
        Log.i("socket.G", String.valueOf(socket));
        return socket;
    }

    public void setSocket(Socket socket) {
        Log.i("socket.C", String.valueOf(socket));
        Connect.socket = socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}