package tamz.geocaching;

import android.app.Application;

public class App extends Application {
    private static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
}
