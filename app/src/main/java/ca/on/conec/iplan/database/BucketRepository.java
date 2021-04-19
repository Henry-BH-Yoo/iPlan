package ca.on.conec.iplan.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.dao.BucketListDao;
import ca.on.conec.iplan.entity.BucketList;

public class BucketRepository {
    private final BucketListDao bucketListDao;
    private final LiveData<List<BucketList>> bucketList;

    public BucketRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        bucketListDao = database.bucketDao();
        bucketList = bucketListDao.findAll();
    }

    public LiveData<List<BucketList>> findAll() {
        return bucketList;
    }

    public LiveData<BucketList> find(long id) {
        return bucketListDao.findOne(id);
    }

    public void insert(BucketList bucket) {
        AppDatabase.databaseWriterExecutor.execute( () -> bucketListDao.insert(bucket));
    }

    public void update(BucketList bucket) {
        AppDatabase.databaseWriterExecutor.execute( () -> bucketListDao.update(bucket));
    }

    public void delete(BucketList bucket) {
        AppDatabase.databaseWriterExecutor.execute( () -> bucketListDao.delete(bucket));
    }
}
