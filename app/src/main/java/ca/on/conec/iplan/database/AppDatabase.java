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
import ca.on.conec.iplan.dao.TodoDao;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.Todo;

/*
 * Creating database use many resources, so recommend Singleton Pattern
 */
@Database(entities = {BucketList.class ,  Todo.class}, version = 3, exportSchema = false)
@TypeConverters({LocalTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract BucketListDao bucketDao();
    public abstract TodoDao todoDao();

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
                // write to the table

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