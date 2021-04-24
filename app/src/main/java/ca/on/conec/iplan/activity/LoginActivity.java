package ca.on.conec.iplan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ca.on.conec.iplan.R;

public class LoginActivity extends AppCompatActivity {


    private SharedPreferences sharedPref;
    private boolean isDarkAppTheme;
    private boolean isUsePassword;
    private String password;

    private EditText edtPassword;
    private Button btnExit , btnLogin;
    private ImageView imgView;
    private final int ANIMATION_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPassword = findViewById(R.id.edtPassword);
        btnExit = findViewById(R.id.btnExit);
        btnLogin = findViewById(R.id.btnLogin);
        imgView = findViewById(R.id.resultImgView);

        isUsePassword = sharedPref.getBoolean("pwCheck" , false);
        password = sharedPref.getString("edtPassword" , "");
        
        edtPassword.requestFocus();

        if(isUsePassword && !"".equals(password)) {
            // Password Check

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String enteredPwd = edtPassword.getText().toString();


                    imgView.setVisibility(View.VISIBLE);
                    imgView.setAlpha(0f);

                    if(enteredPwd.equals(password)) {
                        imgView.setImageResource(R.drawable.ic_approved);
                        imgView.animate().alpha(1f).setDuration(ANIMATION_DURATION).setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                }
                        );
                    } else {
                        imgView.setImageResource(R.drawable.ic_reject);
                        imgView.animate().alpha(1f).setDuration(ANIMATION_DURATION).setListener(
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                        imgView.animate().alpha(0f).setDuration(ANIMATION_DURATION).setListener(
                                                new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        imgView.setVisibility(View.GONE);
                                                    }
                                                }
                                        );
                                    }
                                }
                        );
                    }


                }
            });

        } else {
            // Go Main Activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });



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

}