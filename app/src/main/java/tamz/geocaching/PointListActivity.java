package tamz.geocaching;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PointListActivity extends Activity {

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
                //startActivityForResult(pointDetailIntent, 456);
                startActivityForResult(pointDetailIntent, 401);
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
        setResult(resultCode, data);
        finish();
    }
}
