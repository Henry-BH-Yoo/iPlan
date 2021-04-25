/**
 * FileName : MonthlyPlanListFragment.java
 * Revision History :
 *          2021 03 25  Henry   Create
 *          2021 04 19  Henry    complete bucket
 *          2021 04 23  Henry    modify
 */

package ca.on.conec.iplan.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.adapter.BucketListAdapter;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.MPlanListAdapter;
import ca.on.conec.iplan.calendar.EventDecorator;
import ca.on.conec.iplan.calendar.SaturdayDecorator;
import ca.on.conec.iplan.calendar.SundayDecorator;
import ca.on.conec.iplan.calendar.TodayDecorator;
import ca.on.conec.iplan.entity.MonthlyPlan;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;
import ca.on.conec.iplan.viewmodel.MonthlyPlanViewModel;

public class MonthlyFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<MonthlyPlan> mPlanList = new ArrayList<MonthlyPlan>();
    private MPlanListAdapter mPlanListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private MonthlyPlanViewModel monthlyPlanViewModel;

    private MaterialCalendarView materialCalendarView;
    private BottomSheetMonthFragment bottomSheetMonthFragment;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private Integer selectedDay = null;
    private Calendar calendar;
    private EventDecorator eventDecorator;

    private String selectedDate = "";

    public MonthlyFragment() {
        calendar = Calendar.getInstance();
        eventDecorator = new EventDecorator();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_monthly, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        monthlyPlanViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(MonthlyPlanViewModel.class);

        materialCalendarView = v.findViewById(R.id.calendarView);
        materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.addDecorators( new SundayDecorator() ,  new SaturdayDecorator() , new TodayDecorator());


        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("YYYYMM");
        String currentMonth = simpleDate.format(mDate);

        FloatingActionButton addMonthlyPlanBtn = v.findViewById(R.id.addMonthlyPlanBtn);

        bottomSheetMonthFragment = new BottomSheetMonthFragment();

        CoordinatorLayout frameLayout = v.findViewById(R.id.bottomSheetMonth);
        BottomSheetBehavior<CoordinatorLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);


        addMonthlyPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedYear == null || selectedMonth == null || selectedDay == null) {
                    Snackbar.make(v, "Before addition, you must select specific day in the calendar", Snackbar.LENGTH_LONG).show();
                } else {
                    String month = "";

                    if(selectedMonth < 10) {
                        month = "0" + selectedMonth;
                    } else {
                        month = selectedMonth.toString();
                    }
                    selectedDate = selectedYear.toString() + month + selectedDay.toString();

                    showBottomSheetDialog(selectedDate , 0);
                }
            }
        });

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            String year , month , day;

            if(selected) {
                selectedYear = date.getYear();
                selectedMonth = date.getMonth() + 1;
                selectedDay = date.getDay();
            }

            if(selectedMonth < 10) {
                month = "0" + selectedMonth;
            } else {
                month = selectedMonth.toString();
            }

            if(selectedDay < 10) {
                day = "0" + selectedDay;
            } else {
                day = selectedDay.toString();
            }

            selectedDate = selectedYear.toString() + month + day;


            getPlanByDay(selectedDate);
        });

        getPlanByMonth(currentMonth);

        return v;
    }

    public void showBottomSheetDialog(String selectedDate , int mPlanId) {
        Bundle bundle = new Bundle();
        bundle.putString("mPlanDate" , selectedDate);
        bundle.putInt("mPlanId" , mPlanId);

        getChildFragmentManager().beginTransaction().add(bottomSheetMonthFragment, bottomSheetMonthFragment.getTag());
        bottomSheetMonthFragment.setArguments(bundle);
        bottomSheetMonthFragment.show(getParentFragmentManager(), bottomSheetMonthFragment.getTag());
    }


    private void getDataList() {

        monthlyPlanViewModel.findAll().observe(getViewLifecycleOwner(),  mPlanList -> {
            this.mPlanList = mPlanList;
            bindAdapter();
        });
    }

    private void getPlanByDay(String currentDate) {

        monthlyPlanViewModel.findByDay(currentDate).observe(getViewLifecycleOwner(),  mPlanList -> {
            if(currentDate.equals(selectedDate)) {
                this.mPlanList = mPlanList;
                bindAdapter();
            }
        });


    }

    private void getPlanByMonth(String currentMonth) {

        monthlyPlanViewModel.findByMonth(currentMonth).observe(getViewLifecycleOwner(),  mPlanList -> {
            materialCalendarView.removeDecorator(eventDecorator);
            ArrayList<CalendarDay> dateList = new ArrayList<CalendarDay>();
            for(MonthlyPlan m : mPlanList) {
                int year = Integer.parseInt(m.getMPlanDate().substring(0,4));
                int month = Integer.parseInt(m.getMPlanDate().substring(5,6)) - 1;
                int day = Integer.parseInt(m.getMPlanDate().substring(6,8));

                dateList.add(CalendarDay.from(year , month , day));
            }

            eventDecorator = new EventDecorator(Color.RED , dateList);
            materialCalendarView.addDecorator(eventDecorator);

        });
    }


    /**
     * binding recyclerView.
     */
    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mPlanListAdapter = new MPlanListAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(mPlanListAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mPlanListAdapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mPlanListAdapter.setBucketList(mPlanList);


        mPlanListAdapter.setOnItemClickListener(new MPlanListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String selectedDate, int mPlanId) {
                showBottomSheetDialog(selectedDate , mPlanId);
            }

            @Override
            public void onDeleteBtnClick(View v, MonthlyPlan mPlan) {
                try {
                    MonthlyPlanViewModel.delete(mPlan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}