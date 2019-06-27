package com.app.distance.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.distance.CommonDataArea.CommonFunctions;
import com.app.distance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText name, username, password, confirmpassword;
    Button signup;
    FirebaseAuth mAuth;
    String Name, Username, Password, Confirmpassword;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();

        initviews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");
    }

    private void initviews() {
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp();

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
            myDialog = CommonFunctions.showprogressdialog(SignupActivity.this, "please wait");
            mAuth.createUserWithEmailAndPassword(Username, Password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userid = null;
                                if (user != null) {
                                    userid = user.getUid();
                                }

                                databaseReference.child(userid).setValue(Username);
                                myDialog.dismiss();
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "User already registerd", Toast.LENGTH_SHORT).show();
                                    myDialog.dismiss();
                                }
                            }
                        }
                    });
        }
    }

}
