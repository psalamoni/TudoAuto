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
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.icti.tudoauto.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class Application {

    private static Measure imeasure;
    private static List<Measure> measures = new ArrayList<Measure>();
    private static Register logindata;
    private static List<Measure> means = new ArrayList<Measure>();
    private static Price price;
    private static List<Price> prices = new ArrayList<Price>();
    private Context mbaseContext;
    private Position position;

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

    public static void getUserData(final Context context) {
        FirebaseAuth mAuth;
        DatabaseReference db;

        mAuth = FirebaseAuth.getInstance();

        ConnectFirebase(context);

        db = FirebaseDatabase.getInstance().getReference();

        Query query = db.child("userdata").child(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

        measures.add(imeasure);

        String[] fuelType = context.getResources().getStringArray(R.array.fueltype);
        List<String> fuelTypeList = Arrays.asList(fuelType);
        fuelTypeList = new ArrayList<String>(fuelTypeList);

        if (means.size() == 0) {
            for (int i = 0; i < fuelTypeList.size(); i++) {
                Measure tempMeasure = new Measure();
                tempMeasure.setFueltype(fuelTypeList.get(i));
                means.add(imeasure);
            }
        }

        for (int i = 0; i < means.size(); i++) {
            if (means.get(i).getFueltype() == imeasure.getFueltype()) {
                if (means.get(i).getVolume() != 0) {
                    float newVolume = means.get(i).getVolume() + 1;
                    means.get(i).setMeasureavg((means.get(i).getDistance() + imeasure.getMeasureavg()) / 2);
                    means.get(i).setDistance((means.get(i).getDistance() * means.get(i).getVolume() + imeasure.getMeasureavg()) / newVolume);
                    means.get(i).setVolume(newVolume);
                    break;
                } else {
                    float newVolume = 1;
                    imeasure.setVolume(newVolume);
                    imeasure.setDistance(imeasure.getMeasureavg());
                    means.set(i, imeasure);
                    break;
                }
            }
        }

        imeasure = null;

        String usuarioId = getLogindata().getID_user();

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

            position.setLatitude(loc.getLatitude());
            position.setLongitude(loc.getLongitude());

            /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(mbaseContext,
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    position.setAddresses(addresses);
                cityName = addresses.get(0).getLocality();
                position.setCityName(cityName);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        LocationManager locationManager;
        LocationListener locationListener;

        locationManager = (LocationManager)
                mbaseContext.getSystemService(Context.LOCATION_SERVICE);

        if (displayGpsStatus()) {
            locationListener = new MyLocationListener();

            if (checkSelfPermission(mbaseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mbaseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
            }
            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);
        }

        return position;
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
