package com.example.chevbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chevbook.Class.Appartement;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Ugho on 24/02/14.
 */
public class ListViewAppartementAdapter extends BaseAdapter {

    private final List<Appartement> list;
    private final Context _c;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewAppartementAdapter(Context context) {

        this._c = context;
        this.list = null;
    }

    public ListViewAppartementAdapter(Context context, List<Appartement> ListAppartements) {

        this._c = context;
        this.list = ListAppartements;

    }

    private static class ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView quartier;
        public TextView description;
        public TextView price;
        public TextView date;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return list.size();
        return 8;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_item_appartement, null);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.imageViewRowItemAppartementPicture);
            holder.title = (TextView) v.findViewById(R.id.textViewRowItemAppartementName);
            holder.quartier = (TextView) v.findViewById(R.id.textViewRowItemAppartementQuartier);
            holder.description = (TextView) v.findViewById(R.id.textViewRowItemAppartementDescription);
            holder.price = (TextView) v.findViewById(R.id.textViewRowItemAppartementPrice);
            holder.date = (TextView) v.findViewById(R.id.textViewRowItemAppartementDate);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        String url_image = "https://d2jlgwxg7tqbdp.cloudfront.net/housing_images/Spain_Valencia_Alicante/For-Sale_Apartments/New-build-apartment-for-sale-in-Villamartin-Spain-2656292-7777850/photo/scaled_135766251666300089518.jpg";
        imageLoader.displayImage(url_image, holder.image);

        /*imageLoader.displayImage(list.get(position).getURLimage(), holder.image);
        holder.title.setText(list.get(position).getTitre());
        holder.quartier.setText(list.get(position).getTitre());
        holder.description.setText(list.get(position).getTitre());
        holder.price.setText(list.get(position).getTitre());
        holder.date.setText(list.get(position).getTitre());*/

        return v;
    }
}
