/**
 * FileName : MPlanListAdapter
 * Purpose : For recycler view.
 * Revision History :
 *          2021 04 23  Henry   Create Monthly plan recycler adapter
 *          2021 04 24  Henry   Modify event listener
 */

package ca.on.conec.iplan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;
import ca.on.conec.iplan.viewmodel.MonthlyPlanViewModel;

public class MPlanListAdapter extends RecyclerView.Adapter<MPlanListAdapter.ViewHolder> implements ItemTouchHelperListener {
    private List<MonthlyPlan> mPlanList = new ArrayList<MonthlyPlan>();
    private Context context;
    private OnItemClickListener bListener;

    public MPlanListAdapter(Context context) {
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
        holder.onBind(mPlanList.get(position) , position);
    }


    @Override
    public int getItemCount() {
        return mPlanList.size();
    }

    public void setBucketList(List<MonthlyPlan> mPlanList) {
        this.mPlanList = mPlanList;
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        MonthlyPlan mPlan = mPlanList.get(position);
        MonthlyPlanViewModel.delete(mPlan);

        mPlanList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onComplete(int from_position, int to_position) { }


    /**
     * View Holder Constructor
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            try {
                    title = (TextView) itemView.findViewById(R.id.list_title);
                    deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                bListener.onItemClick(v, mPlanList.get(pos).mPlanDate, mPlanList.get(pos).mPlanId);
                            }
                        }
                    });

                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                bListener.onItemClick(v, mPlanList.get(pos).mPlanDate, mPlanList.get(pos).mPlanId);
                            }
                        }
                    });

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                bListener.onDeleteBtnClick(v, mPlanList.get(pos));
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            public void onBind (MonthlyPlan mPlan,int position){

                title.setText(mPlan.getMPlanTitle());
            }
    }


    /**
     * Item List Click Event Listener interface
     */
    public interface OnItemClickListener {
        void onItemClick(View v, String selectedDate , int mPlanId);
        void onDeleteBtnClick(View v , MonthlyPlan mPlan);
    }

    /**
     * Setter Item Click Listener
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.bListener = listener;
    }

}


