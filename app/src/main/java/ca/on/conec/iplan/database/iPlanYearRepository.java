package ca.on.conec.iplan.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.dao.TodoYearDao;
import ca.on.conec.iplan.entity.TodoYear;

public class iPlanYearRepository {

    private final TodoYearDao todoYearDao;

    private final LiveData<List<TodoYear>> allTodos;

    public iPlanYearRepository (Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        todoYearDao = database.todoYearDao();
        allTodos = todoYearDao.getTodos();
    }

    public LiveData<List<TodoYear>> getAllTodos() {
        return allTodos;
    }

    public LiveData<TodoYear> get(long id) {
        return todoYearDao.get(id);
    }

    public void insert(TodoYear todoYear) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoYearDao.insertTodo(todoYear));
    }

    public void update(TodoYear todoYear) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoYearDao.update(todoYear));
    }

    public void delete(TodoYear todoYear) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoYearDao.delete(todoYear));
    }
}
