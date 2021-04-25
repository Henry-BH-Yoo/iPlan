/**
 * FileName : BucketListViewModel.java
 * Revision History :
 *          2021 04 19  Henry    Create
 */

package ca.on.conec.iplan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.iplan.database.BucketRepository;
import ca.on.conec.iplan.entity.BucketList;

// Inherits from AndroidViewModel, different from SharedViewModel
public class BucketListViewModel extends AndroidViewModel {

    public static BucketRepository repository;
    public final LiveData<List<BucketList>> bucketList;

    public BucketListViewModel(@NonNull Application application) {
        super(application);
        repository = new BucketRepository(application);
        bucketList = repository.findAll();
    }

    public LiveData<List<BucketList>> findAll() {
        return bucketList;
    }

    public static void insert(BucketList bucket) {
        repository.insert(bucket);
    }

    public LiveData<BucketList> find(long id) {
        return repository.find(id);
    }

    public static void update(BucketList bucket) {
        repository.update(bucket);
    }

    public static void delete(BucketList bucket) {
        repository.delete(bucket);
    }
}
