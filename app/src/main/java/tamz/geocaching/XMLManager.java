package tamz.geocaching;

import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLManager {
    public static void exportPointsToFile(Uri uri) {
        List<Point> points = Point.getVisitedPoints();

        try (OutputStream os = MainActivity.instance.getApplicationContext().getContentResolver().openOutputStream(uri)){
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("points_of_interest");
            document.appendChild(root);

            for (Point p : points) {

                Element point = document.createElement("point");

                Element coordinates = document.createElement("coordinates");
                {
                    Element latitude = document.createElement("latitude");
                    latitude.appendChild(document.createTextNode(Double.toString(p.getPosition().getLatitude())));
                    coordinates.appendChild(latitude);

                    Element longitude = document.createElement("longitude");
                    longitude.appendChild(document.createTextNode(Double.toString(p.getPosition().getLongitude())));
                    coordinates.appendChild(longitude);
                }
                point.appendChild(coordinates);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(p.getName()));
                point.appendChild(name);

                Element description = document.createElement("description");
                description.appendChild(document.createTextNode(p.getDescription()));
                point.appendChild(description);

                Element markerColor = document.createElement("marker_color");
                markerColor.appendChild(document.createTextNode(p.getMarkerColor()));
                point.appendChild(markerColor);

                Element visited = document.createElement("visited");
                visited.appendChild(document.createTextNode(Boolean.toString(p.isVisited())));
                point.appendChild(visited);

                Element photoUrl = document.createElement("photo_URL");
                photoUrl.appendChild(document.createTextNode(p.getPhotoURL()));
                point.appendChild(photoUrl);

                root.appendChild(point);

            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult();
            streamResult.setOutputStream(os);
            transformer.transform(source, streamResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importPointsFromFile(Uri uri) {
        try (InputStream is = MainActivity.instance.getApplicationContext().getContentResolver().openInputStream(uri)) {
            importPointsFromInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PointsFile> getPointsFilesFromInputStream(InputStream is) {
        List<PointsFile> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(is);
            Element root = document.getDocumentElement();

            NodeList pointsFiles = root.getElementsByTagName("pointsFile");
            for (int i = 0; i < pointsFiles.getLength(); i++) {
                Node pointsFileNode = pointsFiles.item(i);

                String name = ((Element) pointsFileNode).getElementsByTagName("name").item(0).getTextContent();
                String fileURL = ((Element) pointsFileNode).getElementsByTagName("url").item(0).getTextContent();
                result.add(new PointsFile(name, fileURL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void importPointsFromInputStream(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(is);
        Element root = document.getDocumentElement();

        NodeList points = root.getElementsByTagName("point");
        for (int i = 0; i < points.getLength(); i++) {
            Node pointNode = points.item(i);

            Element coorinates = (Element) ((Element) pointNode).getElementsByTagName("coordinates").item(0);
            double lat = Double.parseDouble(coorinates.getElementsByTagName("latitude").item(0).getTextContent());
            double lon = Double.parseDouble(coorinates.getElementsByTagName("longitude").item(0).getTextContent());

            String name = ((Element) pointNode).getElementsByTagName("name").item(0).getTextContent();
            String description = ((Element) pointNode).getElementsByTagName("description").item(0).getTextContent();
            String markerColor = ((Element) pointNode).getElementsByTagName("marker_color").item(0).getTextContent();
            String photoUrl = ((Element) pointNode).getElementsByTagName("photo_URL").item(0).getTextContent();
            boolean visited = Boolean.parseBoolean(((Element) pointNode).getElementsByTagName("visited").item(0).getTextContent());

            new Point(lat, lon, name, description, markerColor, visited ? 1 : 0, photoUrl).insert();
        }
    }
}
