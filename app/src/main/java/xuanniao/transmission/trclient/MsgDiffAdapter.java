//package xuanniao.transmission.trclient;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.AsyncListDiffer;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class MsgDiffAdapter extends RecyclerView.Adapter {
//    private Context mContext;
//    private AsyncListDiffer<Msg> mTextDiffl;
//    private DiffUtil.ItemCallback<Msg> diffCallback = new MyItemCallBack();
//
//    public MsgDiffAdapter(Context mContext) {
//        this.mContext = mContext;
//        mTextDiffl = new AsyncListDiffer<>(this, diffCallback);
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
//        return new PayLoadViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        MsgAdapter.PayLoadViewHolder myViewHolder = (MsgAdapter.PayLoadViewHolder) holder;
//        Msg textModel = getItem(position);
//        myViewHolder.tv.setText(textModel.getTextTitle() + "." + textModel.getTextContent());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mTextDiffl.getCurrentList().size();
//    }
//    public void submitList(List<Msg> data) {
//        mTextDiffl.submitList(data);
//    }
//
//    public Msg getItem(int position) {
//        return mTextDiffl.getCurrentList().get(position);
//    }
//
//    class PayLoadViewHolder extends RecyclerView.ViewHolder {
//        TextView tv;
//
//        PayLoadViewHolder(View itemView) {
//            super(itemView);
//            tv = (TextView) itemView.findViewById(R.id.item_tv);
//        }
//    }
//
//    public class MyItemCallBack extends DiffUtil.ItemCallback<Msg> {
//        @Override
//        public boolean areItemsTheSame(@NonNull Msg oldItem, @NonNull Msg newItem) {
//            return TextUtils.equals(oldItem.getTextTitle(), newItem.getTextTitle());
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Msg oldItem, @NonNull Msg newItem) {
//            return TextUtils.equals(oldItem.getTextContent(), newItem.getTextContent());
//        }
//    }
//
//    public class DiffUtil.androidx.recyclerview.widget.DiffUtil.Callback
//}
