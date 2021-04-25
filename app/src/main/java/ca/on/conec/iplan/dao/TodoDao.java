/**
 * FileName : TodoDao.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.20 Sean    Add 7 days
 *      2021.04.23 Sean    Add getTodosWithAlarm
 */
package ca.on.conec.iplan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalTime;
import java.util.List;

import ca.on.conec.iplan.entity.Todo;

@Dao
public interface TodoDao {

    @Insert
    void insertTodo(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * FROM todo_table ORDER BY todo_table.start_time")
    LiveData<List<Todo>> getTodos();

    @Query("SELECT * FROM todo_table WHERE todo_table.todo_id == :id")
    LiveData<Todo> get(long id);


    @Query("SELECT * FROM todo_table WHERE todo_table.has_alarm == 1 AND todo_table.days == :days AND (todo_table.start_time BETWEEN :startTime AND :hrLaterTime)")
    LiveData<List<Todo>> getTodosWithAlarm(String startTime, String hrLaterTime, int days);


    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

}
