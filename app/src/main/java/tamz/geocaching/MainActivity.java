package tamz.geocaching;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    // use -> FUSED LOCATION PROVIDER
    public static MapView map = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        Point p = new Point(49.8183, 18.2219, "name", "", "", 1, "");

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
}