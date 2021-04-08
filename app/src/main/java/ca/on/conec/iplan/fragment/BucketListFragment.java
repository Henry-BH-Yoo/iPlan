package ca.on.conec.iplan.fragment;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.activity.MainActivity;
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

        FloatingActionButton addBucketBtn = v.findViewById(R.id.addBucket);

        addBucketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment createBucketFragment = new CreateBucketFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_area , createBucketFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //디비생성
        /*
        AppDatabase db = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        User user = new User();

        user.setFirstName("Henry");
        user.setLastName("Yoo");

        new Thread(() -> {
            Log.i("INFO", "INSERT Obj");
            db.userDao().insertAll(user);
        }).start();
        */
        return v;
    }
}