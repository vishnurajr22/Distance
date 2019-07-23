package com.app.distance.AddStops;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.distance.Adapters.MultiSelectionSpinner;
import com.app.distance.Adapters.RouteAdapter;
import com.app.distance.CommonDataArea.CommonFunctions;
import com.app.distance.CommonDataArea.SharedPrefManager;
import com.app.distance.CommonDataArea.URLs;
import com.app.distance.Model.PlaceModel;
import com.app.distance.Model.User;
import com.app.distance.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class AddStopsFragment extends Fragment {
    List<PlaceModel.Bus> bus_list;
    /********************************/
    Button mOrder;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    /**********************************************/
    private AVLoadingIndicatorView avi;
    Button AddPlaceBtn;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //private String URLstring = "http://192.168.25.197/myweb/routes.php";
    EditText uuid;
    Button add_bus, save, discard, btnSpinner;
    List<PlaceModel> routeList=new ArrayList<>();
    Dialog myDialog, busDialog;
    TextView route, Dest, soure, username,uname;
    ListView listView;
    PlaceModel placeModel;
    Spinner bus_spinner;
    String strtext;
    MultiSelectionSpinner spinner;
    //TODO: Step 4 of 4: Finally call getTag() on the view.
    // This viewHolder will have all required values.
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            placeModel = routeList.get(position);
            Toast.makeText(getActivity(), "You Clicked: " + position, Toast.LENGTH_SHORT).show();
            route.setText("BUS: " + placeModel.getBusString().replace("\"", "").replace("[", "").replace("]", ""));
            soure.setText(placeModel.getSource().getSource());
            Dest.setText(placeModel.getDestination().getDestination());

            //String b=""+bus1.getUuid();
            // mItemSelected.setText(placeModel.getBusString().replace("\"", ""));
           /*String list_bus_items=placeModel.getBusString().replace("[","").replace("]","").replace("\"", "");
            listItems = list_bus_items.split(",");
            Log.d("arr", list_bus_items);*/
            ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), placeModel.getWay_points());

            listView.setAdapter(listViewAdapter);

            myDialog.show();
            spinner(position);
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


        String s = "dfwd,fsdf,dfsdf";
        String[] arr = s.split(",");
        for (int i = 0; i < arr.length; i++)
            Log.d("arr", String.valueOf(arr[i]));

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
        uname = (TextView) getView().findViewById(R.id.u_name);
        recyclerView = (RecyclerView) getView().findViewById(R.id.route_list);
        recyclerView.setHasFixedSize(true);
        routeList = new ArrayList<>();
        add_bus = (Button) getView().findViewById(R.id.addnewbus);
        String indicator = "indicator";
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        uname.setText(user.getName());
        username.setText(user.getUser_name());
        avi = (AVLoadingIndicatorView) getView().findViewById(R.id.avi1);

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

                    bus_data_to_server(UUID);

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

        //spinnerLoader();
