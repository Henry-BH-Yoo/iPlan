package ca.on.conec.iplan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.entity.TodoYear;
import ca.on.conec.iplan.viewmodel.SharedViewModel;
import ca.on.conec.iplan.viewmodel.SharedYearViewModel;
import ca.on.conec.iplan.viewmodel.TodoViewModel;
import ca.on.conec.iplan.viewmodel.TodoYearViewModel;

// When clicks the Floating Plus btn, this fragment will pop up
public class BottomSheetYearFragment extends BottomSheetDialogFragment {

    private EditText etxtYearTodo, etxtMlNoteYear;
    private Button btnResetYear, btnSaveYear;


    //todo add progress xml
    private TextView txtProgressYear, txtNoteYear;


    private SharedYearViewModel sharedYearViewModel;
    private boolean isEdit;

    public BottomSheetYearFragment() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_year, container, false);

        etxtYearTodo = view.findViewById(R.id.etxtYearTodo);
        etxtMlNoteYear = view.findViewById(R.id.etxtMlNoteYear);

        txtProgressYear = view.findViewById(R.id.txtProgressYear);
        txtNoteYear = view.findViewById(R.id.txtNoteYear);

        btnResetYear = view.findViewById(R.id.btnResetYear);
        btnSaveYear = view.findViewById(R.id.btnSaveYear);



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
        } else if (!isEdit) {
            etxtYearTodo.setText("");
            etxtMlNoteYear.setText("");
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


//        //this sharedYearViewModel should be the same one in YearlyFragment sharedYearViewModel
        sharedYearViewModel = new ViewModelProvider(requireActivity()).get(SharedYearViewModel.class);

        // validate before save or edit
        btnSaveYear.setOnClickListener(v -> {
            boolean validationOk = true;

            String todoName = etxtYearTodo.getText().toString();
            String todoNote = etxtMlNoteYear.getText().toString();

            String validationMessage = "";

            try {
                if (TextUtils.isEmpty(todoName)) {
                    validationMessage = "You must enter todo name\r\n" + validationMessage;
                    etxtYearTodo.requestFocus();
                    validationOk = false;
                }

                if(validationOk) {
                    if (isEdit) {
                        TodoYear updateTodoYear = sharedYearViewModel.getSelectedItem().getValue();
                        updateTodoYear.setName(todoName);
                        updateTodoYear.setNote(todoNote);

                        TodoYearViewModel.update(updateTodoYear);
                        sharedYearViewModel.setIsEdit(false);
                    } else {
                        TodoYear todoYear = new TodoYear();
                        todoYear.setName(todoName);
                        todoYear.setNote(todoNote);

                        TodoYearViewModel.insert(todoYear);
                    }
                    etxtYearTodo.setText("");
                    etxtMlNoteYear.setText("");

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
        });

    }
}