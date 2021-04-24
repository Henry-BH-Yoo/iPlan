package ca.on.conec.iplan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.on.conec.iplan.database.BucketRepository;
import ca.on.conec.iplan.database.MonthlyPlanRepository;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;

// Inherits from AndroidViewModel, different from SharedViewModel
public class MonthlyPlanViewModel extends AndroidViewModel {

    public static MonthlyPlanRepository repository;
    public final LiveData<List<MonthlyPlan>> mPlanList;

    public MonthlyPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new MonthlyPlanRepository(application);
        mPlanList = repository.findAll();
    }

    public LiveData<List<MonthlyPlan>> findAll() {
        return mPlanList;
    }

    public LiveData<List<MonthlyPlan>> findByMonth(String currentMonth) {
        return repository.findByMonth(currentMonth);
    }

    public LiveData<List<MonthlyPlan>> findByDay(String currentDate) {
        return repository.findByDate(currentDate);
    }

    public static void insert(MonthlyPlan mPlan) {
        repository.insert(mPlan);
    }

    public LiveData<MonthlyPlan> find(long id) {
        return repository.find(id);
    }

    public static void update(MonthlyPlan mPlan) {
        repository.update(mPlan);
    }

    public static void delete(MonthlyPlan mPlan) {
        repository.delete(mPlan);
    }
}
