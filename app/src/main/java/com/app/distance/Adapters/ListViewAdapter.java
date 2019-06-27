package com.app.distance.Adapters;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.distance.Model.PlaceModel;
import com.app.distance.R;

import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

public class ListViewAdapter extends ArrayAdapter<PlaceModel.Waypoints> {
    private AppCompatActivity context;
    List<PlaceModel.Waypoints> waypoints;
    GradientDrawable color;
    GradientDrawable gd;
    public ListViewAdapter(FragmentActivity context, List<PlaceModel.Waypoints> artists) {
        super(context,R.layout.waypoint_list_item,artists);
        this.context = (AppCompatActivity) context;
        this.waypoints = artists;
        this.color=color;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.waypoint_list_item, null, true);
        int[] colors = new int[2];
        colors[0] = getRandomColor();
        colors[1] = getRandomColor();


        gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gd.setGradientRadius(300f);
        gd.setCornerRadius(10f);

        //listViewItem.setBackground(gd);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.way_point_item);
        CardView card=(CardView)listViewItem.findViewById(R.id.listviewCard) ;
//        card.setBackground(gd);
//        card.setRadius(5f);
        PlaceModel.Waypoints waypoint=waypoints.get(position);
        //PlaceModel placeModel = waypoints.get(position);
        textViewName.setText(waypoint.getWaypoint());


        return listViewItem;
    }
    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
