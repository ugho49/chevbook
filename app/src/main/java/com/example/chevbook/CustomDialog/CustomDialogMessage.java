package com.example.chevbook.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chevbook.CustomsView.CircularImageView;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Ugho on 25/02/14.
 */
public class CustomDialogMessage {

    private static TextView mTextViewCustomDialogMessageUserName;
    private static TextView mTextViewCustomDialogMessageDate;
    private static CircularImageView mImageViewCustomDialogMessageUserReceiver;
    private static EditText mEditTextCustomDialogMessageTitle;
    private static EditText mEditTextCustomDialogMessageMessage;
    private static Button mButtonCustomDialogMessageSent;

    private Activity mActivity;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private AlertDialog dialog;

    public CustomDialogMessage() {
    }

    public CustomDialogMessage(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void createDialog() {
        View custom_view_message = mActivity.getLayoutInflater().inflate(R.layout.custom_dialog_detail_appartement_message, null);

        mEditTextCustomDialogMessageTitle = (EditText)custom_view_message.findViewById(R.id.editTextCustomDialogMessageTitle);
        mEditTextCustomDialogMessageMessage = (EditText)custom_view_message.findViewById(R.id.editTextCustomDialogMessageMessage);
        mTextViewCustomDialogMessageUserName = (TextView)custom_view_message.findViewById(R.id.textViewCustomDialogMessageUserName);
        mTextViewCustomDialogMessageDate = (TextView)custom_view_message.findViewById(R.id.textViewCustomDialogMessageDate);
        mImageViewCustomDialogMessageUserReceiver = (CircularImageView)custom_view_message.findViewById(R.id.imageViewCustomDialogMessageUserReceiver);
        mButtonCustomDialogMessageSent = (Button)custom_view_message.findViewById(R.id.buttonCustomDialogMessageSent);

        dialog = new AlertDialog.Builder(mActivity)
                .setView(custom_view_message)
                .setTitle("Message")
                .setNegativeButton(mActivity.getResources().getString(R.string.btn_return), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();
    }

    public void showDialog() {
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

    public void lookMessage(Boolean b)
    {
        if(b)
        {
            mButtonCustomDialogMessageSent.setVisibility(View.GONE);
            mEditTextCustomDialogMessageMessage.setEnabled(false);
            mEditTextCustomDialogMessageTitle.setEnabled(false);
            mEditTextCustomDialogMessageTitle.setMaxLines(20);
            mEditTextCustomDialogMessageTitle.setBackgroundColor(mActivity.getResources().getColor(R.color.white_transparent));
            mEditTextCustomDialogMessageMessage.setBackgroundColor(mActivity.getResources().getColor(R.color.white_transparent));
        }
        else {
            mButtonCustomDialogMessageSent.setVisibility(View.VISIBLE);
            mEditTextCustomDialogMessageMessage.setEnabled(true);
            mEditTextCustomDialogMessageTitle.setEnabled(true);
        }
    }

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
