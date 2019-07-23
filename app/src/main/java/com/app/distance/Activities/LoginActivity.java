package com.app.distance.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.distance.ApiServices.RequestHandler;
import com.app.distance.CommonDataArea.CommonFunctions;
import com.app.distance.CommonDataArea.SharedPrefManager;
import com.app.distance.CommonDataArea.URLs;
import com.app.distance.Model.User;
import com.app.distance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;
import android.content.DialogInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    TextView signupTv, signupTv2, signup;
    Button btnLogin;
    EditText username, password;
    private boolean login = false;
    boolean user = false;
    boolean pass = false;
    Animation slideUpAnimation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /********************************************************************************
                                        internet check
        ********************************************************************************/
        if (!CommonFunctions.isOnline(this)) try {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
            alertdialog.setCancelable(false);
            alertdialog.setTitle("Info");
            alertdialog.setMessage("Internet not available,check your internet connectivity and try again");
            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertdialog.show();
        } catch (Exception e) {

        }
        /********************************************************************************/


        initviews();
        /***********************************************************************************/
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initviews() {
        signupTv = (TextView) findViewById(R.id.signupTv);
        signupTv2 = (TextView) findViewById(R.id.signupTv2);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        mView = new CatLoadingView();
        mView.setCanceledOnTouchOutside(false);
        signup = (TextView) findViewById(R.id.signup);

    }

    @Override
    protected void onResume() {
        super.onResume();
        validateviews();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!login) {
                    signupTv.setVisibility(View.GONE);
                    //signupTv2.setVisibility(View.GONE);
                    username.startAnimation(slideUpAnimation);

                    username.setVisibility(View.VISIBLE);
                    password.startAnimation(slideUpAnimation);
                    password.setVisibility(View.VISIBLE);
                    btnLogin.setText("Login");
                    login = true;

                } else {
                    if (validateviews()) {
                        //login code goes here
                        userLogin();

                    } else {
                        if (!user) {
                            username.setError("invalid");
                        }
                        if (!pass) {
                            password.setError("invalid");
                        }
                        Toast.makeText(LoginActivity.this, "Invalid format", Toast.LENGTH_SHORT).show();

                    }


                }
            }


        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(signup, "sign_up_tr");
                pairs[1] = new Pair<View, String>(btnLogin, "btn");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

    }

    private void userLogin() {

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("username"),
                                userJson.getString("pas")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();

    }

    public boolean validateviews() {
        final String email = username.getText().toString().trim();
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", String.valueOf(charSequence));
                if (charSequence.toString().matches(emailPattern) && charSequence.length() > 0) {
                    username.setTextColor(Color.parseColor("#75e900"));
                    user = true;
                } else {
                    username.setTextColor(Color.parseColor("#ea313f"));
                    user = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pwd = password.getText().toString().trim();
                if (pwd.length() > 5) {
                    password.setTextColor(Color.parseColor("#75e900"));
                    pass = true;
                } else {
                    password.setTextColor(Color.parseColor("#ea313f"));
                    pass = false;
                }

            }
        });

        if (user && pass) {
            return true;
        } else {
            return false;
        }
    }
}
