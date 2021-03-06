/**
 * FileName : BottomSheetDayFragment.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.20 Sean    Add 7day tab and filtering, CRUD working
 *      2021.04.24 Sean    Modify time picker for time formatting
 *      2021.04.24 Sean    Modify time picker to dialog
 */
package ca.on.conec.iplan.fragment;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;
import java.util.Calendar;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.viewmodel.SharedViewModel;
import ca.on.conec.iplan.viewmodel.TodoViewModel;

// When clicks the Floating Plus btn, this fragment will pop up
public class BottomSheetDayFragment extends BottomSheetDialogFragment {

    private EditText etxtTodo, etxtStartTime, etxtEndTime;
    private Button btnSaveTodo, btnResetDay;
    private Switch swAlarm;
    private Chip chipMon, chipTue, chipWed, chipThu, chipFri, chipSat, chipSun;

    private String startTimeStr, endTimeStr;


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
        btnSaveTodo = view.findViewById(R.id.btnSaveDay);
        btnResetDay = view.findViewById(R.id.btnResetDay);
        swAlarm = view.findViewById(R.id.swAlarm);

        chipMon = view.findViewById(R.id.chipMon);
        chipTue = view.findViewById(R.id.chipTue);
        chipWed = view.findViewById(R.id.chipWed);
        chipThu = view.findViewById(R.id.chipThu);
        chipFri = view.findViewById(R.id.chipFri);
        chipSat = view.findViewById(R.id.chipSat);
        chipSun = view.findViewById(R.id.chipSun);

        etxtStartTime = view.findViewById(R.id.etxtStartTime);
        etxtEndTime = view.findViewById(R.id.etxtEndTime);

        LocalTime now = LocalTime.now();
        etxtStartTime.setHint(LocalTimeConverter.toTimeString(now));
        etxtEndTime.setHint(LocalTimeConverter.toTimeString(now));

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
            swAlarm.setChecked(todo.hasAlarm);

            String startTstr = LocalTimeConverter.toTimeString(todo.startTime);
            etxtStartTime.setText(startTstr);

            String endTstr = LocalTimeConverter.toTimeString(todo.endTime);
            etxtEndTime.setText(endTstr);

