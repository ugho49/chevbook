package com.chevbook.chevbookapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chevbook.chevbookapp.Activity.FullscreenPictureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Ugho on 20/04/2014.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<String> mImages = new ArrayList<String>();
    private Activity activity;
    private Context context;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    //Constructor
    private ImagePagerAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public ImagePagerAdapter(Activity activity, ArrayList<String> mImages) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(7, 0, 7, 0);

        // int padding = context.getResources().getDimensionPixelSize(
        //       R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);

        //imageView.setImageResource(mImages[position]);

        imageLoader.displayImage(mImages.get(position), imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentImage = new Intent(activity, FullscreenPictureActivity.class);
                intentImage.putExtra("position", position);
                intentImage.putExtra("ListeURL", mImages);
                activity.startActivity(intentImage);
            }
        });

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
