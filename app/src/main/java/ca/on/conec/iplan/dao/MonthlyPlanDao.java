/**
 * FileName : MOnthlyPlan.java
 * Purpose : Monthly Plan Dao using ROOM
 * Revision History :
 *          2021 04 23  Henry   Create DAO
 */
package ca.on.conec.iplan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;

@Dao
public interface MonthlyPlanDao {

    @Query("SELECT * FROM MPLAN")
    LiveData<List<MonthlyPlan>> findAll();

    @Query("SELECT * FROM MPLAN WHERE substr(date, 1,6) == :currentMonth")
    LiveData<List<MonthlyPlan>> findByMonth(String currentMonth);

    @Query("SELECT * FROM MPLAN WHERE date == :currentDate")
    LiveData<List<MonthlyPlan>> findByDate(String currentDate);

    @Query("SELECT * FROM MPLAN WHERE `mPlanId` == :id")
    LiveData<MonthlyPlan> findOne(long id);

    @Insert
    void insertAll(MonthlyPlan... mPlan);

    @Insert
    void insert(MonthlyPlan mPlan);

    @Update
    void update(MonthlyPlan mPlan);

    @Delete
    void delete(MonthlyPlan mPlan);
}
