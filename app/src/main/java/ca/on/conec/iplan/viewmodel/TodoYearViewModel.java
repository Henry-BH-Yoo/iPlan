package ca.on.conec.iplan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.database.iPlanYearRepository;
import ca.on.conec.iplan.entity.TodoYear;

public class TodoYearViewModel extends AndroidViewModel {

    public static iPlanYearRepository repository;

    public final LiveData<List<TodoYear>> allTodos;


    public TodoYearViewModel(@NonNull Application application) {
        super(application);
        repository = new iPlanYearRepository(application);
        allTodos = repository.getAllTodos();
    }

    public LiveData<List<TodoYear>> getAllTodos() {
        return allTodos;
    }

    public static void insert(TodoYear todoYear) {
        repository.insert(todoYear);
    }

    public LiveData<TodoYear> get(long id) {
        return repository.get(id);
    }

    public static void update(TodoYear todoYear) {
        repository.update(todoYear);
    }

    public static void delete(TodoYear todoYear) {
        repository.delete(todoYear);
    }
}
