package tamz.geocaching;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PointDetail extends Activity {
    ImageView image;
    ImageView status;
    TextView name;
    TextView coordinates;
    TextView description;

    Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        image = findViewById(R.id.pointDetail_image);
        status = findViewById(R.id.pointDetail_status);
        name = findViewById(R.id.pointDetail_name);
        coordinates = findViewById(R.id.pointDetail_coordinates);
        description = findViewById(R.id.pointDetail_description);

        Intent intent = getIntent();
        this.point = Point.getPointByCoordinates(intent.getDoubleExtra("latitude", 500), intent.getDoubleExtra("longitude", 500));
        if (point == null) {
            return;
        }
        name.setText(point.getName());
        coordinates.setText(point.getCoordinatesString());
        description.setText(point.getDescription());
        status.setImageResource(point.isVisited() ? R.drawable.success_border : R.drawable.cancel_border);
        if (!point.getPhotoURL().equals("")) {
            Picasso.get().load(point.getPhotoURL()).into(image);
        }

        status.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (point.isVisited()) {
                    playSound(R.raw.decline);
                    vibrate(1000);
                    return false;
                }
                Location last = MainActivity.instance.getLocation();
                if (last == null) {
                    playSound(R.raw.decline);
                    vibrate(100);
                    return false;
                }
                if (point.haversine(last.getLatitude(), last.getLongitude()) > MainActivity.maxDistance) {
                    playSound(R.raw.decline);
                    vibrate(500);
                    return false;
                }
                playSound(R.raw.accept);
                status.setImageResource(R.drawable.success_border);
                point.markAsVisited();

                return true;
            }
        });
    }

    private void playSound(int id) {
        final MediaPlayer mp = MediaPlayer.create(this, id);
        mp.start();
    }

    private void vibrate(int len){
        Vibrator vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(len, 255));
        } else {
            //deprecated in API 26
            vibrator.vibrate(len);
        }
    }

    public void coordsResult(View view) {
        Intent intent = new Intent();
        intent.putExtra("latitude", this.point.getPosition().getLatitude());
        intent.putExtra("longitude", this.point.getPosition().getLongitude());
        setResult(450, intent);
        finish();
    }
}
