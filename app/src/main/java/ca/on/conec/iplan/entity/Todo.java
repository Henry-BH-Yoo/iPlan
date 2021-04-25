/**
 * FileName : Todo.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.18 Sean    Add 7 chips for days
 *      2021.04.23 Sean    Delete 7 chips, Add int days
 */
package ca.on.conec.iplan.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

import lombok.Data;

@Entity(tableName = "todo_table")
@Data
public class Todo {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    public long todoId;

    public String name;

    @ColumnInfo(name = "is_done")
    public boolean isDone;

    @ColumnInfo(name = "has_alarm")
    public boolean hasAlarm;

    @ColumnInfo(name = "start_time")
    public LocalTime startTime;

    @ColumnInfo(name = "end_time")
    public LocalTime endTime;

    public int days;

    public Todo(String name, boolean isDone, boolean hasAlarm, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.isDone = isDone;
        this.hasAlarm = hasAlarm;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
