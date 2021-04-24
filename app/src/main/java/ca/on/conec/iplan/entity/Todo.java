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

    @ColumnInfo(name = "has_alarm")
    public boolean hasAlarm; // Day

    @ColumnInfo(name = "start_time")
    public LocalTime startTime; // Day

    @ColumnInfo(name = "end_time")
    public LocalTime endTime; // Day



//    @ColumnInfo(name = "is_mon")
//    public boolean isMon;
//
//    @ColumnInfo(name = "is_tue")
//    public boolean isTue;
//
//    @ColumnInfo(name = "is_wed")
//    public boolean isWed;
//
//    @ColumnInfo(name = "is_thu")
//    public boolean isThu;
//
//    @ColumnInfo(name = "is_fri")
//    public boolean isFri;
//
//    @ColumnInfo(name = "is_sat")
//    public boolean isSat;
//
//    @ColumnInfo(name = "is_sun")
//    public boolean isSun;



    public int days;



    // it is for day_todo
    public Todo(String name, boolean isDone, boolean hasAlarm, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.isDone = isDone;
        this.hasAlarm = hasAlarm;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
