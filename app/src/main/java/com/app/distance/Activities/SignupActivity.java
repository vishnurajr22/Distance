package com.app.distance.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText name, username, password, confirmpassword;
    Button signup;
    FirebaseAuth mAuth;
    String Name, Username, Password, Confirmpassword;
    DatabaseReference databaseReference;
     AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        initviews();

    }

    private void initviews() {
        avi = findViewById(R.id.avi);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password_);
        confirmpassword = (EditText) findViewById(R.id.confirm_password);
        signup = (Button) findViewById(R.id.btn_signup);
    }


    ProgressDialog myDialog;

    @Override
    protected void onResume() {
        super.onResume();
        avi.hide();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp();
                signup.setVisibility(View.GONE);


            }
        });


    }


    private void signUp() {
        Name = name.getText().toString().trim();
        Username = username.getText().toString().trim();
        Password = password.getText().toString().trim();
        Confirmpassword = confirmpassword.getText().toString().trim();
        boolean n, u, p, c;
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (Name.isEmpty()) {
            name.setError("Invalid");
            n = false;
        } else
            n = true;

        if (Username.matches(emailPattern)) {
            u = true;
        } else {
            username.setError("invalid format");
            u = false;
        }
        if (Password.length() >= 6) {
            p = true;
        } else {
            password.setError("minimum length of Password should be 6");
            password.requestFocus();
            p = false;
        }
        if (Password.equals(Confirmpassword))
            c = true;
        else {
            confirmpassword.setError("Passwords do not match");
            c = false;
        }

        if (n && u && p && c) {
            class RegisterUser extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", Username);
                    params.put("name", Name);
                    params.put("password", Password);


                    //returing the response
                    return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //displaying the progress bar while user registers on the server
                    avi.show();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion
                    avi.hide();

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
                                    userJson.getString("user_name"),
                                    userJson.getString("pass")
                            );

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();

                            signup.setVisibility(View.VISIBLE);
                            name.setText("");
                            username.setText("");
                            password.setText("");
                            confirmpassword.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //executing the async task
            RegisterUser ru = new RegisterUser();
            ru.execute();
        }
        }
    }


