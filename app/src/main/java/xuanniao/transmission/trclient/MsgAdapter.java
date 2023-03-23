package xuanniao.transmission.trclient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "MsgAdapter";
    public TextView leftMsg;
    public TextView rightMsg;
    public RecyclerView recyclerView_chat;
    static List<Msg> msgList = MainActivity.msgList;

    public void setMsgList(List<Msg> msgList) {
        MsgAdapter.msgList = MainActivity.msgList;
    }

    // 载入发送（右）聊天列表布局控件
    class RightViewHolder extends RecyclerView.ViewHolder{
        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            rightMsg = itemView.findViewById(R.id.right_text);
        }
    }
    // 载入接受（左）聊天列表布局控件
    class LeftViewHolder extends RecyclerView.ViewHolder{
        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMsg = itemView.findViewById(R.id.left_text);
        }
    }

    // 获取消息类型(左或者右),返回到onCreateViewHolder()方法的viewType参数里面
    @Override
    public int getItemViewType(int position) {
        return msgList.get(position).getType();
    }

    @NonNull
    @Override
    //根据viewType消息类型的不同,构建不同的消息布局(左&右)
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View leftView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.msg_left_layout,parent,false);
            return new LeftViewHolder(leftView);
        } else {
            View rightView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.msg_right_layout,parent,false);
            return new RightViewHolder(rightView);
        }
    }

    @Override
    //对聊天控件的消息文本进行赋值
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (msgList != null && msgList.size() > 0) {
            Msg msg = msgList.get(position);
            String time_chat = msg.getTime();
            Log.i(TAG,time_chat);
            int i = msg.getType();
            try {
                if (i == 0) {
                    Log.i("onBind消息内容-接收", msgList.get(position).getContent());
                    List<String> content_List = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
                    List<Integer> type_List = msgList.stream().map(Msg::getType).collect(Collectors.toList());
                    Log.i("onBind消息列表", content_List.toString());
                    Log.i("onBind类型列表", type_List.toString());
//                    leftMsg = recyclerView_chat.findViewWithTag(time_chat);
                    leftMsg.setText(msgList.get(position).getContent());
                } else {
                    Log.i("onBind消息内容-发送", msgList.get(position).getContent());
                    List<String> content_List = msgList.stream().map(Msg::getContent).collect(Collectors.toList());
                    List<Integer> type_List = msgList.stream().map(Msg::getType).collect(Collectors.toList());
                    Log.i("onBind消息列表", content_List.toString());
                    Log.i("onBind类型列表", type_List.toString());
//                    rightMsg = recyclerView_chat.findViewWithTag(time_chat);
                    rightMsg.setText(msgList.get(position).getContent());
                }
            } catch (IllegalArgumentException e) {
                Log.i("onBind错误", "错误内容格式化");
            }
        }
    }

    @Override
    // 查询消息列表的项数
    public int getItemCount() {
        return msgList.size();
    }
}