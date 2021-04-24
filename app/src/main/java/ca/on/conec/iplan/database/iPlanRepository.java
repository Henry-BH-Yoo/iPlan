package ca.on.conec.iplan.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalTime;
import java.util.List;

import ca.on.conec.iplan.dao.TodoDao;
import ca.on.conec.iplan.entity.Todo;

public class iPlanRepository {

    private final TodoDao todoDao;

    private final LiveData<List<Todo>> allTodos;


    public iPlanRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        todoDao = database.todoDao();
        allTodos = todoDao.getTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public LiveData<Todo> get(long id) {
        return todoDao.get(id);
    }


    public LiveData<List<Todo>> getTodosWithAlarm(String startTime, String hrLaterTime, int days) {
        return todoDao.getTodosWithAlarm(startTime, hrLaterTime, days);
    }
//    public List<Todo> getTodosWithAlarm(LocalTime startTime, LocalTime hrLaterTime, int days) {
//        return todoDao.getTodosWithAlarm(startTime, hrLaterTime, days);
//    }




    public void insert(Todo todo) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoDao.insertTodo(todo));
    }

    public void update(Todo todo) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoDao.update(todo));
    }

    public void delete(Todo todo) {
        AppDatabase.databaseWriterExecutor.execute( () -> todoDao.delete(todo));
    }
}
