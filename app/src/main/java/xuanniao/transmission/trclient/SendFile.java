package xuanniao.transmission.trclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SendFile {
    public static Handler handler_recv_msg;
    public static Socket socket = Connect.socket;
    private FileInputStream fis;
    private static DataOutputStream dos;
    private static File path;
    private static byte[] bytes;
    // 服务功能的设置
    protected static void send_file(File path) {
        try {
            Connect Connect = new Connect();
            socket = Connect.getSocket();
//          MainActivity.textview_sendstate.setText("发送中");
            dos = new DataOutputStream(socket.getOutputStream());
//            bytes = new byte[(int)path.length()];
            dos.writeUTF("@FilMark@"+path.getName()+"@FName@"+(int)path.length()+"@FLen@");
            Log.i("SendFile","文件信息已发送");
            dos.flush();
            send(path);
//            handler_recv_msg = new Handler(Looper.myLooper()) {
//                @Override
//                public void handleMessage(Message message) {
//                    super.handleMessage(message);
//                    if (message.obj == "准备接收") {
//                        Log.i("SendFile","收到电脑端反馈");
//
//                    }
//                }
//            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getToSendData(byte[] data,int off,int len) throws IOException{
        System.out.println("off:"+off+",len:"+len);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Utils.intToByteArray(len));
        outputStream.write(data,off,len);
        System.out.println(Arrays.toString(outputStream.toByteArray()));
        return outputStream.toByteArray();
    }

    //MyService用ServiceHandler接收并处理来自于客户端的消息
    private static void send(File path) {
        try{
//            InputStream in = socket.getInputStream();
//            //计算上传百分百
//            byte[] bufIn = new byte[1024];
//            int num = in.read(bufIn);
    //            int parseInt = Integer.parseInt(new String(bufIn,0,num));
    //            if (parseInt == 0) {
    //                System.out.println("已上传完成 ["+parseInt+"%]");
    //            }else{
    //                System.out.println("已上传完成 ["+Utils.division(parseInt, (int)path.length())+"]");
    //            }

                Log.i("SendFile","开始发送");
            OutputStream ops = socket.getOutputStream();
            // 获取文件的字节流
            FileInputStream fis = new FileInputStream(path);
            //2.往输出流里面投放数据
            int file_len = (int)path.length();
            int len = -1;
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
//            while ((len = fis.read(buffer, 0, 1024)) != -1) {
                n ++;
                ops.write(buffer, 0, len);
                if (file_len>0) {
                    if (n>file_len) {
                        fis.close();
                        break;
                    }
                }

//                int maxint = 512;
//                int size = new Random().nextInt(maxint);
//                while (size<=0) {
//                    size = new Random().nextInt(maxint);
//                }
//                buffer = new byte[size];
            }
                Log.i("SendFile","发送完成");

            //获取上传信息
//            num = in.read(bufIn);
//            if (!String.valueOf(num).equals("")) {
//                System.out.println(new String(bufIn, 0, num));
//            }
            //关闭资源
//                dos.close();
//                in.close();
//                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}