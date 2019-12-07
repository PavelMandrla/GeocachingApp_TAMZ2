package tamz.geocaching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PointAdapter extends ArrayAdapter<Point> {
    private int layoutResourceId;
    private List<Point> data = null;

    public PointAdapter(@NonNull Context context, int resource, @NonNull List<Point> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PointHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) MainActivity.instance.getApplicationContext().getSystemService(MainActivity.instance.getApplicationContext().LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PointHolder();
            holder.name = (TextView)row.findViewById(R.id.listEntry_pointName);
            holder.coordinates = row.findViewById(R.id.listEntry_coordinates);
            holder.status = row.findViewById(R.id.listEntry_status);

            row.setTag(holder);
        } else {
            holder = (PointHolder)row.getTag();
        }

        Point point = data.get(position);

        holder.name.setText(point.getName());
        holder.coordinates.setText(point.getCoordinatesString());
        holder.status.setImageResource(point.isVisited() ? R.drawable.success : R.drawable.cancel);

        return row;
    }

    private static class PointHolder {
        TextView name;
        TextView coordinates;
        ImageView status;
    }

    public List<Point> getData() {
        return data;
    }

    public void setData(List<Point> data) {
        this.data = data;
    }
}