            chipMon.setChecked(todo.days == 1);
            chipTue.setChecked(todo.days == 2);
            chipWed.setChecked(todo.days == 3);
            chipThu.setChecked(todo.days == 4);
            chipFri.setChecked(todo.days == 5);
            chipSat.setChecked(todo.days == 6);
            chipSun.setChecked(todo.days == 7);

        } else if (!isEdit) {
            etxtTodo.setText("");
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


        LocalTime now = LocalTime.now();

        etxtStartTime.setOnClickListener(v -> {
            int hr = now.getHour();
            int min = now.getMinute();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar , (view1, hourOfDay, minute) -> {

                if (hourOfDay < 10 && minute < 10) {
                    String newHr = "0" + hourOfDay;
                    String newMin = "0" + minute;
                    startTimeStr = newHr + ":" + newMin;
                } else if (hourOfDay < 10 && minute >= 10) {
                    String newHour = "0" + hourOfDay;
                    startTimeStr = newHour + ":" + minute;
                } else if (hourOfDay > 10 && minute < 10) {
                    String newMin = "0" + minute;
                    startTimeStr = hourOfDay + ":" + newMin;
                } else {
                    startTimeStr = hourOfDay + ":" + minute;
                }

                // Display in EditText
                etxtStartTime.setText(startTimeStr);
            }, hr, min, false);
            mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mTimePicker.show();
        });

        etxtEndTime.setOnClickListener(v -> {
            int hr = now.getHour();
            int min = now.getMinute();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar , (view1, hourOfDay, minute) -> {

                if (hourOfDay < 10 && minute < 10) {
                    String newHr = "0" + hourOfDay;
                    String newMin = "0" + minute;
                    endTimeStr = newHr + ":" + newMin;
                } else if (hourOfDay < 10 && minute >= 10) {
                    String newHour = "0" + hourOfDay;
                    endTimeStr = newHour + ":" + minute;
                } else if (hourOfDay > 10 && minute < 10) {
                    String newMin = "0" + minute;
                    endTimeStr = hourOfDay + ":" + newMin;
                } else {
                    endTimeStr = hourOfDay + ":" + minute;
                }

                // Display in EditText
                etxtEndTime.setText(endTimeStr);
            }, hr, min, false);
            mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mTimePicker.show();
        });

        // validate before save or edit
        btnSaveTodo.setOnClickListener(v -> {

            boolean validationOk = true;

            String todoName = etxtTodo.getText().toString();

            LocalTime startTime = null;
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

            try {
                if (TextUtils.isEmpty(todoName)) {
                    validationMessage = "You must enter todo name\r\n" + validationMessage;
                    etxtTodo.requestFocus();
                    validationOk = false;
                }

                if (TextUtils.isEmpty(startTimeStr)) {
                    validationMessage = "You must enter Start time\r\n" + validationMessage;
                    etxtStartTime.requestFocus();
                    validationOk = false;
                } else {
                    startTime = LocalTimeConverter.toTime(startTimeStr);
                }

                if (TextUtils.isEmpty(endTimeStr)) {
                    validationMessage = "You must enter End time\r\n" + validationMessage;
                    etxtEndTime.requestFocus();
                    validationOk = false;
                } else {
                    endTime = LocalTimeConverter.toTime(endTimeStr);
                }

                if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
                    validationMessage = "Start time must be sooner than End time\r\n" + validationMessage;
                    etxtEndTime.requestFocus();
                    validationOk = false;
                }

                if (!cMon && !cTue && !cWed && !cThu && !cFri && !cSat && !cSun) {
                    validationMessage = "You must select at least 1 day\r\n" + validationMessage;
                    validationOk = false;
                }


                if (validationOk) {
                    if (isEdit) {

                        //allows user to select only one dayChip
                        if (cMon) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(1);
                            TodoViewModel.update(updateTodo);
                        } else if (cTue) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(2);
                            TodoViewModel.update(updateTodo);
                        } else if (cWed) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(3);
                            TodoViewModel.update(updateTodo);
                        } else if (cThu) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(4);
                            TodoViewModel.update(updateTodo);
                        } else if (cFri) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(5);
                            TodoViewModel.update(updateTodo);
                        } else if (cSat) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(6);
                            TodoViewModel.update(updateTodo);
                        } else if (cSun) {
                            Todo updateTodo = sharedViewModel.getSelectedItem().getValue();
                            updateTodo.setName(todoName);
                            updateTodo.setHasAlarm(hasAlarm);
                            updateTodo.setStartTime(startTime);
                            updateTodo.setEndTime(endTime);
                            updateTodo.setDays(7);
                            TodoViewModel.update(updateTodo);
                        }
                        sharedViewModel.setIsEdit(false);
                    } else {
                        // Insert, not Update
                        if (cMon) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(1);
                            TodoViewModel.insert(todo);
                        }
                        if (cTue) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(2);
                            TodoViewModel.insert(todo);
                        }
                        if (cWed) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(3);
                            TodoViewModel.insert(todo);
                        }
                        if (cThu) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(4);
                            TodoViewModel.insert(todo);
                        }
                        if (cFri) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(5);
                            TodoViewModel.insert(todo);
                        }
                        if (cSat) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(6);
                            TodoViewModel.insert(todo);
                        }
                        if (cSun) {
                            Todo todo = new Todo(todoName, false, hasAlarm, startTime, endTime);
                            todo.setDays(7);
                            TodoViewModel.insert(todo);
                        }
                    }
                    etxtTodo.setText("");
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
                    Snackbar.make(v, validationMessage, Snackbar.LENGTH_LONG).setAction("Error", null).show();
                }
            } catch (Exception e) {
                Snackbar.make(v, e.toString(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
            }
        });

        btnResetDay.setOnClickListener(v -> {
            etxtTodo.setText("");
            swAlarm.setChecked(false);

            chipMon.setChecked(false);
            chipTue.setChecked(false);
            chipWed.setChecked(false);
            chipThu.setChecked(false);
            chipFri.setChecked(false);
            chipSat.setChecked(false);
            chipSun.setChecked(false);
        });
    }
}