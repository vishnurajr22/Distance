package com.app.distance.Activities;


import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;


import com.app.distance.AddStops.AddStopsFragment;
import com.app.distance.R;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    Button placebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String value = getIntent().getExtras().getString("username");

        //initViews();
        //starting activity with AddplaceFragment
        loadFragment(new AddStopsFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

        }
        return true;
    }

    private void initViews() {
        //nav bar
//        BottomNavigationView bottomNavigationView=findViewById(R.id.navbar);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

    }

    private void loadFragment(Fragment fragment) {
        //selecting fragment
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListner=
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                    switch (menuItem.getItemId()){
//                        case R.id.map:
//                            loadFragment(new AddStopsFragment());
//                            break;
//                        case R.id.place:
//                            loadFragment(new AddStopsFragment());
//                            break;
//
//                    }
//                    return true;
//                }
//            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //FirebaseAuth.getInstance().signOut();
        Log.d("TAG","main_onDestroy");
    }
}
