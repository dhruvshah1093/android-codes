package com.homie.india;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.homie.india.es.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class createListing extends Activity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://10.0.2.2:8888/Home/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 0;

    private ImageButton buttonChoose;
    private ImageButton buttonChoose2;
    private ImageButton buttonUpload;

    private ImageView imageView;
    private ImageView mul_img;

    private Bitmap bitmap;
    private Bitmap bitmap2;

    private Uri filePath;
    private Uri path;
    private String result;
    private String[] array;
    final   String[] x =new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        buttonChoose = (ImageButton) findViewById(R.id.buttonChoose);
        buttonChoose2 = (ImageButton) findViewById(R.id.buttonChoose2);
        buttonUpload = (ImageButton) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);
        mul_img = (ImageView) findViewById(R.id.mul_img);

        buttonChoose.setOnClickListener(this);
        buttonChoose2.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void mulFileChooser() {
        Intent mulIntent = new Intent(this, CustomGallery.class);
        startActivityForResult(mulIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    result = data.getStringExtra("result");
                    array = result.split(",");
                    convertBit2String();


                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void convertBit2String(){
        class bit2string extends AsyncTask<Bitmap, Void, String >{
            @Override
            protected String doInBackground(Bitmap... params) {
               Log.d("length",""+array.length);
                for (int i=0;i<array.length;i++){
                path=path.parse("file://"+array[i]);
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                   x[i]=getStringImage(bitmap2);

            }
                for (int i=0;i<array.length;i++){
                    Log.d("mul_images"+i,x[i]);

                }
                return null;
        }

        }
        bit2string ui = new bit2string();
        ui.execute(bitmap2);
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(createListing.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Log.d("query",s);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                    String uploadImage = getStringImage(bitmap);
                    HashMap<String, String> data = new HashMap<>();
                    for(int i=0;i<array.length;i++){
                    data.put("mul_images"+i, x[i]);}
                    data.put(UPLOAD_KEY, uploadImage);
                    data.put("length",""+array.length);
                    Log.d("length", "" + array.length);
                    String result = rh.sendPostRequest(UPLOAD_URL, data);
                    return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            uploadImage();
            Intent myIntent = new Intent(createListing.this,
                    createListing2.class);
            startActivity(myIntent);

        }
        if(v==buttonChoose2){
            mulFileChooser();



        }
    }
}