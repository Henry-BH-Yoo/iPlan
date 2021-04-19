package ca.on.conec.iplan.adapter;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position , int to_position);
    void onItemSwipe(int position);
    void onComplete(int from_position , int to_position);
}
