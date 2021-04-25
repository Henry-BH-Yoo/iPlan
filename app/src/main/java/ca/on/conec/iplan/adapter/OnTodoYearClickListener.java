/**
 * FileName : OnTodoYearClickListener.java
 * Purpose
 * Revision History :
 *      2021.04.22 Sean    Create
 *      2021.04.24 Sean    Add onTodoDeleteImgClick
 */
package ca.on.conec.iplan.adapter;

import ca.on.conec.iplan.entity.TodoYear;

public interface OnTodoYearClickListener {

    void onTodoYearClick(TodoYear todoYear);

    void onTodoDeleteImgClick(TodoYear todoYear);
}
