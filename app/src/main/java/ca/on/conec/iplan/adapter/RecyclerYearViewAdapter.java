package ca.on.conec.iplan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.TodoYear;

public class RecyclerYearViewAdapter extends RecyclerView.Adapter<RecyclerYearViewAdapter.ViewHolder> implements ItemTouchHelperListener {

    private final List<TodoYear> todoYearList;

    private final OnTodoYearClickListener todoYearClickListener;

    public RecyclerYearViewAdapter(List<TodoYear> todoYearList, OnTodoYearClickListener onTodoYearClickListener) {
        this.todoYearList = todoYearList;
        this.todoYearClickListener = onTodoYearClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerYearViewAdapter.ViewHolder holder, int position) {

        TodoYear todoYear = todoYearList.get(position);

        holder.yearTodo.setText(todoYear.getName());
    }

    @Override
    public int getItemCount() {
        return todoYearList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public AppCompatTextView yearTodo;
        public AppCompatImageButton imgBtnDelete;

        OnTodoYearClickListener onTodoYearClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            yearTodo = itemView.findViewById(R.id.txtYearRow);
            imgBtnDelete = itemView.findViewById(R.id.imgBtnDeleteYear);

            this.onTodoYearClickListener = todoYearClickListener;

            itemView.setOnClickListener(this);
            imgBtnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TodoYear currentTodoYear = todoYearList.get(getAdapterPosition());

            int id = v.getId();

            if (id == R.id.year_row_layout) {
                // OnTodoYearClickListener Interface can delegate this information to whoever implements it
                onTodoYearClickListener.onTodoYearClick(currentTodoYear);
            } else if (id == R.id.imgBtnDeleteYear) {
                onTodoYearClickListener.onTodoDeleteImgClick(currentTodoYear);
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
