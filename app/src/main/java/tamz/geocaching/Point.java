package tamz.geocaching;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.util.List;

public class Point extends Marker {

    private String photoURL;
    private String description;
    private String name;
    private String markerColor;

    private boolean visited;

    public Point(double latitude, double longitude, String name, String description, String markerColor, int state) {
        this(latitude, longitude, name, description, markerColor, state, "");
    }


    public Point(double latitude, double longitude, String name, String description, String markerColor, int state, String photoURL) {
        super(MainActivity.map);

        this.name = name == null ? "" : name;
        this.description = description == null ? "" : description;
        this.photoURL = photoURL == null ? "" : photoURL;
        this.visited = (state!= 0);
        this.markerColor = markerColor == null ? "" : markerColor;

        super.setPosition(new GeoPoint(latitude, longitude));
        if (visited) {
            super.setIcon(resize(MainActivity.instance.getApplicationContext().getDrawable(R.drawable.visited)));
        } else {
            super.setIcon(resize(MainActivity.instance.getApplicationContext().getDrawable(R.drawable.notvisited)));
        }
        super.setAnchor(0.176f, 0.176f);

        List<GeoPoint> circle = Polygon.pointsAsCircle(this.getPosition(), MainActivity.maxDistance);
        Polygon p = new Polygon(MainActivity.map);
        p.setInfoWindow(null);
        p.setPoints(circle);
        MainActivity.map.getOverlayManager().add(p);

    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 100, 100, false);
        return new BitmapDrawable(MainActivity.instance.getResources(), bitmapResized);
    }

    public void markAsVisited(){
        this.visited = true;
        this.update();
    }


    public double haversine(double lat, double lon) {
        final int R = 6371000; // Radious of the earth
        Double lat1 = lat;
        Double lon1 = lon;
        Double lat2 = this.getPosition().getLatitude();
        Double lon2 = this.getPosition().getLongitude();
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return distance;
    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }


    public boolean insert() {
        PointsTableHelper.getInstance().insertPoint(this);
        return false;
    }

    public boolean update() {
        PointsTableHelper.getInstance().updatePoint(this);
        return false;
    }

    public static List<Point> getAllPoints(){
        return PointsTableHelper.getInstance().getAllPoints();
    }

    public static List<Point> getVisitedPoints(){
        return PointsTableHelper.getInstance().getVisitedPoints();
    }

    public static Point getPointByCoordinates(double lat, double lon) {
        return PointsTableHelper.getInstance().getPointByCoords(lat, lon);
    }



    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(String markerColor) {
        this.markerColor = markerColor;
    }

    public String getCoordinatesString() {
        StringBuilder result = new StringBuilder();

        result.append(Math.abs(getPosition().getLatitude()));
        result.append(getPosition().getLatitude() >= 0 ? "N " : "S ");
        result.append(Math.abs(getPosition().getLongitude()));
        result.append(getPosition().getLongitude() >= 0 ? "E" : "W");

        return result.toString();
    }

    @Override
    protected boolean onMarkerClickDefault(Marker marker, MapView mapView) {
        MainActivity.instance.showPointDetail(this.getPosition().getLatitude(), this.getPosition().getLongitude());
        return true;
    }

}
