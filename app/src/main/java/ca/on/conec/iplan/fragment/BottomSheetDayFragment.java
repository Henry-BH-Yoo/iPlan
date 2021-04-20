package ca.on.conec.iplan.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Switch;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;
import ca.on.conec.iplan.viewmodel.SharedViewModel;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.viewmodel.TodoViewModel;

// When clicks the Floating Plus btn, this fragment will pop up
public class BottomSheetDayFragment extends BottomSheetDialogFragment {

    private EditText etxtTodo;
    private Button btnSaveTodo;
    private RadioButton rdoSelectedTodo;
    private Switch swAlarm;
    private Chip chipMon, chipTue, chipWed, chipThu, chipFri, chipSat, chipSun;

    //todo change it to Time_Picker_Dialog
    private EditText etxtStart;
    private EditText etxtEnd;

    private SharedViewModel sharedViewModel;
    private boolean isEdit;

    public BottomSheetDayFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        chipMon = view.findViewById(R.id.chipMon);
        chipTue = view.findViewById(R.id.chipTue);
        chipWed = view.findViewById(R.id.chipWed);
        chipThu = view.findViewById(R.id.chipThu);
        chipFri = view.findViewById(R.id.chipFri);
        chipSat = view.findViewById(R.id.chipSat);
        chipSun = view.findViewById(R.id.chipSun);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();

        boolean isEditing = sharedViewModel.getIsEdit();

        if (sharedViewModel.getSelectedItem().getValue() != null && isEditing) {
            isEdit = sharedViewModel.getIsEdit();

            Todo todo = sharedViewModel.getSelectedItem().getValue();
            etxtTodo.setText(todo.getName());
            etxtStart.setText(LocalTimeConverter.toTimeString(todo.startTime));
            etxtEnd.setText(LocalTimeConverter.toTimeString(todo.endTime));
            swAlarm.setChecked(todo.hasAlarm);

            chipMon.setChecked(todo.isMon);
            chipTue.setChecked(todo.isTue);
            chipWed.setChecked(todo.isWed);
            chipThu.setChecked(todo.isThu);
            chipFri.setChecked(todo.isFri);
            chipSat.setChecked(todo.isSat);
            chipSun.setChecked(todo.isSun);

        } else if (!isEdit) {
            etxtTodo.setText("");
            etxtStart.setText("");
            etxtEnd.setText("");
            swAlarm.setChecked(false);

            chipMon.setChecked(false);
            chipTue.setChecked(false);
            chipWed.setChecked(false);
            chipThu.setChecked(false);
            chipFri.setChecked(false);
            chipSat.setChecked(false);
            chipSun.setChecked(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        isEdit = false;
        sharedViewModel.setIsEdit(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

//        //this sharedViewModel should be the same one in DailyFragment sharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // validate before save or edit
        btnSaveTodo.setOnClickListener(v -> {

            Snackbar.make(btnSaveTodo, "testing", Snackbar.LENGTH_LONG).show();

            boolean validationOk = true;

            String todoName = etxtTodo.getText().toString();
            String startTimeStr = etxtStart.getText().toString();
            LocalTime startTime = null;
            String endTimeStr = etxtEnd.getText().toString();
            LocalTime endTime = null;
            boolean hasAlarm = swAlarm.isChecked();

            boolean cMon = chipMon.isChecked();
            boolean cTue = chipTue.isChecked();
            boolean cWed = chipWed.isChecked();
            boolean cThu = chipThu.isChecked();
            boolean cFri = chipFri.isChecked();
            boolean cSat = chipSat.isChecked();
            boolean cSun = chipSun.isChecked();

            String validationMessage = "";

            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) requireActivity().findViewById(R.id.frag_daily_layout);

            try {
                if (TextUtils.isEmpty(todoName)) {
                    validationMessage = "You must enter todo name\r\n" + validationMessage;
                    etxtTodo.requestFocus();
                    validationOk = false;
                }

                if (TextUtils.isEmpty(startTimeStr)) {
                    validationMessage = "You must enter Start time\r\n" + validationMessage;
                    etxtStart.requestFocus();
                    validationOk = false;
                } else {
                    startTime = LocalTimeConverter.toTime(startTimeStr);
                }

                if (TextUtils.isEmpty(endTimeStr)) {
                    validationMessage = "You must enter End time\r\n" + validationMessage;
                    etxtEnd.requestFocus();
                    validationOk = false;
                } else {
                    endTime = LocalTimeConverter.toTime(endTimeStr);
                }

                if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
                    validationMessage = "Start time must be sooner than End time\r\n" + validationMessage;
                    etxtStart.requestFocus();
                    validationOk = false;
                }

                if (!cMon && !cTue && !cWed && !cThu && !cFri && !cSat && !cSun) {
                    validationMessage = "You must select at least 1 day\r\n" + validationMessage;
                    validationOk = false;
                }

                if(validationOk) {
                    if (isEdit) {
                        Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                        updateTodo.setName(todoName);
                        updateTodo.setHasAlarm(hasAlarm);
                        updateTodo.setStartTime(startTime);
                        updateTodo.setEndTime(endTime);

                        updateTodo.setMon(cMon);
                        updateTodo.setTue(cTue);
                        updateTodo.setWed(cWed);
                        updateTodo.setThu(cThu);
                        updateTodo.setFri(cFri);
                        updateTodo.setSat(cSat);
                        updateTodo.setSun(cSun);

                        TodoViewModel.update(updateTodo);
                        sharedViewModel.setIsEdit(false);
                    } else {
                        Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);

                        todo.setMon(cMon);
                        todo.setTue(cTue);
                        todo.setWed(cWed);
                        todo.setThu(cThu);
                        todo.setFri(cFri);
                        todo.setSat(cSat);
                        todo.setSun(cSun);

                        TodoViewModel.insert(todo);
                    }
                    etxtTodo.setText("");
                    etxtStart.setText("");
                    etxtEnd.setText("");
                    swAlarm.setChecked(false);

                    chipMon.setChecked(false);
                    chipTue.setChecked(false);
                    chipWed.setChecked(false);
                    chipThu.setChecked(false);
                    chipFri.setChecked(false);
                    chipSat.setChecked(false);
                    chipSun.setChecked(false);

                    if (this.isVisible()) this.dismiss(); // close bottom sheet dialog
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, validationMessage, Snackbar.LENGTH_LONG).setAction("Error", null);
                    View addView = snackbar.getView();
                    CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams) addView.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    addView.setLayoutParams(params);
                    snackbar.show();
                }
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, e.toString(), Snackbar.LENGTH_LONG).setAction("Error", null);
                View addView = snackbar.getView();
                CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams) addView.getLayoutParams();
                params.gravity = Gravity.TOP;
                addView.setLayoutParams(params);
                snackbar.show();
            }
        });
    }
}