/**
 * FileName : AppDatabase.java
 * Purpose : BucketList Dao using ROOM
 * Revision History :
 *          2021 03 25  Henry   Crate appdatabase
 *          2021 04 18  Sean    add todo table
 *          2021 04 19  Henry    add Bucket table
 *          2021 04 20  Sean    modify todo
 *          2021 04 22  Sean    modify todo
 *          2021 04 23  Sean    fix bug
 *          2021 04 23  Henry   add monthly table
 */

package ca.on.conec.iplan.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.on.conec.iplan.dao.BucketListDao;
import ca.on.conec.iplan.dao.MonthlyPlanDao;
import ca.on.conec.iplan.dao.TodoDao;
import ca.on.conec.iplan.dao.TodoYearDao;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.entity.TodoYear;

/*
 * Creating database use many resources, so recommend Singleton Pattern
 */
@Database(entities = {BucketList.class ,  Todo.class, TodoYear.class , MonthlyPlan.class}, version = 2, exportSchema = false)
@TypeConverters({LocalTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract BucketListDao bucketDao();
    public abstract TodoDao todoDao();
    public abstract TodoYearDao todoYearDao();
    public abstract MonthlyPlanDao mPlanDao();

    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(4);

    public static final RoomDatabase.Callback RoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                // invoke Dao, and write
                TodoDao todoDao = INSTANCE.todoDao();
                todoDao.deleteAll(); // make it clean

                BucketListDao bucketListDao = INSTANCE.bucketDao();
                TodoYearDao todoYearDao = INSTANCE.todoYearDao();

                MonthlyPlanDao mPlanDao = INSTANCE.mPlanDao();

            });
        }
    };

    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context , AppDatabase.class , "iPlan")
                    .addCallback(RoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}