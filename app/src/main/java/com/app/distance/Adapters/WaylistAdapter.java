package com.app.distance.Adapters;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.distance.Model.PlaceModel;
import com.app.distance.Model.WaypointModel;
import com.app.distance.R;

import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

public class WaylistAdapter extends ArrayAdapter<WaypointModel> {
    private AppCompatActivity context;
    public List<WaypointModel> waypoints;
    GradientDrawable color;
    GradientDrawable gd;
    public WaylistAdapter(FragmentActivity context, List<WaypointModel> way) {
        super(context,R.layout.way_item,way);
        this.context = (AppCompatActivity) context;
        this.waypoints = way;
        this.color=color;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.way_item, null, true);
        int[] colors = new int[2];
        colors[0] = getRandomColor();
        colors[1] = getRandomColor();


        gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gd.setGradientRadius(300f);
        gd.setCornerRadius(10f);


        TextView textViewName = (TextView) listViewItem.findViewById(R.id.way_item);

//        card.setBackground(gd);
//        card.setRadius(5f);
        WaypointModel waypoint=waypoints.get(position);
        //PlaceModel placeModel = waypoints.get(position);
        textViewName.setText(waypoint.getWaypoints());


        return listViewItem;
    }
    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
