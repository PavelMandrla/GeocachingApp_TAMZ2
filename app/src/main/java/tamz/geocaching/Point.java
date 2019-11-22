package tamz.geocaching;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        super.setIcon(App.getContext().getDrawable(R.drawable.lunar_module2));
        //super.setIcon(App.getContext().getDrawable(R.drawable.target));
        //super.getIcon().setColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY);
        super.setAnchor(0.176f, 0.176f);
        super.setInfoWindowAnchor(0.176f, 0.176f);
    }

    public void markAsVisited(){
        this.visited = true;
        this.update();
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




}
