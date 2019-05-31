package com.app.distance.Activities;

import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.distance.Adapters.ListViewAdapter;
import com.app.distance.CommonDataArea.CommonFunctions;
import com.app.distance.Model.PlaceModel;
import com.app.distance.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class AddPlacesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button source, destination, waypoints;
    TextView sourceText, destinationText;
    ListView listView;
    private int SOURCE = 1;
    private int DESTINATION = 2;
    private int WAY_POINTS = 3;
    private GoogleApiClient mGoogleApiClient;
    String Splace, Slatitude, Slongitude, Dplace, Dlat, Dlong, Wplace, Wlat, Wlong;

    Animation slideUpAnimation, slideDownAnimation;
    RelativeLayout rsource,rdest;
    List<PlaceModel> places=new ArrayList<>();
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        Log.d("TAG", "onCreate");
        initviews();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Places");


        //CommonFunctions.addPlacesActivity = this;
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

//        String indicator=getIntent().getStringExtra("indicator");
//        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
//        avi.setIndicator(indicator);
//        avi.show();
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_animation);



    }

    private void initviews() {
        source = (Button) findViewById(R.id.source);
        destination = (Button) findViewById(R.id.destination);
        waypoints = (Button) findViewById(R.id.waypoints);
        sourceText = (TextView) findViewById(R.id.sourceT);
        destinationText = (TextView) findViewById(R.id.destinationT);
        rsource=(RelativeLayout)findViewById(R.id.rsource);
        rdest=(RelativeLayout)findViewById(R.id.rdest);
        listView=(ListView)findViewById(R.id.waypoints_lv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume");
        //Log.d("TAG", String.valueOf(CommonFunctions.internetcheck()));
    source.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {

            startActivityForResult(builder.build(AddPlacesActivity.this), SOURCE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }
    });
    destination.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            PlacePicker.IntentBuilder builder1 = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder1.build(AddPlacesActivity.this), DESTINATION);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    });
    waypoints.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PlacePicker.IntentBuilder builder2 = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder2.build(AddPlacesActivity.this), WAY_POINTS);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    });
        loadListview();
//    databaseReference.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            places.clear();
//            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                PlaceModel placeModel=postSnapshot.getValue(PlaceModel.class);
//                places.add(placeModel);
//            }
//
//            ListViewAdapter listViewAdapter=new ListViewAdapter(AddPlacesActivity.this,places);
//            listView.setAdapter(listViewAdapter);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(sourceText, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "onStart");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.d("TAG", "onStop");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOURCE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                Splace = String.format("%s", place.getName());
                Slatitude = String.valueOf(place.getLatLng().latitude);
                Slongitude = String.valueOf(place.getLatLng().longitude);
                sourceText.setText(Splace);
                sourceText.startAnimation(slideDownAnimation);
            }
        } else if (requestCode == DESTINATION) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                Dplace = String.format("%s", place.getName());
                Dlat = String.valueOf(place.getLatLng().latitude);
                Dlong = String.valueOf(place.getLatLng().longitude);
                destinationText.setText(Dplace);
                destinationText.startAnimation(slideDownAnimation);
            }
        } else if (requestCode == WAY_POINTS) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                Wplace = String.format("%s", place.getName());
                Wlat = String.valueOf(place.getLatLng().latitude);
                Wlong = String.valueOf(place.getLatLng().longitude);
                if(Dplace==null || Splace==null){
                    Toast.makeText(this,"Please select Soure and Destination before selecting Waypoints",Toast.LENGTH_SHORT).show();
                }
                else {
                    places = new ArrayList<>();
                    String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    addPlaces(uuid);


                }
            }
        }
    }

    private void loadListview() {
        Query query=FirebaseDatabase.getInstance().getReference("Places")
                .orderByChild("source").startAt("Lotus PG for Gents").endAt("Lotus PG for Gents");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                places.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                PlaceModel placeModel=postSnapshot.getValue(PlaceModel.class);
                places.add(placeModel);
            }

            ListViewAdapter listViewAdapter=new ListViewAdapter(AddPlacesActivity.this,places);
            listView.setAdapter(listViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addPlaces(String uuid) {

        if(Splace!=null&&Slatitude!=null&&Slongitude!=null&&
        Dplace!=null&&Dlat!=null&&Dlong!=null&&Wplace!=null&&Wlat!=null&&Wlong!=null&&uuid!=null){
            try{
            String id = databaseReference.push().getKey();
            PlaceModel placeModel=new PlaceModel(id,Splace,Slatitude,Slongitude,
                    Dplace,Dlat,Dlong,Wplace,Wlat,Wlong,uuid);
            databaseReference.child(id).setValue(placeModel);
            Toast.makeText(this, "Place added", Toast.LENGTH_LONG).show();
        }catch (Exception e){
                Log.e("TAG",e.getMessage());
            }
        }
    }


}
