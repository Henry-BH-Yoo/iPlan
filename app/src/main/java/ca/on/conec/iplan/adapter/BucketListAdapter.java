/**
 * FileName : PostListAdapter
 * Purpose : For recycler view.
 * Revision History :
 *          Creted by Byounghak Yoo (Henry) 2020.12.10
 */

package ca.on.conec.iplan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> implements ItemTouchHelperListener {
    private List<BucketList> bucketList = new ArrayList<BucketList>();
    private Context context;
    private OnItemClickListener bListener;

    public BucketListAdapter(Context context) {
        this.context = context;
    }

    /**
     * VIew Holder Create Layout
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_success_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(bucketList.get(position) , position);
    }


    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    public void setBucketList(List<BucketList> bucketList) {
        this.bucketList = bucketList;
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {

        BucketList bucket = bucketList.get(from_position);
        bucketList.remove(from_position);
        bucketList.add(to_position,bucket);
        bucket.setOrder(to_position);

        notifyItemMoved(from_position , to_position);

        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        BucketList bucket = bucketList.get(position);
        BucketListViewModel.delete(bucket);

        bucketList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onComplete() {
        for(int i = 0 ; i < bucketList.size() ; i++) {
            BucketList tempBucket = (BucketList)bucketList.get(i);
            tempBucket.setOrder(i);

            BucketListViewModel.update(tempBucket);
        }
    }


    /**
     * View Holder Constructor
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.list_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        bListener.onItemClick(v, bucketList.get(pos).bucketId);
                    }
                }
            });

        }

        public void onBind(BucketList bucket,int position) {

            title.setText(bucket.getBucketTitle());

            if(bucket.getCurrentStatus() == bucket.getGoal()) {
                title.setPaintFlags(title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }


            bucket.setOrder(position);
        }
    }


    /**
     * Item List Click Event Listener interface
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int bucketId);
    }

    /**
     * Setter Item Click Listener
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.bListener = listener;
    }

}


