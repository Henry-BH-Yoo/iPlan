/**
 * FileName : BottomSheetBucket.java
 * Revision History :
 *          2021 04 19  Henry   Create
 *          2021 04 23  Henry    modify
 */

package ca.on.conec.iplan.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;

public class BottomSheetBucketFragment extends BottomSheetDialogFragment {
    private Spinner spnProgressType;

    private EditText edtBucketTitle , edtCurrentStatus , edtGoal , edtTargetAge , spnTarget;
    private Button resetBtn , saveBtn;

    private String selectedProgressType = "Quantity";
    private BucketListViewModel bucketListViewModel;
    private static int bucketId = 0;
    private boolean updateYn;
    private BucketList bucket;

    private SharedPreferences sharedPref;
    private boolean isDarkAppTheme;


    public BottomSheetBucketFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_bucket, container, false);

        PreferenceManager.setDefaultValues(v.getContext(), R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        isDarkAppTheme = sharedPref.getBoolean("darkAppTheme", false);

        updateYn = false;
        bucketListViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(BucketListViewModel.class);


        try {
            bucketId = this.getArguments().getInt("bucketId");
        } catch (Exception ne) {
            bucketId = 0;
        }

        Log.d("DEBUG" , "bucketId >>>" + bucketId);
        // Assign ID.
        edtBucketTitle = v.findViewById(R.id.edtBucketTitle);
        edtCurrentStatus = v.findViewById(R.id.edtCurrentStatus);
        edtGoal = v.findViewById(R.id.edtGoal);
        edtTargetAge = v.findViewById(R.id.edtTargetAge);

        resetBtn = v.findViewById(R.id.resetBtn);
        saveBtn = v.findViewById(R.id.saveBtn);

        spnProgressType = v.findViewById(R.id.spnProgressType);
        ArrayAdapter<String> adapterProgressType = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.progressType_values));
        spnProgressType.setAdapter(adapterProgressType);

        spnProgressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProgressType = spnProgressType.getItemAtPosition(position).toString();

                try {
                    if (isDarkAppTheme) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                if (selectedProgressType.equals("Satisfaction")) {
                    edtGoal.setText("100%");
                    edtGoal.setFocusable(false);
                } else {
                    edtGoal.setText("");
                    edtGoal.setFocusable(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No Action
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBucketForm();
            }
        });

        if(bucketId != 0 ) {
            bucketListViewModel.find(bucketId).observe(getViewLifecycleOwner(),  bucket ->{
                this.bucket = bucket;
                int progressTypeIdx;
                String currentStatusStr = "";
                String goalStr = "";
                String progressType = bucket.getProgressType();
                if(progressType.equals("Quantity")) {
                    progressTypeIdx = 0;
                    currentStatusStr = String.valueOf((int)bucket.currentStatus);
                    goalStr = String.valueOf((int)bucket.goal);
                } else if (progressType.equals("Satisfaction")) {
                    progressTypeIdx = 1;
                    currentStatusStr = String.valueOf(String.valueOf(bucket.currentStatus));
                    goalStr = String.valueOf("100%");
                } else {
                    progressTypeIdx = 2;
                    currentStatusStr = String.valueOf(bucket.currentStatus);
                    goalStr = String.valueOf(bucket.goal);
                }

                spnProgressType.setSelection(progressTypeIdx);

                edtBucketTitle.setText(bucket.getBucketTitle());
                edtCurrentStatus.setText(currentStatusStr);
                edtGoal.setText(goalStr);
                edtTargetAge.setText(String.valueOf(bucket.getTargetAge()));

                Log.d("DEBUG" , "Goal String :" + goalStr);
                updateYn = true;

            });
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bucketId = 0;
                // Validation

                boolean validationOk = true;
                // Reset Edit Text
                String bucketTitle = edtBucketTitle.getText().toString();
                String strCurrentStatus = edtCurrentStatus.getText().toString();
                String strGoal = edtGoal.getText().toString();
                String strTargetAge = edtTargetAge.getText().toString();

                String validationMessage = "";

                if(strGoal.equals("100%")) {
                    strGoal = "100";
                }

                if("".equals(strTargetAge)) {
                    validationMessage = "You must enter target age\r\n" + validationMessage;
                    edtTargetAge.requestFocus();
                    validationOk = false;
                }

                if("".equals(strGoal)) {
                    validationMessage = "You must enter goal\r\n" + validationMessage;
                    edtGoal.requestFocus();
                    validationOk = false;
                }

                if("".equals(strCurrentStatus)) {
                    validationMessage = "You must enter current status.\r\n" + validationMessage;
                    edtCurrentStatus.requestFocus();
                    validationOk = false;
                }


                if("".equals(bucketTitle)) {
                    validationMessage = "You must enter title\r\n" + validationMessage;
                    edtBucketTitle.requestFocus();
                    validationOk = false;
                }

                if( Double.parseDouble(strCurrentStatus) > Double.parseDouble(strGoal) ) {
                    validationMessage = "Current status cannot be over Goal\r\n" + validationMessage;
                    edtCurrentStatus.requestFocus();
                    validationOk = false;
                }

                if(validationOk) {

                    if(updateYn) {
                        // update

                        bucket.setBucketTitle(bucketTitle);
                        bucket.setProgressType(selectedProgressType);
                        bucket.setCurrentStatus(Double.parseDouble(strCurrentStatus));
                        bucket.setGoal(Double.parseDouble(strGoal));
                        bucket.setTargetAge(Integer.parseInt(strTargetAge));

                        BucketListViewModel.update(bucket);
                    } else {
                        // Insert

                        bucket = new BucketList();
                        bucket.setBucketTitle(bucketTitle);
                        bucket.setProgressType(selectedProgressType);
                        bucket.setCurrentStatus(Double.parseDouble(strCurrentStatus));
                        bucket.setGoal(Double.parseDouble(strGoal));
                        bucket.setTargetAge(Integer.parseInt(strTargetAge));
                        bucket.setOrder(9999);

                        saveBucketList(bucket);
                    }


                    clearBucketForm();
                    dismiss();

                } else {
                    Snackbar.make(v, validationMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        edtBucketTitle.setText("");
        edtCurrentStatus.setText("");
        spnProgressType.setSelection(0);
        edtGoal.setText("");
        edtTargetAge.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void saveBucketList(BucketList bucket) {
        BucketListViewModel.insert(bucket);
    }

    private void clearBucketForm() {
        // Reset All field

        // Reset Edit Text
        edtBucketTitle.setText("");
        edtCurrentStatus.setText("");
        edtGoal.setText("");
        edtTargetAge.setText("");

        // Reset Spinner
        spnProgressType.setSelection(0);

    }

}