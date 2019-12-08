package tamz.geocaching;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PointsFileAdapter extends ArrayAdapter<PointsFile> {
    private int layoutResourceId;
    private List<PointsFile> data;
    private SharedPreferences preferences = MainActivity.instance.getApplicationContext().getSharedPreferences("pointFiles", 0);

    public PointsFileAdapter(@NonNull Context context, int resource, @NonNull List<PointsFile> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PointsFileHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) MainActivity.instance.getApplicationContext().getSystemService(MainActivity.instance.getApplicationContext().LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PointsFileHolder();
            holder.name = row.findViewById(R.id.listEntry_pointsFileName);
            holder.downloadedStatus = row.findViewById(R.id.listEntry_pointsFileStatus);
        } else {
            holder = (PointsFileHolder) row.getTag();
        }

        PointsFile pointsFile = data.get(position);
        holder.name.setText(pointsFile.getName());

        //TODO -> nastavit obrazek a mozna i disabled na pole, pokud je uz kolekce bodu stazena


        if (preferences.getString(pointsFile.getName(), null) != null) {
            //holder.downloadedStatus.setImageResource(R.drawable.cancel);
        } else {
            holder.downloadedStatus.setImageResource(R.drawable.download);
        }

        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        if (preferences.getString(data.get(position).getName(), null) != null) {
            return false;
        }
        return true;
    }



    class PointsFileHolder {
        TextView name;
        ImageView downloadedStatus;
    }
}
