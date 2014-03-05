package com.example.chevbook.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chevbook.Activity.DeposerModifierAnnonceActivity;
import com.example.chevbook.Class.Appartement;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Ugho on 05/03/14.
 */
public class ListViewMyAnnoncesAdapter extends BaseAdapter {

    private final List<Appartement> list;
    private final Context _c;
    private final Activity activity;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewMyAnnoncesAdapter(Activity activity, Context context) {

        this.activity = activity;
        this._c = context;
        this.list = null;
    }

    public ListViewMyAnnoncesAdapter(Activity activity, Context context, List<Appartement> ListAppartementsFavoris) {

        this.activity = activity;
        this._c = context;
        this.list = ListAppartementsFavoris;

    }

    private static class ViewHolder {
        public ImageView image;
        public ImageView button_delete;
        public ImageView button_edit;
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
        return 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_item_my_annonces, null);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.imageViewRowItemMyAnnouncePicture);
            holder.button_delete = (ImageView) v.findViewById(R.id.imageViewRowItemMyAnnounceDelete);
            holder.button_edit = (ImageView) v.findViewById(R.id.imageViewRowItemMyAnnounceEdit);
            holder.title = (TextView) v.findViewById(R.id.textViewRowItemMyAnnounceName);
            holder.quartier = (TextView) v.findViewById(R.id.textViewRowItemMyAnnounceQuartier);
            holder.description = (TextView) v.findViewById(R.id.textViewRowItemMyAnnounceDescription);
            holder.price = (TextView) v.findViewById(R.id.textViewRowItemMyAnnouncePrice);
            holder.date = (TextView) v.findViewById(R.id.textViewRowItemMyAnnounceDate);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        String url_image = "https://d2jlgwxg7tqbdp.cloudfront.net/housing_images/Spain_Valencia_Alicante/For-Sale_Apartments/New-build-apartment-for-sale-in-Villamartin-Spain-2656292-7777850/photo/scaled_135766251666300089518.jpg";
        imageLoader.displayImage(url_image, holder.image);

        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetailAppartement = new Intent(activity, DeposerModifierAnnonceActivity.class);
                intentDetailAppartement.putExtra("CONST", CONST_MODIFIER);
                activity.startActivity(intentDetailAppartement);
            }
        });

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setPositiveButton(activity.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //todo
                    }
                });
                adb.setNegativeButton(activity.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage("Voulez-vous supprimer l'annonce " + position + " ???");
                adb.show();
            }
        });

        return v;
    }
}
