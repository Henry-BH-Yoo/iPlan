/**
 * FileName : RecyclerViewAdapter.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.20 Sean    Add onTodoIsDoneChkClick
 *      2021.04.24 Sean    Add delete function with delete image button
 */
package ca.on.conec.iplan.adapter;

import android.app.Notification;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.fragment.DailyFragment;
import ca.on.conec.iplan.viewmodel.TodoViewModel;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ItemTouchHelperListener {

    private final List<Todo> todoList;
    private final OnTodoClickListener todoClickListener;


    /**
     * Purpose: Add OnTodoClickListener to RecyclerViewAdapter to allow each row in RecyclerView is clickable
     */
    public RecyclerViewAdapter(List<Todo> todoList, OnTodoClickListener onTodoClickListener) {
        this.todoList = todoList;
        this.todoClickListener = onTodoClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Purpose: Show view with condition
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.dayTodo.setText(todo.getName());
        holder.dayStartTime.setText(LocalTimeConverter.toTimeString(todo.getStartTime()));

        if (todo.hasAlarm) {
            holder.imgBtnAlarm.setVisibility(View.VISIBLE);
        }

        if (todo.isDone) {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public AppCompatTextView dayTodo;
        public AppCompatTextView dayStartTime;
        public AppCompatCheckBox checkBox;
        public AppCompatImageButton imgBtnAlarm, imgBtnDelete;

        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            try {
                checkBox = itemView.findViewById(R.id.chkIsDone);

                dayTodo = itemView.findViewById(R.id.txtDayRow);
                dayStartTime = itemView.findViewById(R.id.txtDayStartTime);
                imgBtnAlarm = itemView.findViewById(R.id.imgBtnHasAlarm);
                imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);

                this.onTodoClickListener = todoClickListener;

                itemView.setOnClickListener(this);
                checkBox.setOnClickListener(this);
                imgBtnDelete.setOnClickListener(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            try {
                Todo currentTodo = todoList.get(getAdapterPosition());

                int id = v.getId();

                if (id == R.id.day_row_layout) {
                    // OnTodoClickListener Interface can delegate this information to whoever implements it
                    onTodoClickListener.onTodoClick(currentTodo);
                } else if (id == R.id.chkIsDone) {
                    onTodoClickListener.onTodoIsDoneChkClick(currentTodo);
                } else if (id == R.id.imgBtnDelete) {
                    onTodoClickListener.onTodoDeleteImgClick(currentTodo);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
    }

    @Override
    public void onComplete(int from_position, int to_position) {
    }

}
