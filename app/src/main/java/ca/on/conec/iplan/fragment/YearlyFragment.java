/**
 * FileName : YearlyFragment.java
 * Purpose
 * Revision History :
 *      2021.04.08 Sean    Create
 *      2021.04.18 Sean    Add floating action button
 *      2021.04.22 Sean    Add function of CRUD, except 'Progress'
 *      2021.04.23 Sean    Add spinner 'Year'
 *      2021.04.24 Sean    Add delete function on delete image icon
 */
package ca.on.conec.iplan.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.OnTodoYearClickListener;
import ca.on.conec.iplan.adapter.RecyclerYearViewAdapter;
import ca.on.conec.iplan.entity.TodoYear;
import ca.on.conec.iplan.viewmodel.SharedYearViewModel;
import ca.on.conec.iplan.viewmodel.TodoYearViewModel;

public class YearlyFragment extends Fragment implements OnTodoYearClickListener {

    public YearlyFragment() {
        // Required empty public constructor
    }

    private Spinner spnYear;
    String[] items = {"2021", "2022", "2023"};

    private TodoYearViewModel todoYearViewModel;
    private SharedYearViewModel sharedYearViewModel;

    private RecyclerView recyclerView;
    private RecyclerYearViewAdapter recyclerYearViewAdapter;

    BottomSheetYearFragment bottomSheetYearFragment;

    List<TodoYear> allYearTodos;

    OnTodoYearClickListener todoYearClickListener;

    private ItemTouchHelper mItemTouchHelper;
    private SharedPreferences sharedPref;
    private boolean isDarkAppTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yearly, container, false);


        PreferenceManager.setDefaultValues(v.getContext(), R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());


        // Connect a Bottom sheet to create and edit yearTodo
        bottomSheetYearFragment = new BottomSheetYearFragment();

        CoordinatorLayout coordinatorLayout = v.findViewById(R.id.bottomSheetYear);
        BottomSheetBehavior<CoordinatorLayout> bottomSheetBehaviorYear = BottomSheetBehavior.from(coordinatorLayout);
        bottomSheetBehaviorYear.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        // Declare Recycler View
        recyclerView = v.findViewById(R.id.recycler_view_year);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // It is for LiveData method in iPlanYearRepository.java
        todoYearViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(TodoYearViewModel.class);

        // Declare sharedYearViewModel
        sharedYearViewModel = new ViewModelProvider(requireActivity()).get(SharedYearViewModel.class);


        // Helper for filtering List and Adapter
        todoYearClickListener = this;


        todoYearViewModel.getAllTodos().observe(getViewLifecycleOwner(), todos -> {
            allYearTodos = todos;

            // Attach RecyclerView Adapter
            recyclerYearViewAdapter = new RecyclerYearViewAdapter(allYearTodos, todoYearClickListener);
            recyclerView.setAdapter(recyclerYearViewAdapter);

            //todo swipe delete
            // when swipe from right to left, ERROR of array null
            mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerYearViewAdapter));
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        });


        // FloatingActionButton
        FloatingActionButton fabYear = (FloatingActionButton) v.findViewById(R.id.fabYear);

        fabYear.setOnClickListener(view ->
            showBottomSheetDialog()
        );

        // Declare Spinner
        spnYear = v.findViewById(R.id.spnYear);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_item_year, items);

        spnYear.setAdapter(adapter);
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = spnYear.getItemAtPosition(position).toString();

                isDarkAppTheme = sharedPref.getBoolean("darkAppTheme", false);

                try {
                    if (isDarkAppTheme) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                } catch(Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No Action
            }
        });


        return v;
    }

    private void showBottomSheetDialog() {
        this.getChildFragmentManager().beginTransaction().add(bottomSheetYearFragment, bottomSheetYearFragment.getTag());

        bottomSheetYearFragment.show(getParentFragmentManager(), bottomSheetYearFragment.getTag());
    }

    @Override
    public void onTodoYearClick(TodoYear todoYear) {
        // now sharedYearViewModel has selected YearTodo which can be used anywhere
        sharedYearViewModel.selectItem(todoYear);
        sharedYearViewModel.setIsEdit(true);

        showBottomSheetDialog();
    }

    @Override
    public void onTodoDeleteImgClick(TodoYear todoYear) {
        TodoYearViewModel.delete(todoYear);

        // it will refresh RecyclerView
        recyclerYearViewAdapter.notifyDataSetChanged();
    }
}