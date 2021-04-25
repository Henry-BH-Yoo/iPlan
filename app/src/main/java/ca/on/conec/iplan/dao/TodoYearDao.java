/**
 * FileName : TodoYearDao.java
 * Purpose
 * Revision History :
 *      2021.04.22 Sean    Create
 */
package ca.on.conec.iplan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.on.conec.iplan.entity.TodoYear;

@Dao
public interface TodoYearDao {

    @Insert
    void insertTodo(TodoYear todoYear);

    @Query("DELETE FROM todoYear_table")
    void deleteAll();

    @Query("SELECT * FROM todoYear_table ORDER BY todoYear_table.name")
    LiveData<List<TodoYear>> getTodos();

    @Query("SELECT * FROM todoYear_table WHERE todoYear_table.todo_id == :id")
    LiveData<TodoYear> get(long id);

    @Update
    void update(TodoYear todoYear);

    @Delete
    void delete(TodoYear todoYear);

}
