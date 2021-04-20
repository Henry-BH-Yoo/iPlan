package ca.on.conec.iplan.adapter;

import ca.on.conec.iplan.entity.Todo;

// This is for when user click one of the DayTodo in RecyclerView,
// allows to click, and open it to edit
public interface OnTodoClickListener {

    void onTodoClick(Todo todo);

    void onTodoRadioBtnClick(Todo todo);

    void onTodoIsDoneChkClick(Todo todo);
}
