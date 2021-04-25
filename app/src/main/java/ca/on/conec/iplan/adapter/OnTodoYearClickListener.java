/**
 * FileName : OnTodoYearClickListener.java
 * Purpose
 * Revision History :
 *      2021.04.22 Sean    Create
 *      2021.04.24 Sean    Add onTodoDeleteImgClick
 */
package ca.on.conec.iplan.adapter;

import ca.on.conec.iplan.entity.TodoYear;


/**
 * Purpose: This is for when user click one of the TodoYear in RecyclerView,
 *          allows to click, and open it to edit
 */
public interface OnTodoYearClickListener {

    void onTodoYearClick(TodoYear todoYear);

    void onTodoDeleteImgClick(TodoYear todoYear);
}
