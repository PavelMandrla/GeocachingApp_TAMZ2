package tamz.geocaching;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class PointListActivity extends Activity {

    private static final int CREATE_FILE_REQUEST_CODE = 900;
    private static final int CHOOSE_FILE_REQUEST_CODE = 901;
    private static final int SHOW_POINT_DETAIL_REQUEST_CODE = 401;

    ListView pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        pointsList = findViewById(R.id.ListView_points);
        pointsList.setAdapter(new PointAdapter(App.getContext(), R.layout.list_point_layout, Point.getAllPoints()));

        pointsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Point point = (Point) adapterView.getItemAtPosition(i);
                Intent pointDetailIntent = new Intent(getApplicationContext(), PointDetail.class);
                pointDetailIntent.putExtra("latitude", point.getPosition().getLatitude());
                pointDetailIntent.putExtra("longitude", point.getPosition().getLongitude());
                startActivityForResult(pointDetailIntent, SHOW_POINT_DETAIL_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.points_list_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode != 0) {
            Uri uri = data.getData();
            if (uri == null) {
                Toast toast = Toast.makeText(App.getContext(), "Problem while saving file", Toast.LENGTH_LONG);
                toast.show();
            } else {
                XMLManager.exportPointsToFile(uri);

                Toast toast = Toast.makeText(App.getContext(), "Points saved to " + uri.getPath(), Toast.LENGTH_LONG);
                toast.show();
            }

        } else if (requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode != 0) {
            Uri uri = data.getData();
            if (uri == null) {
                pointsList.setAdapter(new PointAdapter(App.getContext(), R.layout.list_point_layout, Point.getAllPoints()));
                Toast toast = Toast.makeText(App.getContext(), "Problem while opening file", Toast.LENGTH_LONG);
                toast.show();
            } else {
                XMLManager.importPointsFromFile(uri);

                Toast toast = Toast.makeText(App.getContext(), "Imported points from " + uri.getPath(), Toast.LENGTH_LONG);
                toast.show();
            }
        } else if (requestCode == SHOW_POINT_DETAIL_REQUEST_CODE && resultCode != 0) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pointsListMenu_options:

                break;
            case R.id.pointsListMenu_importXML:
                Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFileIntent.setType("text/xml");
                startActivityForResult(chooseFileIntent, CHOOSE_FILE_REQUEST_CODE);
                break;
            case R.id.pointsListMenu_exportXML:
                Intent createFileIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                createFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                createFileIntent.setType("text/xml");
                createFileIntent.putExtra(Intent.EXTRA_TITLE, "testFile.xml");
                startActivityForResult(createFileIntent, CREATE_FILE_REQUEST_CODE);
                break;
            case R.id.pointsListMenu_pointsDownload:
                Intent downloadPointsIntent = new Intent(getApplicationContext(), PointDownloader.class);
                startActivity(downloadPointsIntent);
                break;
        }
        return true;
    }
}
