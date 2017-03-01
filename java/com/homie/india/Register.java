package com.homie.india;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.homie.india.es.R;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Register extends Activity implements View.OnClickListener {


    public static final String UPLOAD_KEY_IMAGE = "image";
    public static final String UPLOAD_KEY_NAME = "name";
    public static final String UPLOAD_KEY_USERNAME = "username";
    public static final String UPLOAD_KEY_PASSWORD = "password";
    public static final String UPLOAD_KEY_EMAIL = "email";
    public static final String UPLOAD_KEY_DOB = "dob";
    public static final String UPLOAD_KEY_GENDER = "gender";


    private int PICK_IMAGE_REQUEST = 1;

    private int day;
    private int month;
    private int year;

    private TextView textname;
    private TextView textusername;
    private TextView textemail;
    private TextView textpassword;
    private TextView textcpassword;

    private EditText editname;
    private EditText editusername;
    private EditText editemail;
    private EditText editpassword;
    private EditText editcpassword;
    private EditText editdob;
    private EditText editgender;


    private RadioGroup sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private RadioButton rb_other;

    private Button bt_dob;
    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView iv_dp;
    private Bitmap bitmap;

    int pos;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        textname = (TextView) findViewById(R.id.tv_name);
        textusername = (TextView) findViewById(R.id.tv_username);
        textemail = (TextView) findViewById(R.id.tv_email);
        textpassword = (TextView) findViewById(R.id.tv_password);
        textcpassword = (TextView) findViewById(R.id.tv_cpassword);

        editname = (EditText) findViewById(R.id.et_name);
        editgender = (EditText) findViewById(R.id.et_gender);
        editusername = (EditText) findViewById(R.id.et_username);
        editemail = (EditText) findViewById(R.id.et_email);
        editpassword = (EditText) findViewById(R.id.et_password);
        editcpassword = (EditText) findViewById(R.id.et_cpassword);
        editdob = (EditText) findViewById(R.id.et_dob);


        sex = (RadioGroup) findViewById(R.id.rg_sex);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                pos = sex.indexOfChild(findViewById(checkedId));
                String gender;
                switch (pos) {
                    case 0:
                        gender = "MALE";
                        editgender.setText(gender);
                        break;
                    case 1:
                        gender = "FEMALE";
                        editgender.setText(gender);
                        break;
                    case 2:
                        gender = "OTHER";
                        editgender.setText(gender);
                        break;
                    default:
                        gender = "FEMALE";
                        editgender.setText(gender);
                        break;
                }

            }
        });


        buttonChoose = (Button) findViewById(R.id.bt_choose);
        buttonUpload = (Button) findViewById(R.id.bt_upload);
        bt_dob= (Button) findViewById(R.id.bt_dob);

        iv_dp = (SelectableRoundedImageView) findViewById(R.id.iv_dp);

        buttonChoose.setOnClickListener(this);
    buttonUpload.setOnClickListener(this);
        bt_dob.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
       /* intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);*/
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_dp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void uploadImage(){


        final String name = editname.getText().toString().trim();
        final String username = editusername.getText().toString().trim();
        final String email = editemail.getText().toString().trim();
        final String password = editpassword.getText().toString().trim();
        final String dob=editdob.getText().toString().trim();
        final String gender=editgender.getText().toString().trim();



        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY_IMAGE, uploadImage);
                data.put(UPLOAD_KEY_NAME, name);
                data.put(UPLOAD_KEY_USERNAME, username);
                data.put(UPLOAD_KEY_PASSWORD, password);
                data.put(UPLOAD_KEY_EMAIL, email);
                data.put(UPLOAD_KEY_DOB, dob);
               data.put(UPLOAD_KEY_GENDER,gender );


                String result = rh.sendPostRequest(Config.UPLOAD_URL,data);

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
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);

        }
        if(v==bt_dob){
            showDialog(999);
        }

    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            editdob.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };
}