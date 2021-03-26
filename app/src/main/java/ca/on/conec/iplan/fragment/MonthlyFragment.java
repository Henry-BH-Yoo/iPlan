package ca.on.conec.iplan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.AppDatabase;
import ca.on.conec.iplan.entity.User;

public class MonthlyFragment extends Fragment {
    public MonthlyFragment() {
        // Required empty public constructor
    }


    TextView userText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_monthly, container, false);

        userText = v.findViewById(R.id.userText);

        //디비생성
        AppDatabase db = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        new Thread(() -> {
            Log.i("INFO" , db.userDao().getAll().toString());
            userText.setText(db.userDao().getAll().toString());
        }).start();


        return v;
    }
}