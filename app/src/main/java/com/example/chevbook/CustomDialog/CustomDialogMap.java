package com.example.chevbook.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Toast;

import com.example.chevbook.Class.ConnectionDetector;
import com.example.chevbook.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ugho on 21/02/14.
 */
public class CustomDialogMap {

    private String adresse = "";
    private GoogleMap googleMap;

    private Activity mActivity;
    private ConnectionDetector connectionDetector;

    private AlertDialog dialog;

    public CustomDialogMap(Activity _a) {
        this.mActivity = _a;
        connectionDetector = new ConnectionDetector(mActivity.getApplicationContext());
    }

    public CustomDialogMap(Activity _a, String adresse) {
        this.mActivity = _a;
        connectionDetector = new ConnectionDetector(mActivity.getApplicationContext());
        this.adresse = adresse;
    }

    public void createDialog() {
        View custom_view_change_password = mActivity.getLayoutInflater().inflate(R.layout.custom_dialog_detail_appartement_map, null);
        googleMap = ((MapFragment) mActivity.getFragmentManager().findFragmentById(R.id.mapCustomDialog)).getMap();

        dialog = new AlertDialog.Builder(mActivity)
                .setView(custom_view_change_password)
                .setNegativeButton(mActivity.getResources().getString(R.string.btn_return), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        //dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .create();

    }

    public void showDialog()
    {
        googleMap.setMyLocationEnabled(true); // Showing Current Location
        googleMap.getUiSettings().setZoomControlsEnabled(true); // Zooming Buttons
        googleMap.getUiSettings().setZoomGesturesEnabled(true); // Zooming Functionality
        googleMap.getUiSettings().setCompassEnabled(true); // Compass Functionality
        googleMap.getUiSettings().setMyLocationButtonEnabled(true); // My Location Button
        googleMap.getUiSettings().setRotateGesturesEnabled(true); // Map Rotate Gesture

        dialog.show();

        if(adresse != "")
        {
            if(connectionDetector.isConnectingToInternet())
            {
                showMarkerClient();
            }
            else {
                Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.no_internet_connexion), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMarkerClient()
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        LatLng position = getPositionByAdresse(adresse);

        googleMap.addMarker(new MarkerOptions().position(position).title(mActivity.getResources().getString(R.string.appartements)).snippet(adresse).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        builder.include(position);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), mActivity.getResources().getDisplayMetrics().widthPixels, mActivity.getResources().getDisplayMetrics().heightPixels, 200));
    }

    private LatLng getPositionByAdresse(String adresse)
    {
        LatLng pos = null;

        Geocoder fwdGeocoder = new Geocoder(mActivity.getApplicationContext(), Locale.FRANCE);
        List<Address> locations = null;

        try {
            locations = fwdGeocoder.getFromLocationName(adresse, 10);
        }
        catch (IOException e) {
            // Pbs geocoder adresse client
        }

        if ((locations == null) || (locations.isEmpty()))
        {
            // Adresse client inconnue
            Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.map_error_found_adress), Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Réussite du geofencing
            pos = new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude());
        }

        return pos;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
