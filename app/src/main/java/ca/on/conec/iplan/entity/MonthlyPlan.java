/**
 * FileName : BucketList.java
 * Purpose
 * Revision History :
 *      2021.04.23 Henry    Create
 */

package ca.on.conec.iplan.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Entity(tableName = "mPlan")
@Data
public class MonthlyPlan {

    @PrimaryKey(autoGenerate = true)
    public int mPlanId;

    @ColumnInfo(name = "title")
    public String mPlanTitle;

    @ColumnInfo(name = "date")
    public String mPlanDate;

    @ColumnInfo(name = "start_time")
    public String mPlanStart;

    @ColumnInfo(name = "end_time")
    public String mPlanEnd;

}
