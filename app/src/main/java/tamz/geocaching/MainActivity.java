package tamz.geocaching;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MainActivity extends Activity {
    public static final double maxDistance = 50;

    public static MapView map = null;
    public static Context context = null;
    public static MainActivity instance = null;
    GpsMyLocationProvider locationProvider = null;
    MyLocationNewOverlay locationOverlay;
    Button button = null;
    private int REQUEST_LOCATION_CODE = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        context = getApplicationContext();
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        instance = this;

        //PointsTableHelper.getInstance().clearDB();
        checkPermissions();

        locationProvider = new GpsMyLocationProvider(ctx);
        locationOverlay = new MyLocationNewOverlay(locationProvider, map);
        locationOverlay.setDrawAccuracyEnabled(false);


        map.getOverlays().addAll(Point.getAllPoints());
        map.getOverlays().add(locationOverlay);
        map.invalidate();

        map.getController().setZoom(15.0);

        GeoPoint centre;
        Location last = locationProvider.getLastKnownLocation();
        if (last != null) {
            centre = new GeoPoint(last.getLatitude(), last.getLongitude());
        } else {
            centre = new GeoPoint(49.831, 18.161);
        }

        map.getController().setCenter(centre);
    }

    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 450) {
            double lat = data.getDoubleExtra("latitude", 0);
            double lon = data.getDoubleExtra("longitude", 0);
            map.getController().setCenter(new GeoPoint(lat, lon));
            map.getController().setZoom(20.0);
        }
        map.getOverlays().clear();
        map.getOverlays().addAll(Point.getAllPoints());
        map.getOverlays().add(locationOverlay);
        map.invalidate();
    }

    public void showPointsList(View view) {
        Intent i = new Intent(getApplicationContext(), PointListActivity.class);
        startActivityForResult(i, 350);
    }

    public void showPointDetail(double lat, double lon) {
        Intent pointDetailIntent = new Intent(getApplicationContext(), PointDetail.class);
        pointDetailIntent.putExtra("latitude", lat);
        pointDetailIntent.putExtra("longitude", lon);
        startActivityForResult(pointDetailIntent, 401);
    }

    public Location getLocation() {
        return this.locationProvider.getLastKnownLocation();
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }
    }

    public void goToCurrentLocation(View view) {
        Location last = locationProvider.getLastKnownLocation();
        if (last != null) {
            //map.getController().setCenter(new GeoPoint(last.getLatitude(), last.getLongitude()));
            map.getController().animateTo(new GeoPoint(last.getLatitude(), last.getLongitude()));
        } else {
            Vibrator vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, 255));
            } else {
                //deprecated in API 26
                vibrator.vibrate(50);
            }
        }
    }
}