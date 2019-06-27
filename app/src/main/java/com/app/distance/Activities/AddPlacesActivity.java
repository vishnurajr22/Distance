package com.app.distance.Activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.distance.Adapters.WaylistAdapter;
import com.app.distance.Model.WaypointModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddPlacesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ImageButton source, destination3, waypoints;
    Button save;
    TextView sourceText, destinationText;
    ListView listView;
    private int SOURCE = 1;
    private int DESTINATION = 2;
    private int WAY_POINTS = 3;
    private GoogleApiClient mGoogleApiClient;
    String Splace, Slatitude, Slongitude, Dplace, Dlat, Dlong, Wplace, Wlat, Wlong;
    EditText route_no;
    int randomAndroidColor;
    Animation slideUpAnimation, slideDownAnimation;
    RelativeLayout rsource, rdest;
    List<PlaceModel> places = new ArrayList<>();
    List<PlaceModel> waypointslist = new ArrayList<>();
    LinearLayout container;
    ListView lvWaypoints;
    static DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    GradientDrawable gd;
    PlaceModel placeModel=new PlaceModel() ;
    static  List<PlaceModel.Waypoints> way_list = new ArrayList<PlaceModel.Waypoints>();
    static int i=0;
    LinearLayout.LayoutParams layoutParams;
    public static List<WaypointModel> w_List=w_List =new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        Log.d("TAG", "onCreate");
        initviews();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Places");
        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
//        randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//         gd = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM, androidColors);


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
        source = (ImageButton) findViewById(R.id.source);
        destination3 = (ImageButton) findViewById(R.id.dstbtn);
        waypoints = (ImageButton) findViewById(R.id.waypoints);
        sourceText = (TextView) findViewById(R.id.sourceT);
        destinationText = (TextView) findViewById(R.id.destinationT);
        lvWaypoints=(ListView)findViewById(R.id.waylist) ;


        //container=(LinearLayout)findViewById(R.id.con);
        route_no = (EditText) findViewById(R.id.route_no);
        save=(Button)findViewById(R.id.save);

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
        destination3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder2 = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder2.build(AddPlacesActivity.this), DESTINATION);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Splace!=null&&Dplace!=null&&Wplace!=null){
                    placeModel.setWay_points(way_list);
                    toJson(placeModel);}
                else {
                    Toast.makeText(getApplicationContext(),"fill the fields",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //loadListview();
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            places.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                //Log.d("TAG",postSnapshot.toString());

                Log.d("TAG", String.valueOf(postSnapshot.child("Route_no").getValue()));


            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
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
                if(w_List!=null){
                    w_List.clear();
                }
                Splace = String.format("%s", place.getName());
                Slatitude = String.valueOf(place.getLatLng().latitude);
                Slongitude = String.valueOf(place.getLatLng().longitude);
                sourceText.setText(Splace);
                sourceText.startAnimation(slideDownAnimation);

                PlaceModel.Source source = placeModel.new Source();

                source.setSource(String.format("%s", place.getName()));
                source.setsLattitude(String.valueOf(place.getLatLng().latitude));
                source.setsLongitude(String.valueOf(place.getLatLng().longitude));
                placeModel.setSource(source);
            }
        } else if (requestCode == DESTINATION) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                Dplace = String.format("%s", place.getName());
                Dlat = String.valueOf(place.getLatLng().latitude);
                Dlong = String.valueOf(place.getLatLng().longitude);
                destinationText.setText(Dplace);
                destinationText.startAnimation(slideDownAnimation);

                PlaceModel.Destination destination = placeModel.new Destination();
                destination.setDestination(Dplace);
                destination.setdLattitude(Dlat);
                destination.setdLongitude(Dlong);
                placeModel.setDestination(destination);
            }
        } else if (requestCode == WAY_POINTS) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                Wplace = String.format("%s", place.getName());
                Wlat = String.valueOf(place.getLatLng().latitude);
                Wlong = String.valueOf(place.getLatLng().longitude);
                if (Dplace == null || Splace == null || route_no == null) {
                    Toast.makeText(this, "Please select Soure and Destination before selecting Waypoints", Toast.LENGTH_SHORT).show();
                } else {
                    places = new ArrayList<>();
                    String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    //addPlaces(uuid);


                    placeModel.setRoute_no(route_no.getText().toString());


                    PlaceModel.Waypoints way = placeModel.new Waypoints();
                    way.setWaypoint(Wplace);
                    way.setwLattitude(Wlat);
                    way.setwLongitude(Wlong);

                    way_list.add(way);
                    WaypointModel waypointModel=new WaypointModel();
                    waypointModel.setWaypoints(Wplace);

                    w_List.add(waypointModel);


//                    TextView view = new TextView(this);
//                    view.setPadding(10,10,0,10);
//                    view.setBackground(getResources().getDrawable(
//                            R.drawable.textview_container));
//                    view.setText(++i+Wplace);
//                    container.addView(view, layoutParams);



                    //toJson(placeModel);


                }

            }

        }
        WaylistAdapter waylistAdapter=new WaylistAdapter(this, w_List);

        lvWaypoints.setAdapter(waylistAdapter);
    }

    public static String toJson(PlaceModel placeModel) {

        try {
            //main leg
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("Route_no",placeModel.getRoute_no());
            //source leg
            JSONObject sources=new JSONObject();
            sources.put("Place_Name",placeModel.getSource().getSource());
            sources.put("Lattitude",placeModel.getSource().getsLattitude());
            sources.put("Longitude",placeModel.getSource().getsLongitude());
            //destination leg
            JSONObject destination=new JSONObject();
            destination.put("Place_Name",placeModel.getDestination().getDestination());
            destination.put("Lattitude",placeModel.getDestination().getdLattitude());
            destination.put("Longitude",placeModel.getDestination().getdLongitude());
            //adding source and destination object to main object in json
            jsonObject.put("source",sources);
            jsonObject.put("destination",destination);
            //waypoints array
            JSONObject w=new JSONObject();

            JSONArray jsonArr = new JSONArray();

            for (PlaceModel.Waypoints waypoints : placeModel.getWay_points() ) {
                JSONObject wayobject = new JSONObject();
                wayobject.put("Place_Name", waypoints.getWaypoint());
                wayobject.put("Lattitude", waypoints.getwLattitude());
                wayobject.put("Longitude", waypoints.getwLongitude());
                jsonArr.put(wayobject);
            }
            jsonObject.put("Waypoints", jsonArr);
            //jsonObject.put("way_points",w);
           Log.d("TAG",jsonObject.toString());
            //////////////////////////////////////////////////////////////////////

                try {
                    String id = databaseReference.push().getKey();

            /*PlaceModel placeModel=new PlaceModel(id,Splace,Slatitude,Slongitude,
                    Dplace,Dlat,Dlong,Wplace,Wlat,Wlong,uuid);*/
                    databaseReference.child(id).setValue(String.valueOf(jsonObject));
//                    Toast.makeText(AddPlacesActivity.this, "Place added", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }

            //////////////////////////////////////////////////////////////////////
            way_list.clear();

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private void loadListview() {
        Query query = FirebaseDatabase.getInstance().getReference("Places")
                .orderByChild("source").startAt("Lotus PG for Gents").endAt("Lotus PG for Gents");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                places.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PlaceModel placeModel = postSnapshot.getValue(PlaceModel.class);
                    places.add(placeModel);
                }

                /*ListViewAdapter listViewAdapter = new ListViewAdapter(AddPlacesActivity.this, places);
                listView.setAdapter(listViewAdapter);*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
