package com.example.android.masjeed;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android.masjeed.databinding.ActivityMainBinding;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private String mosque_code = "";
    private String donation = "";

    private List found = null;
    private TextView name, fajr, sunrise, zuhr, asr, magrib, isha, jummaah;
    private SimpleDateFormat format;

    public static final String CHANNEL_ID = "#180";
    public static final String CHANNEL_NAME = "Prayer Time Notification";
    public static final String CHANNEL_DESCRIPTION = "New Implementation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Abdulmumin
        format = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NotificationManager notificationManager = null;
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.yusuf_islam);  //Here is FILE_NAME is the name of file that you want to play

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            /**NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
             getApplicationContext().getString(R.string.app_name),
             NotificationManager.IMPORTANCE_HIGH);*/
            Log.d("Audio","1");
            // Configure the notification channel.
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(sound, attributes); // This is IMPORTANT
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            Log.d("Audio","2");
            notificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
         && !notificationManager.isNotificationPolicyAccessGranted()) {

         Intent intent = new Intent(
         android.provider.Settings
         .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

         startActivity(intent);
         }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getApplicationContext().startService(new Intent(getApplicationContext(), PrayingDayCalculateHandler.class));
        //fetchMosqueTimings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }else if (id == R.id.action_refresh_timings) {
            fetchMosqueTimings();
        } else if (id == R.id.action_add_mosque){
            insertMosqueCode();
            return true;
        }

            return super.onOptionsItemSelected(item);
    }

    //Abdulmumin added

    public void insertMosqueCode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insert Mosque Code");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mosque_code = input.getText().toString();
                editor.putString("mosquecode",mosque_code);
                editor.apply();
                editor.commit();

                fetchMosqueTimings();




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        /*return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    public void fetchMosqueTimings() {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory factory = JacksonFactory.getDefaultInstance();
        final Sheets sheetsService = new Sheets.Builder(transport, factory, null)
                .setApplicationName("My Awesome App")
                .build();
        final String spreadsheetId = Config.spreadsheet_id;

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        //Body of your click handler
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //you call here
                String range = "Sheet1!A2:J";
                ValueRange result = null;
                try {
                    result = sheetsService.spreadsheets().values()
                            .get(spreadsheetId, range)
                            .setKey(Config.google_api_key)
                            .execute();
                    List<List<Object>> values = result.getValues() ;
                    int numRows = result.getValues() != null ? result.getValues().size() : 0;
                    Log.d("SUCCESS.", "rows retrived " + numRows);
                    System.out.println("Name, Code, Fajr, Sunrise, Zuhr, Asr, Maghrib, Isha");
                    Log.d("SUCCESS.", "Name, Code, Fajr, Sunrise, Zuhr, Asr, Maghrib, Isha");


                    String mc = preferences.getString("mosquecode", mosque_code);
                    Log.d("Mosque Code.", mc);
                    for (List row : values) {
                        // Print columns A and E, which correspond to indices 0 and 4.
                        String code = (String) row.get(1);

                        if(code.toLowerCase().equals(mc.toLowerCase())) {
                            found = row;
                            donation = (String) row.get(9);
                        }

                    }
                    if(found != null) {
                        MosqueTimings.setTimings(found);

                        //Update
                        name = (TextView) findViewById(R.id.mosque_name);
                        fajr = (TextView) findViewById(R.id.fajrTime);
                        sunrise = (TextView) findViewById(R.id.sunriseTime);
                        zuhr = (TextView) findViewById(R.id.zuhrTime);
                        asr = (TextView) findViewById(R.id.asrTime);
                        magrib = (TextView) findViewById(R.id.magribTime);
                        isha = (TextView) findViewById(R.id.ishaTime);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String currentDate = sdf.format(new Date());

                        String fStr = "",sStr="",zStr="",aStr="",mStr="",iStr="", frStr;

                        DateFormat formatter = new SimpleDateFormat("yyyy.MM.d hh:mm a");

                        final Date fajrDate, sunriseDate, zuhrDate, asrDate, magribDate, ishaDate, fridayDate;
                        fStr = currentDate + " " + found.get(2);
                        sStr = currentDate + " " + found.get(3);
                        zStr = currentDate + " " + found.get(4);
                        aStr = currentDate + " " + found.get(5);
                        mStr = currentDate + " " + found.get(6);
                        iStr = currentDate + " " + found.get(7);
                        frStr = currentDate + " " + found.get(8);


                        editor.putString("ifajr", fStr);
                        editor.putString("isunrise", sStr);
                        editor.putString("izohr", zStr);
                        editor.putString("iasr", aStr);
                        editor.putString("imagrib", mStr);
                        editor.putString("iisha", iStr);
                        editor.putString("ifriday", frStr);

                        try {
                            fajrDate = (Date)formatter.parse(fStr);
                            sunriseDate = (Date)formatter.parse(sStr);
                            zuhrDate = (Date)formatter.parse(zStr);
                            asrDate = (Date)formatter.parse(aStr);
                            magribDate = (Date)formatter.parse(mStr);
                            ishaDate = (Date)formatter.parse(iStr);
                            fridayDate = (Date)formatter.parse(frStr);

                            Log.d("MA::Fajr",String.valueOf(fajrDate));
                            Log.d("MA::Fajr",String.valueOf(format.format(fajrDate)));
                            Log.d("MA::Sunrise",String.valueOf(sunriseDate));
                            Log.d("MA::Sunrise",String.valueOf(format.format(sunriseDate)));
                            Log.d("MA::Zuhr",String.valueOf(zuhrDate));
                            Log.d("MA::Zuhr",String.valueOf(format.format(zuhrDate)));
                            Log.d("MA::Asr",String.valueOf(asrDate));
                            Log.d("MA::Asr",String.valueOf(format.format(asrDate)));
                            Log.d("MA::Magrib",String.valueOf(magribDate));
                            Log.d("MA::Magrib",String.valueOf(format.format(magribDate)));
                            Log.d("MA::Isha",String.valueOf(ishaDate));
                            Log.d("MA::Isha",iStr);
                            Log.d("MA::Isha",String.valueOf(format.format(ishaDate)));
                            Log.d("MA::Friday",frStr);
                            Log.d("MA::Friday",String.valueOf(format.format(fridayDate)));
                            Log.d("Donation Text :: ", donation);

                            Calendar cal = Calendar.getInstance();

                            boolean friday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

                            Log.d("Is Friday :: ", friday + "");

                            editor.putString("mosquename", String.valueOf( found.get(0)));
                            editor.putString("fajr", format.format(fajrDate));
                            editor.putString("sunset", format.format(sunriseDate));
                            editor.putString("zohr", format.format(zuhrDate));
                            editor.putString("asr", format.format(asrDate));
                            editor.putString("maghrib", format.format(magribDate));
                            editor.putString("isha", format.format(ishaDate));
                            editor.putString("friday", format.format(fridayDate));
                            editor.putString("mosquecode",mosque_code);
                            editor.putString("donation",donation);


                            editor.apply();
                            editor.commit();

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.fragment_timings, TimingsFragment.class, null, "tag")
                                            .setReorderingAllowed(true)
                                            .addToBackStack(null)
                                            .commit();

                                    TimingsFragment fragment = (TimingsFragment) fragmentManager.findFragmentByTag("timings1");

                                }
                            });
                            Log.d("Love::2", "rows retrived " + numRows);
                            getApplicationContext().startService(new Intent(getApplicationContext(), PrayingDayCalculateHandler.class));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Log.d("Love::1.", "My Love");
                                //getApplicationContext().startForegroundService(new Intent(getApplicationContext(), PrayingDayCalculateHandler.class));
                            }else {

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("Network Status :: ",  "No Network");

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_timings, TimingsFragment.class, null, "tag")
                                    .setReorderingAllowed(true)
                                    .addToBackStack(null)
                                    .commit();

                            TimingsFragment fragment = (TimingsFragment) fragmentManager.findFragmentByTag("timings1");
                            getApplicationContext().startService(new Intent(getApplicationContext(), PrayingDayCalculateHandler.class));

                            Toast.makeText(getApplicationContext(),"No/Poor Network",Toast.LENGTH_LONG).show();


                        }
                    });
                    e.printStackTrace();


                }


            }
        });
        thread.start();

    }


    /**
     * Open a web page of a specified URL
     *Abdulmumin
     */
    public void updateMosqueTimeWebPage(MenuItem item) {
        Uri webpage = Uri.parse("https://almasjeed.com/mosque-prayer-time-update/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Open a web page of a specified URL
     *Abdulmumin
     */
    public void addMosqueWebPage(MenuItem item) {
        Uri webpage = Uri.parse("https://almasjeed.com/add-mosques/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}