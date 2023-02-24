package xuanniao.transmission.trclient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int viewType;
    public TextView leftMsg;
    public TextView rightMsg;
    static List<Msg> msgList = new ArrayList<>();

    public void setMsgList(List<Msg> msgList) {
        this.msgList = msgList;
        Log.i("消息列表", msgList.toString());
        //一定要记得加，否则视图不会更新！！！
        notifyDataSetChanged();
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

    @Override
    // 获取消息类型(左或者右),返回到onCreateViewHolder()方法的viewType参数里面
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        Msg msg = msgList.get(position);
        // 根据当前数据源的类型（发送/接受）
        viewType = msg.getType();
        return viewType;
    }

    @Override
    //根据viewType消息类型的不同,构建不同的消息布局(左&右)
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Msg.TYPE_RECEIVED) {
            View leftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_left_layout,parent,false);
            // 返回控件和布局
            return new LeftViewHolder(leftView);
        } else {
            View rightView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_right_layout,parent,false);
            return new RightViewHolder(rightView);
        }
    }

    @Override
    //对聊天控件的消息文本进行赋值
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        int i = msg.getType();
        Log.i("类型", String.valueOf(i));
        try {
            if (i == 0) {
                leftMsg.setText(msgList.get(position).getContent());
            } else {
                rightMsg.setText(msgList.get(position).getContent());
            }
        }catch(IllegalArgumentException e){
            Log.i("错误","错误内容格式化");
        }
    }

    @Override
    // 查询消息列表的项数
    public int getItemCount() {
        return msgList.size();
    }
}