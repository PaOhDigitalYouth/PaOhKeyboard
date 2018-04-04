package com.paohdigitalyouth.paohkeyboard;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AdiKa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    LinearLayout enableBtn,selectBtn,testBtn,helpBtn;
    Typeface mm;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adi_ka);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FontChanger calligrapher = new FontChanger(this);
        calligrapher.setFont(this, "fonts/paoh.ttf", true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adRequest = new AdRequest.Builder().build();
        banner = findViewById(R.id.adView);
        banner.loadAd(adRequest);

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
        enableBtn = findViewById(R.id.enableBtn);
        selectBtn = findViewById(R.id.selectBtn);
        testBtn = findViewById(R.id.testBtn);
        helpBtn = findViewById(R.id.helpBtn);

        enableBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        testBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        mm = Typeface.createFromAsset(getAssets(),"fonts/paoh.ttf");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adi_ka, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showAD();
            startActivity(new Intent(this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setup) {
            setupKeyboard();
        } else if (id == R.id.nav_setting) {
            showAD();
            startActivity(new Intent(this,SettingActivity.class));
        } else if (id == R.id.nav_change_theme) {
            startActivity(new Intent(this,ThemeActivity.class));
        } else if (id == R.id.nav_help) {
            showAD();
            startActivity(new Intent(this,Help.class));
        } else if (id == R.id.nav_rate) {
            rate();
        }else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupKeyboard(){
        if (isThisKeyboardEnabled()==true){
            if (isThisKeyboardSetAsDefaultIME(getApplicationContext())){
                Toast.makeText(this, "Already finished!", Toast.LENGTH_SHORT).show();
                showAD();
            }else{
                selectKeyboard();
            }
        }else{
            enableKeyboard();
        }
    }

    private void loadAD() {
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }
    }

    private void showAD() {
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }else{
            interstitialAd.show();
        }
    }

    public static boolean isThisKeyboardSetAsDefaultIME(Context context) {
        final String defaultIME = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        return isThisKeyboardSetAsDefaultIME(defaultIME, context.getPackageName());
    }

    public static boolean isThisKeyboardSetAsDefaultIME(String defaultIME, String myPackageName) {
        if (TextUtils.isEmpty(defaultIME))
            return false;
        ComponentName defaultInputMethod = ComponentName.unflattenFromString(defaultIME);
        return defaultInputMethod.getPackageName().equals(myPackageName);
    }


    public void enableKeyboard() {
        Intent in = new Intent();
        in.setAction(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(in);
    }

    public void selectKeyboard(){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showInputMethodPicker();
        }
    }

    public boolean isThisKeyboardEnabled() {
        String packageLocal = getPackageName();
        boolean isInputDeviceEnabled = false;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list = inputMethodManager.getEnabledInputMethodList();
        for (InputMethodInfo inputMethod : list) {
            String packageName = inputMethod.getPackageName();
            if (packageName.equals(packageLocal)) {
                isInputDeviceEnabled = true;
            }
        }

        return isInputDeviceEnabled;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enableBtn:
                showAD();
                if (isThisKeyboardEnabled()==true){
                    Toast.makeText(this, "Already enabled!\nPlease select keyboard", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Please Enable PaOh Keyboard", Toast.LENGTH_SHORT).show();
                    enableKeyboard();
                }
                break;
            case R.id.selectBtn:
                showAD();
                if (isThisKeyboardEnabled()==true){
                    if (isThisKeyboardSetAsDefaultIME(getApplicationContext())==true){
                        Toast.makeText(this, "Already selected and enabled PaOh Keyboard.\nNow you can write :)", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Please Select PaOh Keyboard", Toast.LENGTH_SHORT).show();
                        selectKeyboard();
                    }
                }else{
                    Toast.makeText(this, "Please Enable PaOh Keyboard First :)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.testBtn:
                if (isThisKeyboardEnabled()==true){
                    if(isThisKeyboardSetAsDefaultIME(this)==true){
                        final EditText editText = new EditText(AdiKa.this);
                        editText.setHint("Enter your text");
                        editText.setTypeface(mm);
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdiKa.this)
                                .setTitle("Testing")
                                .setView(editText)
                                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        showAD();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }else{
                        showAD();
                        Toast.makeText(this, "Please Select PaOh Keyboard First :)", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showAD();
                    Toast.makeText(this, "Please Enable PaOh Keyboard First :)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.helpBtn:
                showAD();
                startActivity(new Intent(this,Help.class));
                break;
        }
    }

    public void rate(){
        ImageView imageView = new ImageView(this);
        imageView.setPadding(5,0,5,0);
        imageView.setImageResource(R.drawable.rateme);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                .setTitle("ံHelp Us")
                .setView(imageView)
                .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"PaOh Keyboard for Android Free at Google Play Store : play.google.com/store/apps/details?id="+getPackageName()+"\n\nDirect Download : http://bit.ly/2FLfqG8\n#mmFacebookToolBox");
        startActivity(Intent.createChooser(intent,"Share App Via..."));
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
