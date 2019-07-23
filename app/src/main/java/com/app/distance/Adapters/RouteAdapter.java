package com.app.distance.Adapters;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.distance.Model.PlaceModel;
import com.app.distance.R;


import java.util.List;
import java.util.Random;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 10/18/2017.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    GradientDrawable gd;
    private Context mCtx;
    private List<PlaceModel> placeList;

    private View.OnClickListener mOnItemClickListener;
    public RouteAdapter(Context mCtx, List<PlaceModel> placeList) {
        this.mCtx = mCtx;
        this.placeList = placeList;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.route_list, null);
        final RouteViewHolder vHolder =new RouteViewHolder(view);

//        vHolder.linear_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mCtx,"clicked"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
//            }
//        });
        return new RouteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        PlaceModel placemodel = placeList.get(position);


        int[] colors = new int[2];
        colors[0] = getRandomColor();
        colors[1] = getRandomColor();

        gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gd.setGradientRadius(300f);
        gd.setCornerRadius(12f);


        holder.textviewSource.setText(placemodel.getSource().getSource());
        holder.textviewSource.setSelected(true);
        holder.textViewShortDesc.setText(placemodel.getDestination().getDestination());
        holder.textViewShortDesc.setSelected(true);
        holder.textViewroute.setText(placemodel.getBusString().replace("\"", "").replace("[","").replace("]",""));
        holder.textViewroute.setSelected(true);

//        for (PlaceModel.Waypoints waypoints : placemodel.getWay_points() ) {
//
//            holder.textViewWay.setText(waypoints.getWaypoint());
//        }


        holder.mCardView.setBackground(gd);
        holder.mCardView.setRadius(12f);
        holder.mCardView.setMaxCardElevation(4f);
        holder.mCardView.setCardElevation(6f);
        holder.mCardView.setContentPadding(15,15,15,15);

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
    //TODO: Step 2 of 4: Assign itemClickListener to your local View.OnClickListener variable
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView textviewSource, textViewShortDesc, textViewWay, textViewroute;
        ImageView imageView;
        CardView mCardView;
        LinearLayout linear_item;
        public RouteViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            textviewSource = itemView.findViewById(R.id.src);
            textViewShortDesc = itemView.findViewById(R.id.dst);
            textViewroute = itemView.findViewById(R.id.save);
            linear_item=itemView.findViewById(R.id.linear_item);
            //TODO: Step 3 of 4: setTag() as current view holder along with
            // setOnClickListener() as your local View.OnClickListener variable.
            // You can set the same mOnItemClickListener on multiple views if required
            // and later differentiate those clicks using view's id.
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(120, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}