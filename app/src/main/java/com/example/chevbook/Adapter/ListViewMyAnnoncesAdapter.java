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
import com.example.chevbook.Class.Annonce;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ugho on 05/03/14.
 */
public class ListViewMyAnnoncesAdapter extends BaseAdapter {

    private List<Annonce> list;
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

    public ListViewMyAnnoncesAdapter(Activity activity, Context context, List<Annonce> listMyAnnonces) {

        this.activity = activity;
        this._c = context;
        this.list = listMyAnnonces;

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
        return list.size();
        //return 3;
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

        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetailAppartement = new Intent(activity, DeposerModifierAnnonceActivity.class);
                intentDetailAppartement.putExtra("CONST", CONST_MODIFIER);
                intentDetailAppartement.putExtra("annonce", list.get(position));
                activity.startActivity(intentDetailAppartement);
            }
        });

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setPositiveButton(activity.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAnnonce(position);
                    }
                });
                adb.setNegativeButton(activity.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage("Voulez-vous vraiment supprimer l'annonce ?\n" +
                        "Si vous cliquez sur oui, elle n'apparaitra plus dans les annonces du site Chevbook ainsi que dans l'application.");
                adb.show();
            }
        });

        imageLoader.displayImage(list.get(position).getUrl_images_annonces().get(0), holder.image);
        holder.title.setText(list.get(position).getTitre_annonce());
        holder.quartier.setText(list.get(position).getQuartier_annonce());
        holder.description.setText(list.get(position).getDescription_annonce());
        holder.price.setText(Double.toString(list.get(position).getPrix_annonce()) + "€");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String DateAndTime = sdf.format(list.get(position).getDate_create_annonce());

        holder.date.setText(DateAndTime);

        return v;
    }

    private void deleteAnnonce(int pos)
    {
        list.remove(pos);
        this.notifyDataSetChanged();
    }
}
