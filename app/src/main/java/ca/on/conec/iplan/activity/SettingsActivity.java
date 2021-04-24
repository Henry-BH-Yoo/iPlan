package ca.on.conec.iplan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import ca.on.conec.iplan.R;

public class SettingsActivity extends AppCompatActivity {

    private boolean isDarkAppTheme;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    /**
     * When the click the back button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean rtnValue = true;
        switch(item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                break;
            default :
                rtnValue =  super.onOptionsItemSelected(item);
                break;
        }

        return rtnValue;
    }

}