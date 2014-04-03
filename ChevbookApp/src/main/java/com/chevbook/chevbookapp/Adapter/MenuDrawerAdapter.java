package com.chevbook.chevbookapp.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomsView.CircularImageView;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Ugho on 27/01/14.
 */
public class MenuDrawerAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] mTitle;
    //int[] mIcon;
    TypedArray mIcon;
    LayoutInflater inflater;
    User currentUser;

    public MenuDrawerAdapter(Context context, String[] title,TypedArray icon) {
        this.context = context;
        this.mTitle = title;
        this.mIcon = icon;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView txtTitle;
        ImageView Icon;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.navigation_drawer_item, parent,
                false);

        if(position == 0)
        {
            /*Modele vmodele = new Modele();
            User currentUser = vmodele.getCurrentUser();*/

            currentUser = new User(context);
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);

            CircularImageView IconCircular;
            txtTitle = (TextView) itemView.findViewById(R.id.title_navigation_drawer_profile);
            IconCircular = (CircularImageView) itemView.findViewById(R.id.icon_navigation_drawer_profile);
            LinearLayout item_profile = (LinearLayout) itemView.findViewById(R.id.item_drawer_profile);
            LinearLayout item = (LinearLayout) itemView.findViewById(R.id.item_drawer);

            item_profile.setVisibility(View.VISIBLE);
            item.setVisibility(View.GONE);

            String url = currentUser.getUrlProfilPicture();
            String name = currentUser.getFirstName() + " " + currentUser.getLastName();

            txtTitle.setText(name);

            if(url.length() > 0)
            {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(url, IconCircular);
            }
            else
            {
                IconCircular.setImageResource(mIcon.getResourceId(position, -1));
            }
        }
        else
        {
            txtTitle = (TextView) itemView.findViewById(R.id.title_navigation_drawer);
            Icon = (ImageView) itemView.findViewById(R.id.icon_navigation_drawer);
            txtTitle.setText(mTitle[position]);
            Icon.setImageResource(mIcon.getResourceId(position, -1));
        }

        return itemView;
    }
}
