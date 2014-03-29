package com.example.chevbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chevbook.Class.Message;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Ugho on 24/02/14.
 */
public class ListViewMessageReceivedAdapter extends BaseAdapter {

    private final List<Message> list;
    private final Context _c;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewMessageReceivedAdapter(Context context) {

        this._c = context;
        this.list = null;
    }

    public ListViewMessageReceivedAdapter(Context context, List<Message> listMessages) {

        this._c = context;
        this.list = listMessages;

    }

    private static class ViewHolder {
        public ImageView picture;
        public ImageView ico_new;
        public TextView title;
        public TextView userName;
        public TextView date;
        public TextView message;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return list.size();
        return 4;
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
            v = vi.inflate(R.layout.row_item_messages_received, null);
            holder = new ViewHolder();
            holder.picture = (ImageView) v.findViewById(R.id.imageViewRowItemMessageReceivedPictureUser);
            holder.ico_new = (ImageView) v.findViewById(R.id.imageViewRowItemMessageReceivedNew);
            holder.title = (TextView) v.findViewById(R.id.textViewRowItemMessageReceivedTitleAnnounce);
            holder.userName = (TextView) v.findViewById(R.id.textViewRowItemMessageReceivedUserName);
            holder.message = (TextView) v.findViewById(R.id.textViewRowItemMessageReceivedMessage);
            holder.date = (TextView) v.findViewById(R.id.textViewRowItemMessageReceivedDate);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        //todo

        String url_image = "https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-prn2/t1/1546110_10202187125312508_9323923_n.jpg";
        imageLoader.displayImage(url_image, holder.picture);

        /*imageLoader.displayImage(list.get(position).getUrl_image_emetteur(), holder.picture);

        holder.title.setText(list.get(position).getObjet_message());
        holder.message.setText(list.get(position).getContenu_message());
        holder.userName.setText(list.get(position).getNomPrenom_emetteur());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String DateAndTime = sdf.format(list.get(position).getDate_create_message());

        holder.date.setText(DateAndTime);

        if(list.get(position).getEst_lu())
        {
            holder.ico_new.setVisibility(View.GONE);
        }
        else {
            holder.ico_new.setVisibility(View.VISIBLE);
        }*/

        return v;
    }
}
