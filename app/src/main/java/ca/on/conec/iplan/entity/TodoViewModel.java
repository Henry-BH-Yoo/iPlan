package ca.on.conec.iplan.entity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.database.AppDatabase;
import ca.on.conec.iplan.database.iPlanRepository;

// Inherits from AndroidViewModel, different from SharedViewModel
public class TodoViewModel extends AndroidViewModel {

    public static iPlanRepository repository;
    public final LiveData<List<Todo>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new iPlanRepository(application);
        allTodos = repository.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public static void insert(Todo todo) {
        repository.insert(todo);
    }

    public LiveData<Todo> get(long id) {
        return repository.get(id);
    }

    public static void update(Todo todo) {
        repository.update(todo);
    }

    public static void delete(Todo todo) {
        repository.delete(todo);
    }
}
