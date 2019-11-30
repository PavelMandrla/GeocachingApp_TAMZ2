package tamz.geocaching;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PointDownloader extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PointsFilesXMLDownloader().execute("http://192.168.0.18/pointFiles.xml");
    }

    private class PointsFilesXMLDownloader extends AsyncTask<String, Void, List<PointsFile>> {
        @Override
        protected List<PointsFile> doInBackground(String ... urls) {
            Log.d("TATATATA", "doInBackground: ");
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
        }

        private InputStream getUrlInputStream(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            return conn.getInputStream();
        }
    }


}
