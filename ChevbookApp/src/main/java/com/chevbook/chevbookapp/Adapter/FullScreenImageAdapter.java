package com.chevbook.chevbookapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chevbook.chevbookapp.CustomsView.TouchImageView;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private TouchImageView imgDisplay;
    private static Button btnClose;
    private static boolean BtnCloseVisible;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.btnClose = (Button)this._activity.findViewById(R.id.buttonFullscreenPictureCloseImage);
        BtnCloseVisible = true;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.row_item_fullscreen_picture, container, false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imageViewRowItemFullScreenPicture);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(_imagePaths.get(position), imgDisplay);

        if(BtnCloseVisible)
        {
            btnClose.setVisibility(View.VISIBLE);
            /*_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            _activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);*/
        }else{
            btnClose.setVisibility(View.GONE);
            /*_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            _activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        }

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        imgDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BtnCloseVisible)
                {
                    btnClose.setVisibility(View.INVISIBLE);
                    BtnCloseVisible = false;
                   /* _activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    _activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
                    //Toast.makeText(v.getContext(), "supprimer", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    btnClose.setVisibility(View.VISIBLE);
                    BtnCloseVisible = true;
                    /*_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    _activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);*/
                    //Toast.makeText(v.getContext(), "afficher", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}