//        spinner();
        //spinner selector button
      /*  btnSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List s = spinner.list();
                for(int i=0;i<s.size();i++)
                Toast.makeText(getContext(), "Selected: " + s.get(i), Toast.LENGTH_LONG).show();

            }
        });*/



    }

    @Override
    public void onResume() {
        super.onResume();
        avi.show();
        fetchingJSON(getActivity());
        avi.hide();
    }

    private void bus_data_to_server(String uuid) {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        String URLstring = URLs.SET_BUS + "&uuid=" +uuid + "&user=" + user.getUser_name() + "&route=" +"NIL" + "&status=" + "0000";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String r = response.trim();
                        Log.d("strrrrr", ">>" + r);
                        try {

                            JSONObject jsonObject=new JSONObject(r);
                            String error=jsonObject.getString("error");
                            if(error.equals("false")) {
                                Toast.makeText(getActivity(),"Bus added",Toast.LENGTH_SHORT).show();
                            }
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

    private void spinner(final int position) {
        // fetchbuslist();
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        String URLstring = URLs.BUS_REQ + "&username=" + user.getUser_name();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String r = response.trim();
                        Log.d("strrrrr", ">>" + r);
                        try {

                            JSONArray array = new JSONArray(r);
                            String list_bus_items = String.valueOf(array).replace("[", "").replace("]", "").replace("\"", "");
                            listItems = list_bus_items.split(",");
                            Log.d("arr", list_bus_items);
                            checkedItems = new boolean[listItems.length];
                            Arrays.fill(checkedItems, Boolean.FALSE);



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

        mItemSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Select Bus");
                String[] bs=placeModel.getBusString().replace("\"", "").replace("[", "").replace("]", "").split(",");

                for(int j=0;j<listItems.length;j++ ){
                    for( int i=0;i<bs.length;i++){
                        if(listItems[j].equals(bs[i])){
                            checkedItems[j]=true;
                        }
                    }


                }
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                            //checkedItems[position]=isChecked;
                        /*if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }
                        } else if (mUserItems.contains(position)) {
                            mUserItems.remove(position);
                        }*/
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }

                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("Select Bus");
                        }
                        try {
                            JSONObject jobject = new JSONObject();
                            JSONObject sources = new JSONObject();
                            sources.put("Place_Name", placeModel.getSource().getSource());
                            sources.put("Lattitude", placeModel.getSource().getsLattitude());
                            sources.put("Longitude", placeModel.getSource().getsLongitude());
                            //destination leg
                            JSONObject destination = new JSONObject();
                            destination.put("Place_Name", placeModel.getDestination().getDestination());
                            destination.put("Lattitude", placeModel.getDestination().getdLattitude());
                            destination.put("Longitude", placeModel.getDestination().getdLongitude());
                            //adding source and destination object to main object in json
                            jobject.put("source", sources);
                            jobject.put("destination", destination);
                            //waypoints array
                            JSONObject w = new JSONObject();

                            JSONArray jsonArr = new JSONArray();

                            for (PlaceModel.Waypoints waypoints : placeModel.getWay_points()) {
                                JSONObject wayobject = new JSONObject();
                                wayobject.put("Place_Name", waypoints.getWaypoint());
                                wayobject.put("Lattitude", waypoints.getwLattitude());
                                wayobject.put("Longitude", waypoints.getwLongitude());
                                jsonArr.put(wayobject);
                            }
                            jobject.put("Waypoints", jsonArr);
                            JSONArray bus=new JSONArray();
                            jobject.put("buses",bus);

                            String _id=placeModel.getId();
                            update_route_details(_id, String.valueOf(jobject));
                            FragmentTransaction ftr = getFragmentManager().beginTransaction();
                            ftr.detach(AddStopsFragment.this).attach(AddStopsFragment.this).commit();
                    }catch (Exception e){

                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        final List<String> busArray = new ArrayList<>();
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///////////////////////////////////////////////////////////////////////////////
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject sources = new JSONObject();
                    sources.put("Place_Name", placeModel.getSource().getSource());
                    sources.put("Lattitude", placeModel.getSource().getsLattitude());
                    sources.put("Longitude", placeModel.getSource().getsLongitude());
                    //destination leg
                    JSONObject destination = new JSONObject();
                    destination.put("Place_Name", placeModel.getDestination().getDestination());
                    destination.put("Lattitude", placeModel.getDestination().getdLattitude());
                    destination.put("Longitude", placeModel.getDestination().getdLongitude());
                    //adding source and destination object to main object in json
                    jsonObject.put("source", sources);
                    jsonObject.put("destination", destination);
                    //waypoints array
                    JSONObject w = new JSONObject();

                    JSONArray jsonArr = new JSONArray();

                    for (PlaceModel.Waypoints waypoints : placeModel.getWay_points()) {
                        JSONObject wayobject = new JSONObject();
                        wayobject.put("Place_Name", waypoints.getWaypoint());
                        wayobject.put("Lattitude", waypoints.getwLattitude());
                        wayobject.put("Longitude", waypoints.getwLongitude());
                        jsonArr.put(wayobject);
                    }
                    jsonObject.put("Waypoints", jsonArr);

                    List<PlaceModel.Bus> bus = placeModel.getBus();

                    JSONArray bus2 = new JSONArray();
                    for (int i = 0; i < mUserItems.size(); i++) {
                        // Log.d("uuids>>>>>",f.getUuid());

                        bus2.put(listItems[mUserItems.get(i)]);
                        busArray.add(listItems[mUserItems.get(i)]);
//                        update_bus_details(listItems[mUserItems.get(i)],String.valueOf(jsonObject));
                    }


                    jsonObject.put("buses", bus2);
                    String id = placeModel.getId();

                    update_route_details(id, String.valueOf(jsonObject));
                    update_bus_details(busArray, String.valueOf(jsonObject));
                    Log.d("json*****", String.valueOf(jsonObject));
                    for (int i = 0; i < checkedItems.length; i++) {
                        checkedItems[i] = false;
                        mUserItems.clear();
                        mItemSelected.setText("Select Bus");
                    }

                    myDialog.dismiss();


                    FragmentTransaction ftr = getFragmentManager().beginTransaction();
                    ftr.detach(AddStopsFragment.this).attach(AddStopsFragment.this).commit();
                    //onResume();
                } catch (Exception e) {
                }
            }
        });


    }

    private void update_bus_details(List busList, String data) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String dat = dateFormat.format(date);
        Log.d("date", dat);
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Log.d("date", user.getUser_name());
        for (int i = 0; i < busList.size(); i++) {
            String URLstring = null;
            try {
                URLstring = URLs.SET_BUS + "&uuid=" + busList.get(i) + "&user=" + user.getUser_name() + "&route=" + URLEncoder.encode(data, "UTF-8") + "&status=" + dat;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("urlstring", URLstring);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.trim());
                                String res = jsonObject.getString("error");
                                if (res.equalsIgnoreCase("false"))
                                    Toast.makeText(getActivity(), "Bus details updated", Toast.LENGTH_SHORT).show();
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

    }

    public void update_route_details(String id, String data) {
        String URLstring = null;
        try {
            URLstring = URLs.UPDATE_BUS_DETAILS + "&id=" + id + "&data=" + URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("error");
                            if (res.equalsIgnoreCase("false"))
                                Toast.makeText(getActivity(), "Bus details updated", Toast.LENGTH_SHORT).show();
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

    private void fetchbuslist() {
        String URLstring = URLs.BUS_REQ + "&username=vr21@gmail";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String r = response.trim();
                        Log.d("strrrrr", ">>" + r);
                        try {

                            JSONArray array = new JSONArray(r);
                            String list_bus_items = String.valueOf(array).replace("[", "").replace("]", "").replace("\"", "");
                            listItems = list_bus_items.split(",");
                            Log.d("arr", list_bus_items);

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

    private void spinnerLoader() {
        List<String> categories = new ArrayList<String>();
        categories.add("UUID1");
        categories.add("UUID2");
        categories.add("UUID3");
        categories.add("UUID4");

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        bus_spinner.setAdapter(dataAdapter);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            stringArrayList.add(String.valueOf(i));
        }
        spinner.setItems(stringArrayList);
    }

    private void mYDialog() {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.route_dialouge);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        route = (TextView) myDialog.findViewById(R.id.routeNo);
        Dest = (TextView) myDialog.findViewById(R.id.dest_dialog);
        soure = (TextView) myDialog.findViewById(R.id.source_dialog);
        //btnSpinner = (Button) myDialog.findViewById(R.id.spinner_submit);
        // bus_spinner = (MultiSelectionSpinner) myDialog.findViewById(R.id.bus_spinner);
        //spinner = (MultiSelectionSpinner) myDialog.findViewById(R.id.mySpinner1);
        route.setSelected(true);
        Dest.setSelected(true);
        soure.setSelected(true);
        listView = (ListView) myDialog.findViewById(R.id.list);

        mOrder = (Button) myDialog.findViewById(R.id.spinner_submit);
        mItemSelected = (TextView) myDialog.findViewById(R.id.mySpinner1);

    }


    private void busDialog() {
        busDialog = new Dialog(getActivity());
        busDialog.setContentView(R.layout.add_bus_dialog);
        uuid = (EditText) busDialog.findViewById(R.id.edttxt_add_bus);
        save = (Button) busDialog.findViewById(R.id.btn_save);
        discard = (Button) busDialog.findViewById(R.id.btn_discard);

    }

    public void fetchingJSON(Context con) {
        routeList.clear();
        User user=SharedPrefManager.getInstance(getActivity()).getUser();
        String URLstring = URLs.REQ_DATA + "&username=" + user.getUser_name();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String r = response.trim();
                        Log.d("strrrrr", ">>" + r);
                        try {

                            JSONArray array = new JSONArray(r);
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject obj = array.getJSONObject(j);
                                PlaceModel placeModel = new PlaceModel();
                                //String routeno = obj.getString("Routeno");
//                                if (routeno != null)
                                // placeModel.setBusString(routeno);
                                String id = obj.getString("id");
                                placeModel.setId(id);

                                JSONObject source = obj.getJSONObject("source");
                                String sName = source.getString("Place_Name");
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
                                String dname = dest.getString("Place_Name");
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
                                    String name = jsonArray.getJSONObject(i).getString("Place_Name");
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
                                JSONArray busArray = obj.getJSONArray("buses");
                                placeModel.setBusString(String.valueOf(busArray));
                                 bus_list = new ArrayList<PlaceModel.Bus>();
                                if (busArray.length() > 0) {
                                    for (int k = 0; k < busArray.length(); k++) {
                                        String uuid = busArray.getString(k);
                                        PlaceModel.Bus b = placeModel.new Bus();
                                        b.setUuid(uuid);
                                        bus_list.add(b);
                                    }
                                    placeModel.setBus(bus_list);
                                }

                                Log.d("TAG", sName + Slat + Slong);
                                routeList.add(placeModel);

                            }

                            RouteAdapter routeAdapter = new RouteAdapter(getActivity(), routeList);


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
        RequestQueue requestQueue = Volley.newRequestQueue(con);

        requestQueue.add(stringRequest);
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "frag_onStop");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "frag_onDestroy");
        route=null;
        Dest=null;
        soure=null;
        listView=null;
        mOrder=null;
        mItemSelected=null;
        myDialog=null;
        AddPlaceBtn=null;
        username=null;
        uname=null;
        recyclerView=null;
        add_bus=null;
        avi=null;
        save=null;

    }

}
