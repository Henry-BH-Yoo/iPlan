package ca.on.conec.iplan.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.Todo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<Todo> todoList;

    private final OnTodoClickListener todoClickListener;

    // Add OnTodoClickListener to RecyclerViewAdapter to allow each row in RecyclerView is clickable
    public RecyclerViewAdapter(List<Todo> todoList, OnTodoClickListener onTodoClickListener) {
        this.todoList = todoList;
        this.todoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_row, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.dayTodo.setText(todo.getName());
        holder.dayStartTime.setText(LocalTimeConverter.toTimeString(todo.getStartTime()));

        if (todo.hasAlarm) {
            holder.imageButton.setVisibility(View.VISIBLE);
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

        public AppCompatRadioButton radioButton;
        public AppCompatTextView dayTodo;
        public AppCompatTextView dayStartTime;
        public AppCompatCheckBox checkBox;
        public AppCompatImageButton imageButton;

        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.rdoDayRow);
            checkBox = itemView.findViewById(R.id.chkIsDone);

            dayTodo = itemView.findViewById(R.id.txtDayRow);
            dayStartTime = itemView.findViewById(R.id.txtDayStartTime);
            imageButton = itemView.findViewById(R.id.imgBtnHasAlarm);

            this.onTodoClickListener = todoClickListener;

            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Todo currentTodo = todoList.get(getAdapterPosition());

            int id = v.getId();

            if (id == R.id.day_row_layout) {
                // OnTodoClickListener Interface can delegate this information to whoever implements it
                onTodoClickListener.onTodoClick(currentTodo);
            } else if (id == R.id.rdoDayRow) {
                onTodoClickListener.onTodoRadioBtnClick(currentTodo);
            } else if (id == R.id.chkIsDone) {
                onTodoClickListener.onTodoIsDoneChkClick(currentTodo);
            }
        }
    }
}
