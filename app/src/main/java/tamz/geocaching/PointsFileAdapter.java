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

public class PointsFileAdapter extends ArrayAdapter<PointsFile> {
    private int layoutResourceId;
    private List<PointsFile> data;

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
            LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(App.getContext().LAYOUT_INFLATER_SERVICE);
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
        holder.downloadedStatus.setImageResource(R.drawable.success);

        return row;
    }

    class PointsFileHolder {
        TextView name;
        ImageView downloadedStatus;
    }
}
