package ca.on.conec.iplan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.fragment.BucketListFragment;
import ca.on.conec.iplan.fragment.DailyFragment;
import ca.on.conec.iplan.fragment.MonthlyFragment;
import ca.on.conec.iplan.fragment.YearlyFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragment = new DailyFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_area , fragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.bottom_tab_daily:
                        fragment = new DailyFragment();
                        break;
                    case R.id.bottom_tab_monthly:
                        fragment = new MonthlyFragment();
                        break;
                    case R.id.bottom_tab_year:
                        fragment = new YearlyFragment();
                        break;
                    case R.id.bottom_tab_life:
                        fragment = new BucketListFragment();
                        break;
                }

                if(fragment != null) {

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_area, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);

                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);

        return true;
    }

    /**
     * Option Item Clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.top_setting :

                startActivity(new Intent(getApplicationContext() , SettingsActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}