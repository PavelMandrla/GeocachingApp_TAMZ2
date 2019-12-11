package tamz.geocaching;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PointDownloader extends Activity {
    private ListView filesList;
    private PointsFileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesList = findViewById(R.id.ListView_files);

        new PointsFilesXMLDownloader().execute("http://158.196.115.135/pointFiles.xml");//TODO zmenit IP adresu
    }

    private class PointsFilesXMLDownloader extends AsyncTask<String, Void, List<PointsFile>> {
        @Override
        protected List<PointsFile> doInBackground(String ... urls) {
            List<PointsFile> result = null;
            InputStream is;
            try {
                is = getUrlInputStream(urls[0]);
                result = XMLManager.getPointsFilesFromInputStream(is);

                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<PointsFile> pointsFiles) {
            setContentView(R.layout.activity_point_downloader);
            filesList = findViewById(R.id.ListView_files);
            adapter = new PointsFileAdapter(getApplicationContext(), R.layout.list_pointsfile_layout, pointsFiles);
            filesList.setAdapter(adapter);
            filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    filesList.getChildAt(position).setEnabled(false);
                    PointsFile pointsFile =(PointsFile) parent.getItemAtPosition(position);
                    new PointsXMLDownloader().execute(pointsFile.getUrl());
                    SharedPreferences preferences = MainActivity.instance.getApplicationContext().getSharedPreferences("pointFiles", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(pointsFile.getName(), pointsFile.getName());
                    editor.commit();
                }
            });
        }
    }

    private class PointsXMLDownloader extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            InputStream is;
            try {
                is = getUrlInputStream(strings[0]);
                XMLManager.importPointsFromInputStream(is);

                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast toast = Toast.makeText(getApplicationContext(), "collection downloaded", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    private InputStream getUrlInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        return conn.getInputStream();
    }
}