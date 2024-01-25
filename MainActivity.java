package com.iotexpedition.musharaf;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText dp,ac,na,ad;
    VideoView v1;
    Uri u1,u2;
    String u3;
    private ActivityResultLauncher<Intent> launcher;
    TextView t1;
    Spinner sp;
    Button b1,sub,rec;
    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update
    LocationManager locationManager;
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button bpic,bpic2;
    ImageView ipic,ipic2;
    public final class GPSTracker implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        public boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        /**
         * Function to get the user's current location
         *
         * @return
         */
        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                Log.v("isGPSEnabled", "=" + isGPSEnabled);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

                if (isGPSEnabled == false ) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;

                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        location=null;
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener Calling this function will stop using GPS in your
         * app
         * */
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog On pressing Settings button will
         * lauch Settings Options
         * */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            ipic.setImageBitmap(photo);
            u1=getImageUri(getApplicationContext(),photo);
        }
        else if(requestCode==2 && resultCode==RESULT_OK)
        {
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            ipic2.setImageBitmap(photo);
            u2=getImageUri(getApplicationContext(),photo);
        }
        else if(requestCode==3 && resultCode==RESULT_OK)
        {
            //AlertDialog.Builder builder= new AlertDialog.Builder(this);
            //VideoView videoView= new VideoView(this);
            v1.setVideoURI(data.getData());

            v1.start();
            //builder.setView(videoView).show();
            u3=v1.toString();
        }
        else{
            Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG);
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions();
        GPSTracker mGPS = new GPSTracker(this);
        ac=findViewById(R.id.editTextNumber);
        na=findViewById(R.id.name);
        ad=findViewById(R.id.address);
        bpic=findViewById(R.id.button2);
        ipic=findViewById(R.id.imageView);
        bpic2=findViewById(R.id.button3);
        ipic2=findViewById(R.id.imageView2);
        sub=findViewById(R.id.button4);
        rec=findViewById(R.id.button5);
        v1=findViewById(R.id.videoView);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
                startActivityForResult(intent,3);
            }
        });

        bpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,1);
            }
        });

        bpic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2,2);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(ac.getText().toString())<0 || Integer.parseInt(ac.getText().toString())>100)
                {
                    Toast.makeText(MainActivity.this, "Enter valid Farm Land Area", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Intent intent1= new Intent(MainActivity.this, MainActivity2.class);
                    intent1.putExtra("name",na.getText().toString());
                    intent1.putExtra("address",ad.getText().toString());
                    intent1.putExtra("dob",dp.getText().toString());
                    intent1.putExtra("gender",sp.getSelectedItem().toString());
                    intent1.putExtra("area",ac.getText().toString());
                    intent1.putExtra("location",t1.getText().toString());
                    intent1.putExtra("image1",u1.toString());
                    intent1.putExtra("image2",u2.toString());
                    intent1.putExtra("video",u3);
                    startActivity(intent1);
                    return;

                }
            }
        });


    dp = findViewById(R.id.dob);
        //Calendar calendar = Calendar.getInstance();
         sp=findViewById(R.id.spinner);
         b1=findViewById(R.id.button);
         t1=findViewById(R.id.loctext);
         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String latitude,longitude;
                 if (ActivityCompat.checkSelfPermission(
                         MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                         MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                 } else {

                     if(mGPS.canGetLocation ){
                         mGPS.getLocation();
                         t1.setText("Lat:"+mGPS.getLatitude()+" Lon:"+mGPS.getLongitude());
                     }else {
                         Toast.makeText(MainActivity.this,"Unable to find location",Toast.LENGTH_LONG).show();
                         //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                     }
             }
             }
         });
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dp.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp.setAdapter(adapter);

    }

    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[3]) == PackageManager.PERMISSION_GRANTED) {
            if(!isGPSEnabled())
            {
                turnOnGPS();
            }

        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
    }

    private void turnOnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermissions();
    }
    private boolean isGPSEnabled()
    {
        LocationManager lm=null;
        boolean isEnabled=false;
        if(lm==null)
        {
            lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }
}

