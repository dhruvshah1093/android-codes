package com.homie.india;

/**
 * Created by dhruv on 3/21/16.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homie.india.es.R;
import com.squareup.picasso.Picasso;

import java.util.List;
//set view listner
public class CustomListViewAdapter extends ArrayAdapter<list> implements AdapterView.OnItemClickListener{

    private Activity activity;

    public CustomListViewAdapter(Activity activity, int resource, List<list> list) {
        super(activity, resource, list);
        this.activity = activity;

    }


    //creates a customise listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        list Lst = getItem(position);

        holder.user_name.setText(Lst.getName());
        holder.listing_address.setText(Lst.getAddress());
        holder.listing_rent.setText(Lst.getRent());
        holder.id.setText(Lst.getId());

        Picasso.with(activity).load(Lst.getImageUrl()).into(holder.listing_images);
        Picasso.with(activity).load(Lst.getUser_dp()).into(holder.user_dp);

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private static class ViewHolder {
        private TextView listing_address;
        private TextView listing_rent;
        private ImageView listing_images;
        private TextView user_name;
        private ImageView user_dp;
        private TextView id;

        public ViewHolder(View v) {
            listing_images = (ImageView) v.findViewById(R.id.listing_images);
            listing_address = (TextView) v.findViewById(R.id.listing_address);
           id = (TextView) v.findViewById(R.id.id);
            listing_rent = (TextView) v.findViewById(R.id.listing_rent);
            user_name = (TextView) v.findViewById(R.id.user_name);
            user_dp = (ImageView) v.findViewById(R.id.user_dp);

        }
    }

}