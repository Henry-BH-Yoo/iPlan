package ca.on.conec.iplan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.AppDatabase;
import ca.on.conec.iplan.entity.User;

public class DailyFragment extends Fragment {

    public DailyFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily, container, false);

        // FloatingActionButton
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add action will be created", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Create db
        AppDatabase db = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        User user = new User();

        user.setFirstName("Henry");
        user.setLastName("Yoo");

        new Thread(() -> {
            Log.i("INFO", "INSERT Obj");
            db.userDao().insertAll(user);
        }).start();

        return v;
    }
}