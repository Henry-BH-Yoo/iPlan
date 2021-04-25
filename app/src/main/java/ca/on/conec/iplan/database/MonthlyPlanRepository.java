/**
 * FileName : MonthlyPlan.java
 * Revision History :
 *          2021 04 19  Henry   Crate
 *          2021 04 19  Henry    modify
 */

package ca.on.conec.iplan.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.dao.BucketListDao;
import ca.on.conec.iplan.dao.MonthlyPlanDao;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;

public class MonthlyPlanRepository {
    private final MonthlyPlanDao mPlanDao;
    private final LiveData<List<MonthlyPlan>> mPlanList;

    public MonthlyPlanRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        mPlanDao = database.mPlanDao();
        mPlanList = mPlanDao.findAll();
    }

    public LiveData<List<MonthlyPlan>> findAll() {
        return mPlanList;
    }

    public LiveData<List<MonthlyPlan>> findByMonth(String currentMonth) {
        return mPlanDao.findByMonth(currentMonth);
    }

    public LiveData<List<MonthlyPlan>> findByDate(String currentDate) {
        return mPlanDao.findByDate(currentDate);
    }

    public LiveData<MonthlyPlan> find(long id) {
        return mPlanDao.findOne(id);
    }

    public void insert(MonthlyPlan mPlan) {
        AppDatabase.databaseWriterExecutor.execute( () -> mPlanDao.insert(mPlan));
    }

    public void update(MonthlyPlan mPlan) {
        AppDatabase.databaseWriterExecutor.execute( () -> mPlanDao.update(mPlan));
    }

    public void delete(MonthlyPlan mPlan) {
        AppDatabase.databaseWriterExecutor.execute( () -> mPlanDao.delete(mPlan));
    }

}
