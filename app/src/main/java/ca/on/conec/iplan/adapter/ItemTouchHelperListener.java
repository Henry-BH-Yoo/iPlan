/**
 * FileName : ItemTouchHelperListener.java
 * Purpose : For recycler view.
 * Revision History :
 *          2021 04 19  Henry   Create Touch Helper interface
 */

package ca.on.conec.iplan.adapter;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position , int to_position);
    void onItemSwipe(int position);
//    void onItemSwipe(int position, int direction);
    void onComplete(int from_position , int to_position);
}
