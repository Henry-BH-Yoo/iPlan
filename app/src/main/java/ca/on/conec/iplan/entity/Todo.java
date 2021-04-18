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
    public long todoId; // Day, Year

    public String name; // Day, Year

    @ColumnInfo(name = "is_done")
    public boolean isDone; // Day, Year

//    public int progress; // Year
//
//    public String note; // Year

    @ColumnInfo(name = "has_alarm")
    public boolean hasAlarm; // Day

    @ColumnInfo(name = "start_time")
    public LocalTime startTime; // Day

    @ColumnInfo(name = "end_time")
    public LocalTime endTime; // Day

    // it is for day_todo
    public Todo(String name, boolean isDone, boolean hasAlarm, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.isDone = isDone;
        this.hasAlarm = hasAlarm;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
