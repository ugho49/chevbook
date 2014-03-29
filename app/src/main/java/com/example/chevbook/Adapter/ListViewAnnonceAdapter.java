package com.example.chevbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chevbook.Class.Annonce;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ugho on 24/02/14.
 */
public class ListViewAnnonceAdapter extends BaseAdapter {

    private List<Annonce> list;
    private final Context _c;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewAnnonceAdapter(Context context) {

        this._c = context;
        this.list = null;
    }

    public ListViewAnnonceAdapter(Context context, List<Annonce> listAnnonces) {

        this._c = context;
        this.list = listAnnonces;

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
        return list.size();
        //return 8;
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
            v = vi.inflate(R.layout.row_item_annonce, null);
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

        String url_image = list.get(position).getUrl_images_annonces().get(0);

        if(!url_image.equals(""))
        {
            imageLoader.displayImage(url_image, holder.image);
        }
        else {
            holder.image.setImageResource(R.drawable.logo_android_chevbook);
        }

        holder.title.setText(list.get(position).getTitre_annonce());
        holder.quartier.setText(list.get(position).getQuartier_annonce());
        holder.description.setText(list.get(position).getDescription_annonce());
        holder.price.setText(Double.toString(list.get(position).getPrix_annonce()) + "€");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String DateAndTime = sdf.format(list.get(position).getDate_create_annonce());

        holder.date.setText(DateAndTime);

        return v;
    }

    public void setList(List<Annonce> list) {
        this.list = list;
    }
}
