/**
 * FileName : BottomSheetBucket.java
 * Revision History :
 *          2021 04 23  Henry   Create
 */

package ca.on.conec.iplan.fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.MonthlyPlan;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;
import ca.on.conec.iplan.viewmodel.MonthlyPlanViewModel;

public class BottomSheetMonthFragment extends BottomSheetDialogFragment {

    private EditText edtMPlanTitle , edtMPlanStart , edtMPlanEnd;
    private TimePicker mStartPicker , mEndPicker;
    private Button resetBtn , saveBtn;

    private String mPlanDate = "";
    private String mPlanStart = "";
    private String mPlanEnd = "";
    private int mPlanId = 0;
    private boolean updateYn;
    private MonthlyPlan mPlan;

    private MonthlyPlanViewModel mPlanViewModel;

    public BottomSheetMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_month, container, false);

        updateYn = false;
        mPlanDate = this.getArguments().getString("mPlanDate");
        try {
            mPlanId = this.getArguments().getInt("mPlanId");
        } catch (Exception ne) {
            mPlanId = 0;
        }


        mPlanViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(MonthlyPlanViewModel.class);


        // Assign ID
        edtMPlanTitle = v.findViewById(R.id.edtMPlanTitle);
        edtMPlanStart = v.findViewById(R.id.edtMPlanStart);
        edtMPlanEnd = v.findViewById(R.id.edtMPlanEnd);

//        mStartPicker = v.findViewById(R.id.mStartPicker);
//        mEndPicker = v.findViewById(R.id.mEndPicker);
        resetBtn = v.findViewById(R.id.resetBtn);
        saveBtn = v.findViewById(R.id.saveBtn);

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm");
        String currentTime = simpleDate.format(mDate);

        edtMPlanStart.setHint(currentTime);
        edtMPlanEnd.setHint(currentTime);


        // Setting 24 hour time picker
//        mStartPicker.setIs24HourView(true);
//        mEndPicker.setIs24HourView(true);


        /*
        mStartPicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            if (hourOfDay < 10) {
                String newHour = "0" + hourOfDay;
                mPlanStart = newHour + ":" + minute;
            } else {
                mPlanStart = hourOfDay + ":" + minute;
            }
        });

        mEndPicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            if (hourOfDay < 10) {
                String newHour = "0" + hourOfDay;
                mPlanEnd = newHour + ":" + minute;
            } else {
                mPlanEnd = hourOfDay + ":" + minute;
            }
        });
        */


        edtMPlanStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String strHour = "";
                        String strMin = "";

                        if(hourOfDay < 10) {
                            strHour = "0" + hourOfDay;
                        } else {
                            strHour = hourOfDay + "";
                        }

                        if(minute < 10) {
                            strMin = "0" + minute;
                        } else {
                            strMin = minute + "";
                        }


                        // Display in EditText
                        edtMPlanStart.setText(strHour + ":" + strMin);
                        mPlanStart = strHour + ":" + strMin;
                    }

                }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();

            }
        });


        edtMPlanEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Display in EditText

                        String strHour = "";
                        String strMin = "";

                        if(hourOfDay < 10) {
                            strHour = "0" + hourOfDay;
                        } else {
                            strHour = hourOfDay + "";
                        }

                        if(minute < 10) {
                            strMin = "0" + minute;
                        } else {
                            strMin = minute + "";
                        }

                        edtMPlanEnd.setText(strHour + ":" + strMin);
                        mPlanEnd = strHour + ":" + strMin;
                    }

                }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();

            }
        });

        if(mPlanId != 0 ) {
            mPlanViewModel.find(mPlanId).observe(getViewLifecycleOwner(),  mPlan ->{
                this.mPlan = mPlan;


                edtMPlanTitle.setText(mPlan.getMPlanTitle());
                edtMPlanStart.setText(mPlan.getMPlanStart());
                edtMPlanEnd.setText(mPlan.getMPlanEnd());
                mPlanDate = mPlan.mPlanDate;
                mPlanStart = mPlan.getMPlanStart();
                mPlanEnd = mPlan.getMPlanEnd();

                updateYn = true;

            });
        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation

                boolean validationOk = true;
                // Reset Edit Text
                String mPlanTitle = edtMPlanTitle.getText().toString();

                String validationMessage = "";

                try {

                    if ("".equals(mPlanEnd)) {
                        validationMessage = "You must enter end time.\r\n" + validationMessage;
                        edtMPlanStart.requestFocus();
                        validationOk = false;
                    }

                    if ("".equals(mPlanStart)) {
                        validationMessage = "You must enter start time.\r\n" + validationMessage;
                        edtMPlanEnd.requestFocus();
                        validationOk = false;
                    }

                    if ("".equals(mPlanTitle)) {
                        validationMessage = "You must enter your plan.\r\n" + validationMessage;
                        edtMPlanTitle.requestFocus();
                        validationOk = false;
                    }

                    if (validationOk) {

                        if (updateYn) {
                            // update
                            mPlan.setMPlanTitle(mPlanTitle);
                            mPlan.setMPlanDate(mPlanDate);
                            mPlan.setMPlanStart(mPlanStart);
                            mPlan.setMPlanEnd(mPlanEnd);
                            MonthlyPlanViewModel.update(mPlan);

                        } else {

                            mPlan = new MonthlyPlan();
                            mPlan.setMPlanTitle(mPlanTitle);
                            mPlan.setMPlanDate(mPlanDate);
                            mPlan.setMPlanStart(mPlanStart);
                            mPlan.setMPlanEnd(mPlanEnd);

                            // Insert
                            MonthlyPlanViewModel.insert(mPlan);
                        }


                        clearMonthPlanForm();
                        dismiss();

                    } else {
                        Snackbar.make(v, validationMessage, Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMonthPlanForm();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        edtMPlanTitle.setText("");
        edtMPlanStart.setText("");
        edtMPlanEnd.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void clearMonthPlanForm() {
        // Reset Edit Text
        edtMPlanTitle.setText("");
        edtMPlanStart.setText("");
        edtMPlanEnd.setText("");
    }



}