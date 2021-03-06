/**
 * FileName : BottomSheetYearFragment.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.22 Sean    Add function of CRUD, except 'Progress'
 *      2021.04.23 Sean    Add spinner 'Progress'
 */
package ca.on.conec.iplan.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.TodoYear;
import ca.on.conec.iplan.viewmodel.SharedYearViewModel;
import ca.on.conec.iplan.viewmodel.TodoYearViewModel;

// When clicks the Floating Plus btn, this fragment will pop up
public class BottomSheetYearFragment extends BottomSheetDialogFragment {

    private EditText etxtYearTodo, etxtMlNoteYear;
    private Button btnResetYear, btnSaveYear;

    private Spinner spnProgressTypeYear;

    private String selectedProgressType = "Quantity";
    int progressTypeIdx;

    private EditText etxtCurrentStatusYear, etxtGoalYear;

    private SharedYearViewModel sharedYearViewModel;
    private boolean isEdit;

    private SharedPreferences sharedPref;
    private boolean isDarkAppTheme;

    public BottomSheetYearFragment() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_year, container, false);


        PreferenceManager.setDefaultValues(view.getContext(), R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        isDarkAppTheme = sharedPref.getBoolean("darkAppTheme", false);

        etxtYearTodo = view.findViewById(R.id.etxtYearTodo);
        etxtMlNoteYear = view.findViewById(R.id.etxtMlNoteYear);

        btnResetYear = view.findViewById(R.id.btnResetYear);
        btnSaveYear = view.findViewById(R.id.btnSaveYear);

        etxtCurrentStatusYear = view.findViewById(R.id.etxtCurrentStatusYear);
        etxtGoalYear = view.findViewById(R.id.etxtGoalYear);

        spnProgressTypeYear = view.findViewById(R.id.spnProgressTypeYear);
        ArrayAdapter<String> adapterProgressType = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.progressType_values));

        spnProgressTypeYear.setAdapter(adapterProgressType);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isEditing = sharedYearViewModel.getIsEdit();

        if (sharedYearViewModel.getSelectedItem().getValue() != null && isEditing) {
            isEdit = sharedYearViewModel.getIsEdit();

            TodoYear todoYear = sharedYearViewModel.getSelectedItem().getValue();
            etxtYearTodo.setText(todoYear.getName());
            etxtMlNoteYear.setText(todoYear.getNote());

            String currentStatusStr = "";
            String goalStr = "";
            String progressType = todoYear.getProgressType();
            if(progressType.equals("Quantity")) {
                progressTypeIdx = 0;
                currentStatusStr = String.valueOf((int)todoYear.currentStatus);
                goalStr = String.valueOf((int)todoYear.goal);
            } else if (progressType.equals("Satisfaction")) {
                progressTypeIdx = 1;
                currentStatusStr = String.valueOf(todoYear.currentStatus);
                goalStr = "100%";
            } else {
                progressTypeIdx = 2;
                currentStatusStr = String.valueOf(todoYear.currentStatus);
                goalStr = String.valueOf(todoYear.goal);
            }
            spnProgressTypeYear.setSelection(progressTypeIdx);

            etxtCurrentStatusYear.setText(currentStatusStr);
            etxtGoalYear.setText(goalStr);
        } else if (!isEdit) {
            etxtYearTodo.setText("");
            etxtMlNoteYear.setText("");
            etxtCurrentStatusYear.setText("");
            etxtGoalYear.setText("");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        isEdit = false;
        sharedYearViewModel.setIsEdit(false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //this sharedYearViewModel should be the same one in YearlyFragment sharedYearViewModel
        sharedYearViewModel = new ViewModelProvider(requireActivity()).get(SharedYearViewModel.class);


        spnProgressTypeYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProgressType = spnProgressTypeYear.getItemAtPosition(position).toString();
                try {
                    if (isDarkAppTheme) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                } catch(Exception e) {
                }

                if (selectedProgressType.equals("Satisfaction")) {
                    etxtGoalYear.setText("100%");
                    etxtGoalYear.setFocusableInTouchMode(false);
                } else {
                    etxtGoalYear.setFocusableInTouchMode(true);
                    if("100%".equals(etxtGoalYear.getText().toString())) {
                        etxtGoalYear.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No Action
            }
        });


        // validate before save or edit
        btnSaveYear.setOnClickListener(v -> {
            boolean validationOk = true;

            String todoName = etxtYearTodo.getText().toString();
            String todoNote = etxtMlNoteYear.getText().toString();

            String strCurrentStatus = etxtCurrentStatusYear.getText().toString();
            String strGoal = etxtGoalYear.getText().toString();

            String validationMessage = "";

            try {
                if (TextUtils.isEmpty(todoName)) {
                    validationMessage = "You must enter todo name\r\n" + validationMessage;
                    etxtYearTodo.requestFocus();
                    validationOk = false;
                }

                if (TextUtils.isEmpty(strCurrentStatus)) {
                    validationMessage = "You must enter current status\r\n" + validationMessage;
                    etxtCurrentStatusYear.requestFocus();
                    validationOk = false;
                }

                if (TextUtils.isEmpty(strGoal)) {
                    validationMessage = "You must enter a goal\r\n" + validationMessage;
                    etxtGoalYear.requestFocus();
                    validationOk = false;
                }

                if(validationOk) {
                    if (isEdit) {
                        TodoYear updateTodoYear = sharedYearViewModel.getSelectedItem().getValue();
                        updateTodoYear.setName(todoName);
                        updateTodoYear.setNote(todoNote);

                        updateTodoYear.setProgressType(selectedProgressType);
                        updateTodoYear.setCurrentStatus(Double.parseDouble(strCurrentStatus));

                        String progressType = updateTodoYear.getProgressType();
                        if(progressType.equals("Quantity")) {
                            updateTodoYear.setGoal(Double.parseDouble(strGoal));
                        } else if (progressType.equals("Satisfaction")) {
                            updateTodoYear.setGoal(100.00);
                        } else {
                            updateTodoYear.setGoal(Double.parseDouble(strGoal));
                        }

                        TodoYearViewModel.update(updateTodoYear);
                        sharedYearViewModel.setIsEdit(false);
                    } else {
                        TodoYear todoYear = new TodoYear();
                        todoYear.setName(todoName);
                        todoYear.setNote(todoNote);

                        todoYear.setProgressType(selectedProgressType);
                        todoYear.setCurrentStatus(Double.parseDouble(strCurrentStatus));

                        String progressType = todoYear.getProgressType();
                        if(progressType.equals("Quantity")) {
                            todoYear.setGoal(Double.parseDouble(strGoal));
                        } else if (progressType.equals("Satisfaction")) {
                            todoYear.setGoal(100.00);
                        } else {
                            todoYear.setGoal(Double.parseDouble(strGoal));
                        }

                        TodoYearViewModel.insert(todoYear);
                    }
                    etxtYearTodo.setText("");
                    etxtMlNoteYear.setText("");
                    etxtCurrentStatusYear.setText("");
                    etxtGoalYear.setText("");

                    if (this.isVisible()) this.dismiss(); // close bottom sheet dialog
                } else {
                    Snackbar.make(v, validationMessage, Snackbar.LENGTH_LONG).setAction("Error", null).show();
                }
            } catch (Exception e) {
                Snackbar.make(v, e.toString(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
            }
        });


        btnResetYear.setOnClickListener(v -> {
            etxtYearTodo.setText("");
            etxtMlNoteYear.setText("");
            etxtCurrentStatusYear.setText("");
            etxtGoalYear.setText("");
        });

    }
}