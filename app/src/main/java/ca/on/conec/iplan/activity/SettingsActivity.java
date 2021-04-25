/**
 * FileName : SettingActivity.java
 * Purpose
 * Revision History :
 *      2021.03.25 Henry    Create Setting Activity
 *      2021.04.23 Henry    Add theme function
 *      2021.04.23 Henry    Add save password function
 *      2021.04.24 Henry    Connect Loging Page
 */

package ca.on.conec.iplan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.material.snackbar.Snackbar;

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

            SwitchPreference swPref = (SwitchPreference) findPreference("pwCheck");
            EditTextPreference edtPref = (EditTextPreference) findPreference("edtPassword");

            if(swPref.isChecked()) {
                edtPref.setVisible(true);

                if(!"".equals(edtPref.getText())) {
                    edtPref.setTitle("****");
                } else {
                    edtPref.setTitle("");
                }
            } else {
                edtPref.setVisible(false);
                edtPref.setText("");
            }


            swPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    if(Boolean.parseBoolean(newValue.toString())) {
                        edtPref.setVisible(true);
                    } else {
                        edtPref.setVisible(false);
                        edtPref.setText("");
                    }

                    return true;
                }
            });

            edtPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newStr = newValue.toString();

                    if(newStr.length() < 4) {
                        return false;
                    } else {
                        preference.setTitle("****");
                        return true;
                    }
                }
            });

            edtPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                }
            });

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