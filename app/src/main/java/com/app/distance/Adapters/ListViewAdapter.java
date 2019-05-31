package com.app.distance.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.distance.Model.PlaceModel;
import com.app.distance.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ListViewAdapter extends ArrayAdapter<PlaceModel> {
    private AppCompatActivity context;
    List<PlaceModel> waypoints;

    public ListViewAdapter(AppCompatActivity context, List<PlaceModel> artists) {
        super(context,R.layout.waypoint_list_item,artists);
        this.context = context;
        this.waypoints = artists;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.waypoint_list_item, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.way_point_item);


        PlaceModel artist = waypoints.get(position);
        textViewName.setText(artist.getWaypoint());


        return listViewItem;
    }
}
