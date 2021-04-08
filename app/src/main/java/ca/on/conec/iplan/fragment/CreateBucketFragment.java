package ca.on.conec.iplan.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.iplan.R;

public class CreateBucketFragment extends Fragment {

    private String[] typeList = {"Quantity", "Satisfaction" , "Money"};

    private Spinner spnProgressType;


    private String selectedProgressType;


    public CreateBucketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_bucket, container, false);

        spnProgressType = v.findViewById(R.id.spnProgressType);
        ArrayAdapter<String> adapterProgressType = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, typeList);
        spnProgressType.setAdapter(adapterProgressType);


        spnProgressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(R.color.gray_900);
                selectedProgressType = spnProgressType.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return v;
    }
}