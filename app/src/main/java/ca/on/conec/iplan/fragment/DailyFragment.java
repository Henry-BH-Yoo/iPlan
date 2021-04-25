package ca.on.conec.iplan.fragment;

import android.os.AsyncTask;
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
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.ItemTouchHelperListener;
import ca.on.conec.iplan.adapter.OnTodoClickListener;
import ca.on.conec.iplan.adapter.RecyclerViewAdapter;
import ca.on.conec.iplan.viewmodel.SharedViewModel;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.viewmodel.TodoViewModel;

public class DailyFragment extends Fragment implements OnTodoClickListener {

    public DailyFragment() {
        // Required empty public constructor
    }

    private TodoViewModel todoViewModel;
    private SharedViewModel sharedViewModel;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    BottomSheetDayFragment bottomSheetDayFragment;

    List<Todo> allDayTodos;
    List<Todo> onlyMonTodos;
    List<Todo> onlyTueTodos;
    List<Todo> onlyWedTodos;
    List<Todo> onlyThuTodos;
    List<Todo> onlyFriTodos;
    List<Todo> onlySatTodos;
    List<Todo> onlySunTodos;

    OnTodoClickListener todoClickListener;

    private ItemTouchHelper mItemTouchHelper;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily, container, false);

        // Connect a Bottom sheet to create and edit dayTodo
        bottomSheetDayFragment = new BottomSheetDayFragment();

        CoordinatorLayout coordinatorLayout = v.findViewById(R.id.bottomSheetDay);
        BottomSheetBehavior<CoordinatorLayout> bottomSheetBehavior = BottomSheetBehavior.from(coordinatorLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        // Declare Recycler View
        recyclerView = v.findViewById(R.id.recycler_view_day);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // It is for LiveData method in iPlanRepository.java
        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(TodoViewModel.class);

        // Declare sharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        // Helper for filtering List and Adapter
        todoClickListener = this;


        // tab layout of 7 days
        TabLayout tabs = v.findViewById(R.id.tabs);


        // get day of week
        LocalDateTime now = LocalDateTime.now();
        int days = now.getDayOfWeek().getValue();

        tabs.selectTab(tabs.getTabAt(days - 1));

        int selectedTabPosition = tabs.getSelectedTabPosition();
        selectDayAdapter(selectedTabPosition);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // Process tab selection event
                int position = tab.getPosition();

                selectDayAdapter(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        // Floating Action Button
        FloatingActionButton fabDay = v.findViewById(R.id.fabDay);

        fabDay.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectDayAdapter(int position) {
        switch (position) {
            case 0: // Mon
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlyMonTodos = allDayTodos.stream().filter(a -> a.days == 1).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlyMonTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 1: // Tue
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlyTueTodos = allDayTodos.stream().filter(a -> a.days == 2).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlyTueTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 2: // Wed
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlyWedTodos = allDayTodos.stream().filter(a -> a.days == 3).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlyWedTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 3: // Thu
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlyThuTodos = allDayTodos.stream().filter(a -> a.days == 4).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlyThuTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 4: // Fri
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlyFriTodos = allDayTodos.stream().filter(a -> a.days == 5).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlyFriTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 5: // Sat
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlySatTodos = allDayTodos.stream().filter(a -> a.days == 6).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlySatTodos, todoClickListener);

//                    recyclerViewAdapter.notifyDataSetChanged();

                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
            case 6: // Sun
                todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
                    allDayTodos = todos;

                    onlySunTodos = allDayTodos.stream().filter(a -> a.days == 7).collect(Collectors.toList());

                    // Attach RecyclerView Adapter
                    recyclerViewAdapter = new RecyclerViewAdapter(onlySunTodos, todoClickListener);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                });
                break;
        }
    }

    private void showBottomSheetDialog() {
        this.getChildFragmentManager().beginTransaction().add(bottomSheetDayFragment, bottomSheetDayFragment.getTag());

        bottomSheetDayFragment.show(getParentFragmentManager(), bottomSheetDayFragment.getTag());
    }

    @Override
    public void onTodoClick(Todo todo) {
        // now sharedViewModel has selected DayTodo which can be used anywhere
        sharedViewModel.selectItem(todo);
        sharedViewModel.setIsEdit(true);

        showBottomSheetDialog();
    }

    @Override
    public void onTodoIsDoneChkClick(Todo todo) {
        Log.d("My", "onChkboxClick: " + todo.getName());

        todo.setDone(!todo.isDone);
        TodoViewModel.update(todo);

        // it will refresh RecyclerView
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTodoDeleteImgClick(Todo todo) {
        TodoViewModel.delete(todo);

        // it will refresh RecyclerView
        recyclerViewAdapter.notifyDataSetChanged();
    }
}