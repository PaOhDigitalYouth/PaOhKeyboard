package com.paohdigitalyouth.paohkeyboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView kb1,kb2,paoh,dark;
    String myPath;
    int color = 0xffffff00;

    InterstitialAd interstitialAd;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        myPath = Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()+"/";

        sharedPreferences = getSharedPreferences("Htetz",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        kb1 = findViewById(R.id.kb1IV);
        kb2 = findViewById(R.id.kb2IV);
        paoh = findViewById(R.id.paoh);
        dark = findViewById(R.id.dark);


        kb1.setVisibility(View.GONE);
        kb2.setVisibility(View.GONE);

        paoh.setOnClickListener(this);
        dark.setOnClickListener(this);

        checkTheme();
        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/paoh.ttf");

        adRequest = new AdRequest.Builder().build();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1325188641119577/1312833208");
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadAD();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                loadAD();
            }
        });
    }

    public void loadAD(){
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }
    }

    public void showAD(){
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }else{
            interstitialAd.show();
        }
    }

    public void checkTheme(){
        String theme = sharedPreferences.getString("theme","paoh");
        if (theme.equals("paoh")){
            kb2.setVisibility(View.VISIBLE);
            kb1.setVisibility(View.GONE);
        }else if (theme.equals("dark")){
            kb1.setVisibility(View.VISIBLE);
            kb2.setVisibility(View.GONE);
        }else{
            kb1.setVisibility(View.GONE);
            kb2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dark:
                editor.putString("theme","dark");
                break;
            case R.id.paoh:
                editor.putString("theme","paoh");
                break;
        }
        editor.commit();
        editor.apply();
        checkTheme();
        Toast.makeText(this, "Theme Applied!", Toast.LENGTH_SHORT).show();
        showAD();
    }

    public void chooseImage(View view) {
        if (checkPermissions() == true) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("My Crop")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setCropMenuCropButtonTitle("Done")
                    .setRequestedSize(400, 400)
                    .start(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showAD();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    File directory = new File(myPath);
                    if (!directory.exists()){
                        directory.mkdirs();
                    }

                    File lol=new File(directory,"htetz.png");
                    if (saveImage(BitmapFactory.decodeFile(result.getUri().getPath()),lol)){
                        editor.putString("theme", "image");
                        editor.commit();
                        editor.apply();
                        checkTheme();
                        Toast.makeText(this, "Theme Applied!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("Htetz","File not found!");
                    }
                }catch (Exception e){
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean saveImage(Bitmap imageData,File sdIconStorageDir) {
        try {
            String filePath = sdIconStorageDir.toString();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(
                    fileOutputStream);
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("If you want to choose image, please allow this permission!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(ThemeActivity.this, listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), 5217);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.d("TAG","Permission"+"\n"+String.valueOf(false));
            return false;
        }
        Log.d("Permission","Permission"+"\n"+String.valueOf(true));
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5217: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("My Crop")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setCropMenuCropButtonTitle("Done")
                            .setRequestedSize(400, 400)
                            .start(this);
                } else {
                    checkPermissions();
                    Toast.makeText(this, "You need to Allow Write Storage Permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void chooseColor(View view) {
        openDialog(true);
    }

    void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                ThemeActivity.this.color = color;
                displayColor();
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                showAD();
            }
        });
        dialog.show();
    }

    void displayColor() {
        editor.putString("theme",String.valueOf(color));
        editor.commit();
        editor.apply();
        checkTheme();
        Toast.makeText(this, "Theme Applied!", Toast.LENGTH_SHORT).show();
        showAD();
    }
}
