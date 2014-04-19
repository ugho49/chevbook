package com.chevbook.chevbookapp.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chevbook.chevbookapp.Class.ConnectionDetector;
import com.chevbook.chevbookapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void createDialog() {
        View custom_view_map = mActivity.getLayoutInflater().inflate(R.layout.custom_dialog_detail_annonce_map, null);
        googleMap = ((MapFragment) mActivity.getFragmentManager().findFragmentById(R.id.mapCustomDialog)).getMap();

        dialog = new AlertDialog.Builder(mActivity)
                .setView(custom_view_map)
                .setCancelable(false)
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
                //showMarkerClient();
                LoadMarkerTask mLoadMarkerTask = new LoadMarkerTask();
                mLoadMarkerTask.execute((Void) null);
            }
            else {
                Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.no_internet_connexion), Toast.LENGTH_SHORT).show();
                Log.d("chevbook_CD_MAP", "no internet connexion");
            }
        }
    }

    public class LoadMarkerTask extends AsyncTask<Void, Void, Boolean> {

        LatLng position;
        Geocoder fwdGeocoder;
        List<Address> locations;

        Double lon = new Double(0);
        Double lat = new Double(0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            position = null;
            //fwdGeocoder = new Geocoder(mActivity.getApplicationContext(), Locale.FRANCE);
            fwdGeocoder = new Geocoder(mActivity.getApplicationContext());
            locations = new ArrayList<Address>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                locations = fwdGeocoder.getFromLocationName(adresse, 10);

                if (locations.size() > 0)
                {
                    position = new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude());
                    return true;
                }
                else {
                    String vad = URLEncoder.encode(adresse, "UTF-8");
                    String vurl = "http://maps.google.com/maps/api/geocode/json?address=" + vad + "&sensor=true";

                    HttpURLConnection urlConnection = null;
                    StringBuilder sb = new StringBuilder();

                    URL url = new URL(vurl);
                    urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setConnectTimeout(5000);
                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                    // Création objet jsonn clé valeur
                    JSONObject jsonParam = new JSONObject();
                    // Exemple Clé valeur utiles à notre application
                    /*jsonParam.put("debut", debut);
                    jsonParam.put("fin", fin);*/
                    out.write(jsonParam.toString());
                    out.flush();
                    out.close();

                    // récupération du serveur
                    int HttpResult = urlConnection.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        JSONObject jsonObject = new JSONObject(sb.toString());

                        lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        if(lon != 0 && lat != 0){
                            position = new LatLng(lat,lon);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            catch (IOException e) {
                return false;
            } catch (JSONException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if(position != null){
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            //.title("")
                            //.snippet(adresse)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                    CameraPosition cameraPosition = new CameraPosition
                            .Builder()
                            .target(position)
                            .zoom(13)
                            .build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else {
                    Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.map_error_found_adress), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.map_error_found_adress), Toast.LENGTH_SHORT).show();
                Log.d("chevbook_CD_MAP", "Error geocoder adress");
            }
        }
    }
}
