package com.chevbook.chevbookapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chevbook.chevbookapp.Activity.DeposerModifierAnnonceActivity;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ugho on 05/03/14.
 */
public class ListViewMyAnnoncesAdapter extends BaseAdapter {

    private List<Annonce> list;
    private final Context _c;
    private final Activity activity;
    private User mUser;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewMyAnnoncesAdapter(Activity activity, Context context) {

        this.activity = activity;
        this._c = context;
        this.mUser = new User(context);
        this.list = null;
    }

    public ListViewMyAnnoncesAdapter(Activity activity, Context context, List<Annonce> listMyAnnonces) {

        this.activity = activity;
        this._c = context;
        this.mUser = new User(context);
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

    private void deleteAnnonce(final int pos)
    {
        new AsyncTask<Void, Void, Boolean>() {

            String ErreurLoginTask = "Erreur";
            String AfficherJSON = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;
                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL(activity.getResources().getString(R.string.URL_SERVEUR) + activity.getResources().getString(R.string.URL_SERVEUR_DELETE_ANNONCE));
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
                    jsonParam.put("email", mUser.getEmail());
                    jsonParam.put("password", mUser.getPasswordSha1());
                    jsonParam.put("id_annonce", list.get(pos).getId_annonce());
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

                        //AfficherJSON = sb.toString();

                        JSONObject jsonObject = new JSONObject(sb.toString());

                        Boolean fav = jsonObject.getBoolean("supprReussi");

                        if(fav){
                            return true;
                        } else {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (MalformedURLException e){
                    ErreurLoginTask = ErreurLoginTask + "URL";
                    return false; //Erreur URL
                } catch (SocketTimeoutException e) {
                    ErreurLoginTask = ErreurLoginTask + "Temps trop long";
                    return false; //Temps trop long
                } catch (IOException e) {
                    ErreurLoginTask = ErreurLoginTask + "Connexion internet lente ou inexistante";
                    return false; //Pas de connexion internet
                } catch (JSONException e) {
                    ErreurLoginTask = ErreurLoginTask + "Problème de JSON";
                    return false; //Erreur JSON
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(final Boolean success) {

                if (success)
                {
                    list.remove(pos);
                    notifyDataChang();
                }
                else {
                    Toast.makeText(_c, "Erreur de suppression de l'annonce", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    public void setList(List<Annonce> list) {
        this.list = list;
    }

    public void notifyDataChang() {
        this.notifyDataSetChanged();
    }
}
