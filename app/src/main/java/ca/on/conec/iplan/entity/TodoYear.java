/**
 * FileName : TodoYear.java
 * Purpose
 * Revision History :
 *      2021.04.22 Sean    Create
 */
package ca.on.conec.iplan.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Entity(tableName = "todoYear_table")
@Data
public class TodoYear {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    public long todoId;

    public String name;


    @ColumnInfo(name = "progress_Type")
    public String progressType;

    @ColumnInfo(name = "current_Status")
    public double currentStatus;

    public double goal;


    public String note;

}
