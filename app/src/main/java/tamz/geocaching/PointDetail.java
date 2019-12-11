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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PointDetail extends Activity {
    ImageView image;
    ImageView status;
    TextView name;
    TextView coordinates;
    TextView description;
    Button markAsVisitedButton;

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
        markAsVisitedButton = findViewById(R.id.pointDetail_markAsVisitedButton);

        Intent intent = getIntent();
        this.point = Point.getPointByCoordinates(intent.getDoubleExtra("latitude", 500), intent.getDoubleExtra("longitude", 500));
        if (point == null) {
            return;
        }
        name.setText(point.getName());
        coordinates.setText(point.getCoordinatesString());
        description.setText(point.getDescription());
        status.setImageResource(point.isVisited() ? R.drawable.success_border : R.drawable.cancel_border);
        if (point.isVisited()) {
            markAsVisitedButton.setVisibility(View.GONE);
        }
        if (!point.getPhotoURL().equals("")) {
            Picasso.get().load(point.getPhotoURL()).into(image);
        }

        markAsVisitedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (point.isVisited()) {
                    playSound(R.raw.decline);
                    vibrate(500);
                    return;
                }
                Location last = MainActivity.instance.getLocation();
                if (last == null) {
                    playSound(R.raw.decline);
                    vibrate(500);
                    return;
                }
                if (point.haversine(last.getLatitude(), last.getLongitude()) > MainActivity.maxDistance) {
                    playSound(R.raw.decline);
                    vibrate(500);
                    return;
                }
                playSound(R.raw.accept);
                status.setImageResource(R.drawable.success_border);
                point.markAsVisited();
                markAsVisitedButton.setVisibility(View.GONE);
                return;
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
