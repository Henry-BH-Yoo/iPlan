package ca.on.conec.iplan.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
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

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.activity.MainActivity;
import ca.on.conec.iplan.adapter.OnTodoClickListener;
import ca.on.conec.iplan.adapter.RecyclerViewAdapter;
import ca.on.conec.iplan.viewmodel.SharedViewModel;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.viewmodel.TodoViewModel;

public class DailyFragment extends Fragment implements OnTodoClickListener {

    private static final String TAG = "ITEM";

    public DailyFragment() {
        // Required empty public constructor
    }

    //todo Drag n drop for delete

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily, container, false);

        // Connect a Bottom sheet to create and edit dayTodo
        bottomSheetDayFragment = new BottomSheetDayFragment();
        FrameLayout frameLayout = v.findViewById(R.id.bottomSheetDay);
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
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

        // select only Mon by default
        todoViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
            allDayTodos = todos;

            onlyMonTodos = allDayTodos.stream().filter(a -> a.isMon).collect(Collectors.toList());

            // Attach RecyclerView Adapter
            recyclerViewAdapter = new RecyclerViewAdapter(onlyMonTodos, todoClickListener);
            recyclerView.setAdapter(recyclerViewAdapter);
        });




        // tab layout of 7 days
        TabLayout tabs = v.findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // Process tab selection event
                int position = tab.getPosition();

                switch (position) {
                    case 0: // Mon
                        onlyMonTodos = allDayTodos.stream().filter(a -> a.isMon).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlyMonTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 1: // Tue
                        onlyTueTodos = allDayTodos.stream().filter(a -> a.isTue).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlyTueTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 2: // Wed
                        onlyWedTodos = allDayTodos.stream().filter(a -> a.isWed).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlyWedTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 3: // Thu
                        onlyThuTodos = allDayTodos.stream().filter(a -> a.isThu).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlyThuTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 4: // Fri
                        onlyFriTodos = allDayTodos.stream().filter(a -> a.isFri).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlyFriTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 5: // Sat
                        onlySatTodos = allDayTodos.stream().filter(a -> a.isSat).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlySatTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                    case 6: // Sun
                        onlySunTodos = allDayTodos.stream().filter(a -> a.isSun).collect(Collectors.toList());
                        recyclerViewAdapter = new RecyclerViewAdapter(onlySunTodos, todoClickListener);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        break;
                }
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

    private void showBottomSheetDialog() {
        this.getChildFragmentManager().beginTransaction().add(bottomSheetDayFragment, bottomSheetDayFragment.getTag());

//        bottomSheetDayFragment.onDetach();
//        bottomSheetDayFragment.onDestroy();
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
    public void onTodoRadioBtnClick(Todo todo) {
        Log.d("My", "onRadioBtnClick: " + todo.getName());

        TodoViewModel.delete(todo);

        // it will refresh RecyclerView
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTodoIsDoneChkClick(Todo todo) {
        Log.d("My", "onChkboxClick: " + todo.getName());

        todo.setDone(!todo.isDone);
        TodoViewModel.update(todo);

        // it will refresh RecyclerView
        recyclerViewAdapter.notifyDataSetChanged();

    }
}