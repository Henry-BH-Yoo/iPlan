/**
 * FileName : OnTodoClickListener.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.20 Sean    Add onTodoIsDoneChkClick
 *      2021.04.24 Sean    Delete onTodoRadioBtnClick
 *      2021.04.24 Sean    Add onTodoDeleteImgClick
 */
package ca.on.conec.iplan.adapter;

import ca.on.conec.iplan.entity.Todo;


/**
 * Purpose: This is for when user click one of the DayTodo in RecyclerView,
 *          allows to click, and open it to edit
 */
public interface OnTodoClickListener {

    void onTodoClick(Todo todo);

    void onTodoIsDoneChkClick(Todo todo);

    void onTodoDeleteImgClick(Todo todo);
}
