package ca.on.conec.iplan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.database.AppDatabase;
import ca.on.conec.iplan.entity.User;


public class BucketListFragment extends Fragment {

    public BucketListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bucket_list, container, false);

        //디비생성
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