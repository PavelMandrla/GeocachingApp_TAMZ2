package tamz.geocaching;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    // use -> FUSED LOCATION PROVIDER
    public static MapView map = null;
    public static Context context = null;
    public static MainActivity instance = null;
    Button button = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        context = getApplicationContext();
        map.setTileSource(TileSourceFactory.MAPNIK);

        button = (Button) findViewById(R.id.button3);
        instance = this;


        Point p = new Point(49.831, 18.161, "name", "", "", 1, "");

        //new Point(49.831, 18.161, "FEI", "Fakulta elektrotechniky a informatiky", "", 0, "").insert();
        //new Point(49.833, 18.161, "CPIT", "Centrum pokročilých inovačních technologií", "", 0, "").insert();
        //new Point(49.833, 18.164, "Rektorát VŠB-TUO", "Rektorát Vysoké školy báňské - Technické univerzity Ostrava", "", 0, "").insert();
        //new Point(49.8339, 18.1626, "Kruhovka", "Budova C v kampusu všb-tuo. Má v sobě nějaké posluchárny a mají tam výborné bagety. 10/10", "", 0, "https://upload.wikimedia.org/wikipedia/commons/f/f7/TU_Ostrava.JPG").insert();

        XMLManager xmlManager = new XMLManager();
        xmlManager.exportPointsToFile("points_export2.xml");

        Log.d("POINT", ((ArrayList<Point>) Point.getAllPoints()).size() + "");

        map.getOverlays().addAll(Point.getAllPoints());
        map.invalidate();

        map.getController().setZoom(15.0);
        map.getController().setCenter(p.getPosition());

    }

    public void onResume(){
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
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

}