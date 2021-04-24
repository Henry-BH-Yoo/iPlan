package ca.on.conec.iplan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.on.conec.iplan.NotificationService;
import ca.on.conec.iplan.R;
import ca.on.conec.iplan.fragment.BucketListFragment;
import ca.on.conec.iplan.fragment.DailyFragment;
import ca.on.conec.iplan.fragment.MonthlyFragment;
import ca.on.conec.iplan.fragment.YearlyFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;

    private SharedPreferences sharedPref;
    private boolean isDarkAppTheme;
    private ActionBar actionBar;

    private int menuNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setAppTheme();

        // Start Local Notification
        startService(new Intent(getApplicationContext(), NotificationService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
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
                        menuNumber = 1;
                        fragment = new DailyFragment();
                        actionBar.setTitle("Day Plan");
                        break;
                    case R.id.bottom_tab_monthly:
                        menuNumber = 2;
                        fragment = new MonthlyFragment();
                        actionBar.setTitle("Month Plan");
                        break;
                    case R.id.bottom_tab_year:
                        menuNumber = 3;
                        fragment = new YearlyFragment();
                        actionBar.setTitle("Year Plan");
                        break;
                    case R.id.bottom_tab_life:
                        menuNumber = 4;
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


    public void changeMenu(int menuNumber) {
        switch(menuNumber) {
            case 1 :
                bottomNavigationView.setSelectedItemId(R.id.bottom_tab_daily);
                break;
            case 2 :
                bottomNavigationView.setSelectedItemId(R.id.bottom_tab_monthly);
                break;
            case 3 :
                bottomNavigationView.setSelectedItemId(R.id.bottom_tab_year);
                break;
            case 4 :
                bottomNavigationView.setSelectedItemId(R.id.bottom_tab_life);
                break;
        }
    }

    /**
     * App theme setting
     */
    private void setAppTheme() {
        isDarkAppTheme = sharedPref.getBoolean("darkAppTheme", false);

        if(isDarkAppTheme) {
            setTheme(R.style.Theme_IPlan_Dark);
        } else {
            setTheme(R.style.Theme_IPlan);
        }
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

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("menuNumber", menuNumber);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        menuNumber = sharedPref.getInt("menuNumber" , 0);

        changeMenu(menuNumber);
    }

    // For Local Notification Service
    @Override
    protected void onStop() {

        // MOVE to onCreate?
//        startService(new Intent(getApplicationContext(), NotificationService.class));
        super.onStop();
    }
}