package com.app.distance.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.distance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

public class LoginActivity extends AppCompatActivity {
    TextView signupTv,signupTv2,signup;
    Button btnLogin;
    EditText username,password;
    private boolean login=false;
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
        initviews();
        mAuth = FirebaseAuth.getInstance();
        mAuthlistner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    //mView.dismiss();
                    Log.d("TAG" , String.valueOf(firebaseAuth.getCurrentUser().getUid()));
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    login=false;
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
       mAuth.addAuthStateListener(mAuthlistner);
    }

    private void initviews() {
        signupTv=(TextView)findViewById(R.id.signupTv);
        signupTv2=(TextView)findViewById(R.id.signupTv2);
        btnLogin=(Button) findViewById(R.id.btnlogin);
        username=(EditText) findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        mView = new CatLoadingView();
        mView.setCanceledOnTouchOutside(false);
        signup=(TextView)findViewById(R.id.signup);

    }

    @Override
    protected void onResume() {
        super.onResume();
        validateviews();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!login){
                    signupTv.setVisibility(View.GONE);
                    //signupTv2.setVisibility(View.GONE);
                    username.startAnimation(slideUpAnimation);

                    username.setVisibility(View.VISIBLE);
                    password.startAnimation(slideUpAnimation);
                    password.setVisibility(View.VISIBLE);
                    btnLogin.setText("Login");
                    login=true;

                }else{
                    if(validateviews()){
                        mView.show(getSupportFragmentManager(),"");
                        mAuth.signInWithEmailAndPassword(username.getText().toString().trim(),password.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(LoginActivity.this,"Login Problem",Toast.LENGTH_SHORT).show();

                                        }
//                                        else{
//                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                                            login=false;
//                                        }
                                    }
                                });
                    }else {
                        if(!user){
                            username.setError("invalid");
                        }
                        if(!pass){
                            password.setError("invalid");
                        }
                        Toast.makeText(LoginActivity.this,"Invalid format",Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }
    public boolean validateviews(){
        final String email=username.getText().toString().trim();
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", String.valueOf(charSequence));
                if (charSequence.toString().matches(emailPattern)&& charSequence.length()>0){
                    username.setTextColor(Color.parseColor("#75e900"));
                    user = true;
                }else{
                    username.setTextColor(Color.parseColor("#ea313f"));
                    user=false;
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
               String pwd=password.getText().toString().trim();
                if(pwd.length()>5){
                    password.setTextColor(Color.parseColor("#75e900"));
                    pass=true;
                }else{
                    password.setTextColor(Color.parseColor("#ea313f"));
                    pass=false;
                }

            }
        });

        if(user&&pass){
            return true;
        }
        else {
            return false;
        }
    }
}
