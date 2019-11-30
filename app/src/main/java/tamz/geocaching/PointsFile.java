package tamz.geocaching;

public class PointsFile {
    private String name;
    private String url;

    public PointsFile(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
