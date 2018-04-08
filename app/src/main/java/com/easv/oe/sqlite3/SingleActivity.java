package com.easv.oe.sqlite3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static android.content.ContentValues.TAG;

public class SingleActivity extends AppCompatActivity {

    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    DAO dao;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    ImageButton btnPicture;
    Bitmap profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        dao = DAO.getInstance();

        int index = getIntent().getExtras().getInt("index");

        final BEPerson current = dao.getAll().get(index);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtAddress = (TextView)findViewById(R.id.txtAddress);
        final TextView txtPhone = (TextView)findViewById(R.id.txtPhone);
        final TextView txtMail = (TextView)findViewById(R.id.txtMail);
        TextView txtBirthday = (TextView)findViewById(R.id.txtBirthday);
        final TextView txtWebsite = (TextView)findViewById(R.id.txtWebsite);
        ImageButton callBtn = (ImageButton)findViewById(R.id.callBtn);
        ImageButton mailBtn = (ImageButton)findViewById(R.id.mailBtn);
        ImageButton smsBtn = (ImageButton)findViewById(R.id.smsBtn);
        ImageButton websiteBtn = (ImageButton)findViewById(R.id.websiteBtn);
        btnPicture = (ImageButton)findViewById(R.id.btnPicture);
        txtName.setText(current.m_name);
        txtAddress.setText(current.m_address);
        txtPhone.setText(current.m_phone);
        txtMail.setText(current.m_mail);
        txtBirthday.setText(current.m_birthday);
        txtWebsite.setText(current.m_website);
        btnPicture.setImageBitmap(current.m_picture);

        /**
         * Open website
         */
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("http://"+txtWebsite.getText().toString());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        /**
         * Open sms
         */
        smsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + txtPhone.getText().toString()));
                intent.putExtra("sms_body", "message");
                startActivity(intent);
            }
        });

        /**
         * Open mail
         */
        mailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{txtMail.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        /**
         * Open phone
         */
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + txtPhone.getText().toString()));
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPictureActivity();
            }
        });
    }

    public void onClickHome(View v)
    {

    }

    /**
     * goes to the MapsActivity
     * @param v
     */
    public void onClickShow(View v)
    {
        if(isServicesOK()){
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }

    }

    /**
     * Deletes the current friend
     * @param v
     */
    public void onClickDelete(View v)
    {
        dao = DAO.getInstance();

        int index = getIntent().getExtras().getInt("index");

        BEPerson current = dao.getAll().get(index);

        dao.deleteById(current.m_id);

        finish();
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SingleActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SingleActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * this open the camera app
     */
    private void openPictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * activates when taking a picture and sends the data
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    setTakenPicture(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * uses the data to and sets it on the button after being cropped
     * @param data
     */
    private void setTakenPicture(Intent data) {
        Bitmap picture = (Bitmap) data.getExtras().get("data");
        profilePicture = roundCropBitmap(picture);
        btnPicture.setImageBitmap(profilePicture);

    }

    /**
     * Crops the picture to be round
     * @param bitmap
     * @return
     */
    public Bitmap roundCropBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
