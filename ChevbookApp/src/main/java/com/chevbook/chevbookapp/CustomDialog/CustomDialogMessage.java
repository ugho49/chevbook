package com.chevbook.chevbookapp.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chevbook.chevbookapp.CustomsView.CircularImageView;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ugho on 25/02/14.
 */
public class CustomDialogMessage {

    private static TextView mTextViewCustomDialogMessageUserName;
    private static TextView mTextViewCustomDialogMessageDate;
    private static TextView mTextViewCustomDialogMessageFromTo;
    private static CircularImageView mImageViewCustomDialogMessageUserReceiver;
    private static EditText mEditTextCustomDialogMessageTitle;
    private static EditText mEditTextCustomDialogMessageMessage;
    private static Button mButtonCustomDialogMessageSent;
    private static Button mButtonCustomDialogMessageReply;

    private Activity mActivity;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private AlertDialog dialog;

    public CustomDialogMessage() {
    }

    public CustomDialogMessage(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void createDialog() {
        View custom_view_message = mActivity.getLayoutInflater().inflate(R.layout.custom_dialog_message, null);

        mEditTextCustomDialogMessageTitle = (EditText)custom_view_message.findViewById(R.id.editTextCustomDialogMessageTitle);
        mEditTextCustomDialogMessageMessage = (EditText)custom_view_message.findViewById(R.id.editTextCustomDialogMessageMessage);
        mTextViewCustomDialogMessageUserName = (TextView)custom_view_message.findViewById(R.id.textViewCustomDialogMessageUserName);
        mTextViewCustomDialogMessageDate = (TextView)custom_view_message.findViewById(R.id.textViewCustomDialogMessageDate);
        mTextViewCustomDialogMessageFromTo = (TextView) custom_view_message.findViewById(R.id.textViewCustomDialogMessageFromTo);
        mImageViewCustomDialogMessageUserReceiver = (CircularImageView)custom_view_message.findViewById(R.id.imageViewCustomDialogMessageUserReceiver);
        mButtonCustomDialogMessageSent = (Button)custom_view_message.findViewById(R.id.buttonCustomDialogMessageSent);
        mButtonCustomDialogMessageReply = (Button)custom_view_message.findViewById(R.id.buttonCustomDialogMessageReply);

        dialog = new AlertDialog.Builder(mActivity)
                .setView(custom_view_message)
                .setTitle("Message")
                .setCancelable(false)
                .setNegativeButton(mActivity.getResources().getString(R.string.btn_return), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();
    }

    public void showDialog() {
        mButtonCustomDialogMessageReply.setOnClickListener(clickListener);
        mButtonCustomDialogMessageSent.setOnClickListener(clickListener);

        dialog.show();
    }

    public void resetDialog() {
        //dialog.show();
        mEditTextCustomDialogMessageMessage.setText("");
        mEditTextCustomDialogMessageTitle.setText("");
        mImageViewCustomDialogMessageUserReceiver.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_user_drag_drop));
        mTextViewCustomDialogMessageUserName.setText("");
        mTextViewCustomDialogMessageDate.setText("");
    }

    public void instantiateDialog(String url_image, String username, String date, String title, String message) {
        //mImageViewCustomDialogMessageUserReceiver.setImageDrawable();

        imageLoader.displayImage(url_image, mImageViewCustomDialogMessageUserReceiver);
        mTextViewCustomDialogMessageUserName.setText(username);
        mTextViewCustomDialogMessageDate.setText(date);
        mEditTextCustomDialogMessageTitle.setText(title);
        mEditTextCustomDialogMessageMessage.setText(message);

    }

    public void lookMessage()
    {
        mButtonCustomDialogMessageSent.setVisibility(View.GONE);
        mEditTextCustomDialogMessageMessage.setEnabled(false);
        mEditTextCustomDialogMessageTitle.setEnabled(false);
        mEditTextCustomDialogMessageTitle.setMaxLines(20);
        mEditTextCustomDialogMessageTitle.setSingleLine(false);
        mEditTextCustomDialogMessageTitle.setBackgroundColor(mActivity.getResources().getColor(R.color.white_transparent));
        mEditTextCustomDialogMessageMessage.setBackgroundColor(mActivity.getResources().getColor(R.color.white_transparent));
    }

    public void showButtonReply(Boolean b)
    {
        if(b)
        {
            mButtonCustomDialogMessageReply.setVisibility(View.VISIBLE);
            mTextViewCustomDialogMessageFromTo.setText("De :");
        }
        else {
            mButtonCustomDialogMessageReply.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.buttonCustomDialogMessageSent:
                    Toast.makeText(mActivity.getApplicationContext(), "Envoi en cours...", Toast.LENGTH_SHORT).show();

                    //todo

                    dialog.cancel();
                    break;

                case R.id.buttonCustomDialogMessageReply:
                    //Toast.makeText(mActivity.getApplicationContext(), "Réponse...", Toast.LENGTH_SHORT).show();

                    dialog.cancel();

                    CustomDialogMessage dm = new CustomDialogMessage(mActivity);
                    dm.createDialog();
                    dm.resetDialog();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                    String currentDateAndTime = sdf.format(new Date());

                    dm.setDateMessage(currentDateAndTime);
                    dm.setUserName("testName");
                    dm.setImageUserMessage("https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-prn2/t1/1546110_10202187125312508_9323923_n.jpg");
                    dm.showDialog();
                    break;

            }
        }
    };

    public void setUserName(String userName)
    {
        mTextViewCustomDialogMessageUserName.setText(userName);
    }

    public void setDateMessage(String date)
    {
        mTextViewCustomDialogMessageDate.setText(date);
    }

    public void setImageUserMessage(String url_image)
    {
        imageLoader.displayImage(url_image, mImageViewCustomDialogMessageUserReceiver);
    }

    public void setTitleMessage(String title)
    {
        mEditTextCustomDialogMessageTitle.setText(title);
    }

    public void setMessage(String message)
    {
        mEditTextCustomDialogMessageMessage.setText(message);
    }

}
