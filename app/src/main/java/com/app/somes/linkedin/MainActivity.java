package com.app.somes.linkedin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Spinner citizenship,state,town;
    AppCompatEditText firstName,lastName,headline,industry,summary;
    AppCompatTextView newEducation;
    CircleImageView userProfile;
    private final int PICK_IMAGE_CAMERA = 100, PICK_IMAGE_GALLERY = 200, CROP_IMAGE = 1;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent;
    AppCompatTextView saveButton;
    AppCompatButton uploadButton,linkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);
        newEducation.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        linkButton.setOnClickListener(this);
    }

    private void initView() {
        citizenship= (Spinner)findViewById(R.id.country_spinner);
        state= (Spinner)findViewById(R.id.state_spinner);
        town= (Spinner)findViewById(R.id.town_spinner);
        firstName= (AppCompatEditText) findViewById(R.id.firstName_editText);
        lastName= (AppCompatEditText) findViewById(R.id.lastName_editText);
        industry= (AppCompatEditText) findViewById(R.id.industry_editText);
        summary= (AppCompatEditText) findViewById(R.id.summary_editText);
        newEducation= (AppCompatTextView) findViewById(R.id.new_Education);
        userProfile= (CircleImageView) findViewById(R.id.user_profile);
        saveButton= (AppCompatTextView) findViewById(R.id.save_button);
        uploadButton= (AppCompatButton) findViewById(R.id.upload_button);
        linkButton= (AppCompatButton) findViewById(R.id.link_button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_Education:
                startActivity(new Intent(this,NewEducation.class));
                break;
            case R.id.upload_button:
                selectProfile();
                break;
            case R.id.save_button:
                Toast.makeText(this, "Oh boss don't click !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.link_button:

                break;

        }
    }

    private void selectProfile() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.choose_option));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(getString(R.string.take_photo))) {
                            dialog.dismiss();
                            ClickImageFromCamera();
                            Toast.makeText(MainActivity.this, "currently not working", Toast.LENGTH_SHORT).show();
                        } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                            dialog.dismiss();
                            GetImageFromGallery();
                        } else if (options[item].equals(getString(R.string.cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, getString(R.string.camera_permission_error), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.camera_permission_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void ClickImageFromCamera() {
        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, PICK_IMAGE_CAMERA);
    }

    private void GetImageFromGallery() {
        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, getString(R.string.choose_from_gallery)), PICK_IMAGE_GALLERY);

    }
}
