package com.app.distance.AddStops;


import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.distance.Activities.AddPlacesActivity;
import com.app.distance.Activities.MainActivity;
import com.app.distance.Adapters.ListViewAdapter;
import com.app.distance.Adapters.RouteAdapter;
import com.app.distance.CommonDataArea.CommonFunctions;
import com.app.distance.Model.PlaceModel;
import com.app.distance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddStopsFragment extends Fragment {
    private AVLoadingIndicatorView avi;
    Button AddPlaceBtn;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String URLstring = "http://192.168.25.197/myweb/routes.php";
    EditText uuid;
    Button add_bus, save, discard, btnSpinner;
    List<PlaceModel> routeList;
    Dialog myDialog, busDialog;
    TextView route, Dest, soure, username;
    ListView listView;
    PlaceModel placeModel;
    Spinner bus_spinner;
    String strtext;
    //TODO: Step 4 of 4: Finally call getTag() on the view.
    // This viewHolder will have all required values.
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            placeModel = routeList.get(position);
            Toast.makeText(getActivity(), "You Clicked: " + position, Toast.LENGTH_SHORT).show();
            route.setText("Route no: " + placeModel.getRoute_no());
            soure.setText(placeModel.getSource().getSource());
            Dest.setText(placeModel.getDestination().getDestination());


            ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), placeModel.getWay_points());

            listView.setAdapter(listViewAdapter);
            myDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "frag_onCreateView");
        View view = inflater.inflate(R.layout.add_place_frag, container, false);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "frag_onCreate");
        /********************************************************************************
         internet check
         ********************************************************************************/
        if (!CommonFunctions.isOnline(getActivity())) try {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
            alertdialog.setCancelable(false);
            alertdialog.setTitle("Info");
            alertdialog.setMessage("Internet not available,check your internet connectivity and try again");
            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            alertdialog.show();
        } catch (Exception e) {

        }
        /********************************************************************************/

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "frag_onActivityCreated");
        AddPlaceBtn = (Button) getView().findViewById(R.id.addnewstops);
        username = (TextView) getView().findViewById(R.id.user_name1);
        recyclerView = (RecyclerView) getView().findViewById(R.id.route_list);
        recyclerView.setHasFixedSize(true);
        routeList = new ArrayList<>();
        add_bus = (Button) getView().findViewById(R.id.addnewbus);
        String indicator = "indicator";
        avi = (AVLoadingIndicatorView) getView().findViewById(R.id.avi);
        avi.setIndicator(indicator);
        mYDialog();
        busDialog();
        //username.setText(strtext);
        mLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(mLayoutManager);
        //add place activity button
        AddPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPlacesActivity.class);
                startActivity(intent);
            }
        });
        //add bus dialog button
        add_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busDialog.show();

            }
        });
        //add bus save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UUID = uuid.getText().toString().trim();
                if (!TextUtils.isEmpty(UUID)) {
                    Log.d("d", "fd");
                    //to server
                    busDialog.dismiss();
                } else
                    Toast.makeText(getActivity(), "Please enter a valid UUID", Toast.LENGTH_SHORT).show();
            }
        });
        //add bus discard button
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busDialog.dismiss();
            }
        });

        spinnerLoader();
        //spinner selector button
        btnSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Selected: " + bus_spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            }
        });
        fetchingJSON();


    }

    private void spinnerLoader() {
        List<String> categories = new ArrayList<String>();
        categories.add("UUID1");
        categories.add("UUID2");
        categories.add("UUID3");
        categories.add("UUID4");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bus_spinner.setAdapter(dataAdapter);
    }

    private void mYDialog() {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.route_dialouge);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        route = (TextView) myDialog.findViewById(R.id.routeNo);
        Dest = (TextView) myDialog.findViewById(R.id.dest_dialog);
        soure = (TextView) myDialog.findViewById(R.id.source_dialog);
        btnSpinner = (Button) myDialog.findViewById(R.id.spinner_submit);
        bus_spinner = (Spinner) myDialog.findViewById(R.id.bus_spinner);
        route.setSelected(true);
        Dest.setSelected(true);
        soure.setSelected(true);
        listView = (ListView) myDialog.findViewById(R.id.list);
    }

    private void busDialog() {
        busDialog = new Dialog(getActivity());
        busDialog.setContentView(R.layout.add_bus_dialog);
        uuid = (EditText) busDialog.findViewById(R.id.edttxt_add_bus);
        save = (Button) busDialog.findViewById(R.id.btn_save);
        discard = (Button) busDialog.findViewById(R.id.btn_discard);

    }

    private void fetchingJSON() {
        avi.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject obj = array.getJSONObject(j);
                                PlaceModel placeModel = new PlaceModel();
                                String routeno = obj.getString("Routeno");
//                                if (routeno != null)
                                    placeModel.setRoute_no(routeno);
                                JSONObject source = obj.getJSONObject("source");
                                String sName = source.getString("PlaceName");
                                String Slat = source.getString("Lattitude");
                                String Slong = source.getString("Longitude");
//                                if (sName != null && Slat != null && Slong != null) {
                                    PlaceModel.Source s = placeModel.new Source();
                                    s.setSource(sName);
                                    s.setsLattitude(Slat);
                                    s.setsLongitude(Slong);
                                    placeModel.setSource(s);
//                                }
                                JSONObject dest = obj.getJSONObject("destination");
                                String dname = dest.getString("PlaceName");
                                String dlat = dest.getString("Lattitude");
                                String dlong = dest.getString("Longitude");
//                                if (dname != null && dlat != null && dlong != null) {
                                    PlaceModel.Destination d = placeModel.new Destination();
                                    d.setDestination(dname);
                                    d.setdLattitude(dlat);
                                    d.setdLongitude(dlong);
                                    placeModel.setDestination(d);
//                                }

                                JSONArray jsonArray = obj.getJSONArray("Waypoints");
                                List<PlaceModel.Waypoints> way_list = new ArrayList<PlaceModel.Waypoints>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String name = jsonArray.getJSONObject(i).getString("Place Name");
                                    String wlat = jsonArray.getJSONObject(i).getString("Lattitude");
                                    String wlong = jsonArray.getJSONObject(i).getString("Longitude");
//                                    if (name != null && wlat != null && wlong != null) {
                                        PlaceModel.Waypoints w = placeModel.new Waypoints();
                                        w.setWaypoint(name);
                                        w.setwLattitude(wlat);
                                        w.setwLongitude(wlong);
                                        way_list.add(w);
//                                    }
                                }
                                placeModel.setWay_points(way_list);
                                Log.d("TAG", sName + Slat + Slong);
                                routeList.add(placeModel);

                            }

                            RouteAdapter routeAdapter = new RouteAdapter(getActivity(), routeList);

                            avi.hide();
                            recyclerView.setAdapter(routeAdapter);
//                            DefaultItemAnimator animator = new DefaultItemAnimator() {
//                                @Override
//                                public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
//                                    return true;
//                                }
//                            };
//                            recyclerView.setItemAnimator(animator);
                            //TODO: Step 1 of 4: Create and set OnItemClickListener to the adapter.
                            routeAdapter.setOnItemClickListener(onClickListener);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "frag_onDestroy");
        //FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "frag_onStop");
    }
}
