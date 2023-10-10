package xuanniao.transmission.trclient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.PayLoadViewHolder> {
    private final String TAG = "MsgAdapter";
    static List<Msg> msgList = MainActivity.msgList;
    public LinearLayout tr_file;
    int count = 0;

    public void setMsgList(List<Msg> msgList) {
        MsgAdapter.msgList = MainActivity.msgList;
    }

    static class PayLoadViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rightLayout, leftLayout, tr_file;
        TextView rightMsg, leftMsg, tv_fileName, tv_trDone, tv_trTotal, tv_trTotalB;
        ImageView iv_FileTypeIcon;
        xuanniao.transmission.trclient.LineProgressBar pb_trProgress;

        public PayLoadViewHolder(View itemView) {
            super(itemView);
            rightLayout = itemView.findViewById(R.id.right_layout);
            leftLayout = itemView.findViewById(R.id.left_layout);

            rightMsg = itemView.findViewById(R.id.right_text);
            leftMsg = itemView.findViewById(R.id.left_text);

            tv_fileName = itemView.findViewById(R.id.fileName);
            tv_trDone = itemView.findViewById(R.id.trDone);
            tv_trTotal = itemView.findViewById(R.id.trTotal);
            tv_trTotalB = itemView.findViewById(R.id.trTotalB);
            iv_FileTypeIcon = itemView.findViewById(R.id.FileTypeIcon);
            pb_trProgress = itemView.findViewById(R.id.trProgress);
        }
    }

    // 获取消息类型(左或者右),返回到onCreateViewHolder()方法的viewType参数里面
    @Override
    public int getItemViewType(int position) {
        return msgList.get(position).getRSType() * 10 + msgList.get(position).getFileType();
    }

    @NonNull
    @Override
    //根据viewType消息类型的不同,构建不同的消息布局(左&右)
    public PayLoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("创建holder","onCreate方法被触发"+viewType);
        count++;
        ViewGroup chartView;
        LinearLayout bubleLayout;
        if (viewType / 10 == 0) {
            chartView = (ViewGroup) LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.msg_left_layout, parent, false);
            bubleLayout = chartView.findViewById(R.id.left_layout);
        } else {
            chartView = (ViewGroup) LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.msg_right_layout,parent,false);
            bubleLayout = chartView.findViewById(R.id.right_layout);
        }
        if (viewType % 10 != 0) {
            LayoutInflater i = LayoutInflater.from(bubleLayout.getContext());
            tr_file = (LinearLayout) i.inflate(R.layout.tr_file, null);
            bubleLayout.removeAllViews();
            bubleLayout.addView(tr_file);
        }
        return new PayLoadViewHolder(chartView);
    }

    @Override
    //对聊天控件的消息文本进行赋值
    public void onBindViewHolder(@NonNull PayLoadViewHolder holder, int position, @NonNull List<Object> payloads) {
        Log.d("PayloadAdapter", "onBindViewHolder payload: " + payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            int done = (int) payloads.get(0);
            Log.d(TAG+"进度条", done+" done,"+ position);

            TextView tv_trDone = holder.tv_trDone;
            TextView tv_trTotalB = holder.tv_trTotalB;
            LineProgressBar pb_trProgress = holder.pb_trProgress;

            // 依赖于前端的文件总大小获取方法需要修改
            String total = (String) tv_trTotalB.getText();
//            int t = Integer.parseInt(total.substring(1, total.length() - 1));
            int t = Integer.parseInt(total)/1024;
            Log.d(TAG+"done/t",done+" / "+t);
            if (done == t) {
                tv_trDone.setText("传输完毕");
            } else {
                tv_trDone.setText(fileSizeChange(done*1024));
            }
            pb_trProgress.setParameter(done, t);
            pb_trProgress.invalidate();
        }
    }

    @Override
    //对聊天控件的消息文本进行赋值
    public void onBindViewHolder(@NonNull PayLoadViewHolder holder, int position) {
        setMsg(holder, position);
    }

    public void setMsg(@NonNull PayLoadViewHolder holder, int position) {
        if (msgList != null && msgList.size() > 0) {
            Msg msg = msgList.get(position);
            String time_chat = msg.getTime();
            Log.i(TAG+"msg位置","msg位置"+position);
            int i = msg.getRSType();
            try {
                if (i == 0) {
                    Log.i("onBind消息内容-接收", msg.getContent());
//                    List<String> content_List = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
//                    List<Integer> type_List = msgList.stream().map(Msg::getRSType).collect(Collectors.toList());
//                    Log.i("onBind消息列表", content_List.toString());
//                    Log.i("onBind类型列表", type_List.toString());
//                    leftMsg = recyclerView_chat.findViewWithTag(time_chat);
                    if (msg.getFileType() == 0) {
                        holder.leftMsg.setText(msgList.get(position).getContent());
                    } else if (msg.getFileType() == 1) {
                        addTrFileView(position);
                    }
                } else {
                    Log.i("onBind消息内容-发送", msg.getContent());
//                    List<String> content_List = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
//                    List<Integer> type_List = msgList.stream().map(Msg::getRSType).collect(Collectors.toList());
//                    Log.i("onBind消息列表", content_List.toString());
//                    Log.i("onBind类型列表", type_List.toString());
//                    rightMsg = recyclerView_chat.findViewWithTag(time_chat);
                    Log.d(TAG,"是否是文件" + msg.getFileType());
                    if (msg.getFileType() == 0) {
                        holder.rightMsg.setText(msgList.get(position).getContent());
                    } else if (msg.getFileType() == 1) {
                        addTrFileView(position);
                    }
                }
            } catch (IllegalArgumentException e) {
                Log.i("onBind错误", "错误内容格式化");
            }
        }
    }

    private void addTrFileView(int position) {
        Log.d(TAG+"文件大小", fileSizeChange(msgList.get(position).getFileSize())+"");
        TextView tv_fileName = tr_file.findViewById(R.id.fileName);
        TextView tv_trDone = tr_file.findViewById(R.id.trDone);
        TextView tv_trTotalB = tr_file.findViewById(R.id.trTotalB);
        TextView tv_trTotal = tr_file.findViewById(R.id.trTotal);
        LineProgressBar pb_trProgress = tr_file.findViewById(R.id.trProgress);

        tv_fileName.setText(msgList.get(position).getContent());
        tv_trDone.setText("0");
        int totalB = msgList.get(position).getFileSize();
        String total = fileSizeChange(totalB);
        tv_trTotalB.setText(String.valueOf(totalB));
        tv_trTotal.setText("/" + total);
        total = total.substring(0, total.length() - 1);
        Log.d(TAG+" 文件大小显示",total);
        pb_trProgress.setParameter(0, Integer.parseInt(total));
    }

    @Override
    // 查询消息列表的项数
    public int getItemCount() {
        return msgList.size();
    }

    public String fileSizeChange(int fileSize) {
        String result;
        int k = fileSize / 1024;
        if (k == 0){
            result = fileSize + "B";
        } else {
            int m = k / 1024;
            if (m == 0){
                result = k + "K";
            } else {
                int g = m / 1024;
                if (g == 0){
                    result = m + "M";
                } else {
                    result = g + "G";
                }
            }
        }
        return result;
    }

    public String changeSize(String size) {
        if (size.length() < 3) {
            int r = Integer.parseInt(size.substring(0,size.length()-2)) * 1024;
            String sign = String.valueOf(size.charAt(size.length()-2));
            switch (sign) {
                case "B" :
                    break;
                case "K" :
                    size = r + "B";
                    break;
                case "M" :
                    size = r + "K";
                    break;
                case "G" :
                    size = r + "M";
                    break;
            }
        }
        return size;
    }
}