package com.icti.tudoauto.Model;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.icti.tudoauto.FuelActivity;
import com.icti.tudoauto.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class Application {

    private static Measure imeasure;
    private static List<Measure> measures;
    private static Register logindata;
    private static List<Measure> means;
    private static Price price;
    private static List<Price> prices;
    private Context mbaseContext;
    private Position position = new Position();

    //-----------------------------------------------------------------------------------------------------------------------------PUBLIC FUNCTIONS

    public static boolean VerifyNoLogin(Context context) {
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        ConnectFirebase(context);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            return true;
        } else {
            return false;
        }

    }

    public static void importUserData(final Context context) {
        FirebaseAuth mAuth;
        DatabaseReference db;

        mAuth = FirebaseAuth.getInstance();

        ConnectFirebase(context);

        db = FirebaseDatabase.getInstance().getReference();

        Query query = db.child("userdata").child(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //importing data from db
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String usuarioId = mAuth.getCurrentUser().getUid();

                //Creating lists
                measures = new ArrayList<Measure>();
                means = new ArrayList<Measure>();
                prices = new ArrayList<Price>();

                Application.setImeasure(dataSnapshot.child("incompletemeasure").getValue(Measure.class));
                Application.setLogindata(dataSnapshot.child("userinfo").getValue(Register.class));
                for (DataSnapshot objSnapshot : dataSnapshot.child("measures").getChildren()) {
                    Measure measure = objSnapshot.getValue(Measure.class);
                    measures.add(measure);
                }
                for (DataSnapshot objSnapshot : dataSnapshot.child("means").getChildren()) {
                    Measure mean = objSnapshot.getValue(Measure.class);
                    means.add(mean);
                }
                for (DataSnapshot objSnapshot : dataSnapshot.child("prices").getChildren()) {
                    Price price = objSnapshot.getValue(Price.class);
                    prices.add(price);
                }
                checkMeansList(context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                alert("Não foi possível carregar dados", context);
            }
        });
        String oi = null;
    }

    private static void alert(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void checkMeansList(Context context) {
        //CORRECT POSIBLE FAULTS
        Boolean val = true;

        String[] fuelType = context.getResources().getStringArray(R.array.fueltype);
        List<String> fuelTypeList = Arrays.asList(fuelType);
        fuelTypeList = new ArrayList<String>(fuelTypeList);

        if (means.size()<fuelTypeList.size()){
            for (int i = 0; i < fuelTypeList.size(); i++) {
                Measure tempMeasure = new Measure();
                tempMeasure.setFueltype(fuelTypeList.get(i));
                means.add(tempMeasure);
            }
            val = false;
        } else {
            for (int i = 0; i < fuelTypeList.size(); i++) {
                if (means.get(i).getFueltype() != fuelTypeList.get(i) || means.size() != fuelTypeList.size()) {
                    val = false;
                }
            }
        }

        if (val == false) {
            redoMeansList(context);
        }
    }

    public static void redoMeansList(Context context) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String usuarioId = mAuth.getCurrentUser().getUid();

        String[] fuelType = context.getResources().getStringArray(R.array.fueltype);
        List<String> fuelTypeList = Arrays.asList(fuelType);
        fuelTypeList = new ArrayList<String>(fuelTypeList);

        means = null;
        means = new ArrayList<Measure>();
        for (int i = 0; i < fuelTypeList.size(); i++) {
            Measure tempMeasure = new Measure();
            tempMeasure.setFueltype(fuelTypeList.get(i));
            means.add(tempMeasure);
        }
        for (int i = measures.size()-1; i >= 0; i--) {
            for (int j = 0; j < means.size(); j++) {
                if (means.get(j).getFueltype().equals(measures.get(i).getFueltype())) {
                    if (means.get(j).getVolume() != 0) {
                        float newVolume = means.get(j).getVolume() + 1;
                        means.get(j).setMeasureavg((means.get(j).getDistance() + measures.get(i).getMeasureavg()) / 2);
                        means.get(j).setDistance((means.get(j).getDistance() * means.get(j).getVolume() + measures.get(i).getMeasureavg()) / newVolume);
                        means.get(j).setVolume(newVolume);
                        break;
                    } else {
                        float newVolume = 1;
                        means.get(j).setVolume(newVolume);
                        means.get(j).setDistance(measures.get(i).getMeasureavg());
                        means.get(j).setPosition(measures.get(i).getPosition());
                        means.get(j).setMeasureavg(measures.get(i).getMeasureavg());
                        means.get(j).setTimestamp(measures.get(i).getTimestamp());
                        break;
                    }
                }
            }
        }
        db.child("userdata").child(usuarioId).child("means").setValue(means);
    }

    public static void ConnectFirebase(Context context) {
        FirebaseApp.initializeApp(context);
    }

    public static DatabaseReference startFirebase(Context context) {
        DatabaseReference databaseReference;

        FirebaseApp.initializeApp(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return databaseReference;
    }

    public static boolean CreateIMeasure(Context context) {
        DatabaseReference databaseReference;

        String usuarioId = getLogindata().getID_user();

        databaseReference = startFirebase(context);
        databaseReference.child("userdata").child(usuarioId).child("incompletemeasure").setValue(imeasure);
        return true;
    }

    public static boolean CreatePrices(Context context) {
        DatabaseReference databaseReference;

        prices.add(price);

        String usuarioId = getLogindata().getID_user();

        databaseReference = startFirebase(context);
        databaseReference.child("userdata").child(usuarioId).child("prices").setValue(prices);
        return true;
    }

    public static boolean CreateMeasures(Context context) {

        DatabaseReference databaseReference;
        String usuarioId = getLogindata().getID_user();

        redoMeansList(context);

        databaseReference = startFirebase(context);
        databaseReference.child("userdata").child(usuarioId).child("measures").setValue(measures);
        databaseReference.child("userdata").child(usuarioId).child("means").setValue(means);
        databaseReference.child("userdata").child(usuarioId).child("incompletemeasure").setValue(imeasure);
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------------------------GEOLOCATION FUNCTIONS

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = mbaseContext
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            //position.setLatitude(loc.getLatitude());
            //position.setLongitude(loc.getLongitude());

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    public Position getPosition() {
        LocationManager mLocationManager;
        LocationListener locationListener;

        mLocationManager = (LocationManager)
                mbaseContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        if (displayGpsStatus()) {
            locationListener = new MyLocationListener();

            //Check if app has geolocation permissions
            if (checkSelfPermission(mbaseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mbaseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            } else {

                for (String provider : providers) {
                    mLocationManager.requestSingleUpdate(provider, locationListener, null);
                    Location l = mLocationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }

                    position.setLongitude(bestLocation.getLongitude());
                    position.setLatitude(bestLocation.getLatitude());
                }

                /*----------to get City-Name from coordinates ------------- */
                String adressLine = null;
                Geocoder gcd = new Geocoder(mbaseContext,
                        Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(position.getLatitude(), position.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        adressLine = addresses.get(0).getAddressLine(0);
                        position.setAdressLine(adressLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return position;
        } else {
            return null;
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------GETTERS AND SETTERS

    public static Measure getImeasure() {
        return imeasure;
    }

    public static void setImeasure(Measure imeasure) {
        Application.imeasure = imeasure;
    }

    public static List<Measure> getMeasures() {
        return measures;
    }

    public static void setMeasures(List<Measure> measures) {
        Application.measures = measures;
    }

    public static Register getLogindata() {
        return logindata;
    }

    public static void setLogindata(Register logindata) {
        Application.logindata = logindata;
    }

    public static List<Measure> getMeans() {
        return means;
    }

    public static void setMeans(List<Measure> means) {
        Application.means = means;
    }

    public static void setPrice(Price price) {
        Application.price = price;
    }

    public static void setPrices(List<Price> prices) {
        Application.prices = prices;
    }

    public void setMbaseContext(Context mbaseContext) {
        this.mbaseContext = mbaseContext;
    }

}
