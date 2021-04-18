package ca.on.conec.iplan.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalTime;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.SharedViewModel;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.entity.TodoViewModel;

// When clicks the Floating Plus btn, this fragment will pop up
public class BottomSheetDayFragment extends BottomSheetDialogFragment {

    private EditText etxtTodo;
    private Button btnSaveTodo;
    private RadioButton rdoSelectedTodo;
    private Switch swAlarm;

    //todo change it to Time_Picker_Dialog
    private EditText etxtStart;
    private EditText etxtEnd;

    //todo what for?
    private int selectedBtnId;

    private SharedViewModel sharedViewModel;


    public BottomSheetDayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_day, container, false);

        etxtTodo = view.findViewById(R.id.etxtDayTodo);
        etxtStart = view.findViewById(R.id.etxtStartTime);
        etxtEnd = view.findViewById(R.id.etxtEndTime);
        btnSaveTodo = view.findViewById(R.id.btnSaveDay);
        swAlarm = view.findViewById(R.id.swAlarm);

        rdoSelectedTodo = view.findViewById(R.id.rdoDayRow);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //todo REMEMBER if a Fragment is inside of other Fragment, it seems there's 2 way to handle it?
        // 1. use parentFragment, childFragment - it was working
        // 2. use requireActivity() - not working now

        // this sharedViewModel is same one in DailyFragment sharedViewModel
        //todo ERR here - need to get DailyFragment's sharedViewModel
//        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // original f25fa19
//        sharedViewModel = new ViewModelProvider(dailyFragment.getActivity()).get(SharedViewModel.class);
        sharedViewModel = new ViewModelProvider(this.getActivity()).get(SharedViewModel.class);


//        this.getChildFragmentManager().beginTransaction().add(bottomSheetDayFragment, bottomSheetDayFragment.getTag());
//        bottomSheetDayFragment.show(getParentFragmentManager(), bottomSheetDayFragment.getTag());

//        this.getChildFragmentManager().beginTransaction().add(dailyFragment, dailyFragment.getTag());
//        sharedViewModel = new ViewModelProvider().get(SharedViewModel.class);




        if (sharedViewModel.getSelectedItem().getValue() != null) {
            Todo todo = sharedViewModel.getSelectedItem().getValue();

            Log.d("My", "todo name: " + todo.getName());
        } else if (sharedViewModel.getSelectedItem().getValue() == null) {
            Todo todo = sharedViewModel.getSelectedItem().getValue(); // ERROR todo obj is null

//            Log.d("My", "todo name: " + todo.getName());
            Log.d("My", "sharedViewModel is: " + sharedViewModel.toString());
//            Log.d("My", "todo obj is null"); // working this msg
        }


        btnSaveTodo.setOnClickListener(v -> {
            String todoName = etxtTodo.getText().toString().trim();

            String startTimeStr = etxtStart.getText().toString();
            LocalTime startTime = LocalTimeConverter.toTime(startTimeStr);

            String endTimeStr = etxtEnd.getText().toString();
            LocalTime endTime = LocalTimeConverter.toTime(endTimeStr);

            boolean hasAlarm = swAlarm.isChecked();

            if (!TextUtils.isEmpty(todoName)) {
                Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);

                TodoViewModel.insert(todo);
            }
        });
    }
}