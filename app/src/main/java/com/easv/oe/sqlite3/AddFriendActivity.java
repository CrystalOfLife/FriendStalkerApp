package com.easv.oe.sqlite3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Nicolai on 08-04-2018.
 */

public class AddFriendActivity extends ActionBarActivity {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    ImageButton btnPicture;
    Button btnSave;
    EditText editName, editBirthday, editAddress, editPhone, editMail, editWebsite;
    TextView txtFail;
    Bitmap profilePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        btnPicture = (ImageButton) findViewById(R.id.btnPicture);
        editName = (EditText) findViewById(R.id.editName);
        editBirthday = (EditText) findViewById(R.id.editBirthday);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editMail = (EditText) findViewById(R.id.editMail);
        editWebsite = (EditText) findViewById(R.id.editWebsite);
        txtFail = (TextView) findViewById(R.id.txtFail);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty() || editMail.getText().toString().isEmpty() || editWebsite.getText().toString().isEmpty() ||
                        editPhone.getText().toString().isEmpty() || editBirthday.getText().toString().isEmpty() || editAddress.getText().toString().isEmpty() ||
                        profilePicture == null)
                {
                    txtFail.setText("Please input information on all fields");
                }
                else
                {
                    onClickSave();
                }
            }

        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    openPictureActivity();
                }
            }
        });
    }

    void onClickSave() {
        DAO dao = DAO.getInstance();
        String name = editName.getText().toString();
        String mail = editMail.getText().toString();
        String website = editWebsite.getText().toString();
        String phone = editPhone.getText().toString();
        String birthday = editBirthday.getText().toString();
        String address = editAddress.getText().toString();
        Bitmap picture = profilePicture;
        dao.insert(new BEPerson(0, name, mail, website, phone, birthday, address, picture));
        editName.setText("");
        editMail.setText("");
        editWebsite.setText("");
        editPhone.setText("");
        editBirthday.setText("");
        editAddress.setText("");

        Intent x = new Intent();
        x.setClass(AddFriendActivity.this, MainActivity.class);
        startActivity(x);
    }

    private void openPictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

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

    private void setTakenPicture(Intent data) {
        Bitmap picture = (Bitmap) data.getExtras().get("data");
        profilePicture = roundCropBitmap(picture);
        btnPicture.setImageBitmap(profilePicture);
    }

    /**
     * Method the crop the picture to a round shape once the user takes one
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
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
