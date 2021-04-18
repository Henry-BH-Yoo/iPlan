package ca.on.conec.iplan.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.logging.Logger;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.adapter.OnTodoClickListener;
import ca.on.conec.iplan.adapter.RecyclerViewAdapter;
import ca.on.conec.iplan.entity.SharedViewModel;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.entity.TodoViewModel;

public class DailyFragment extends Fragment implements OnTodoClickListener {

    private static final String TAG = "ITEM";

    public DailyFragment() {
        // Required empty public constructor
    }


    //todo once check box is checked, update todo isDone -> true?


    private TodoViewModel todoViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    BottomSheetDayFragment bottomSheetDayFragment;
    private SharedViewModel sharedViewModel;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily, container, false);


        // Connect a Bottom sheet to create dayTodo
        bottomSheetDayFragment = new BottomSheetDayFragment();
        FrameLayout frameLayout = v.findViewById(R.id.bottomSheetDay);
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);


        // Recycler View
        recyclerView = v.findViewById(R.id.recycler_view_day);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        // it is for LiveData method in iPlanRepository.java
        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                DailyFragment.this.getActivity().getApplication()).create(TodoViewModel.class);

        // declare sharedViewModel
        // check if this makes error - no err here
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        if (sharedViewModel != null) {
            Log.d("My", "sharedViewModel is not null here");
            Log.d("My", "sharedViewModel is: " + sharedViewModel.toString());
        }


        todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
            // attach RecyclerView Adapter
            recyclerViewAdapter = new RecyclerViewAdapter(todos, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        // tab layout of 7 days
        //todo once recycler view is done, connect it with 7 days tabs
        TabLayout tabs = v.findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //todo : process tab selection event
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        }) ;

        // Floating Action Button
        FloatingActionButton fabDay = v.findViewById(R.id.fabDay);

        fabDay.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        return v;
    }

    private void showBottomSheetDialog() {
        this.getChildFragmentManager().beginTransaction().add(bottomSheetDayFragment, bottomSheetDayFragment.getTag());
        bottomSheetDayFragment.show(getParentFragmentManager(), bottomSheetDayFragment.getTag());
    }

    @Override
    public void onTodoClick(Todo todo) {
//        Log.d("Click", "onTodoClick: " + todo.getName());

        // now sharedViewModel has selected DayTodo which can be used anywhere
        sharedViewModel.selectItem(todo);

        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioBtnClick(Todo todo) {
        Log.d("Click", "onRadioBtnClick: " + todo.getName());

        //todo before delete, pop up dialog to check if user confirms
        TodoViewModel.delete(todo);

        // it will refresh RecyclerView
        recyclerViewAdapter.notifyDataSetChanged();
    }
}