package ca.on.conec.iplan.adapter;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;

    private int mFrom = -1;
    private int mTo = -1;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) { this.listener = listener; }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags,swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        if(mFrom == -1) {
            mFrom =  fromPosition;
        }
        mTo = toPosition;

        return listener.onItemMove(mFrom,mTo);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if(mFrom != -1 && mTo != -1 && mFrom != mTo) {
            //reallyMoved(dragFrom, dragTo);
            Log.d("DEBUG" , "Completed Drag and deop");
            listener.onComplete(mFrom , mTo);
        }

        mFrom = mTo = -1;

    }

}
