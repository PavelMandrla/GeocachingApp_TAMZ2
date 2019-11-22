package tamz.geocaching;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLManager {
    public void exportPointsToFile(String filename) {
        File outputFile = new File(App.getContext().getExternalFilesDir(null), filename);
        List<Point> points = Point.getAllPoints();

        //String photoURL

        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("points_of_interest");
            document.appendChild(root);

            for (Point p : points) {

                Element point = document.createElement("point");

                Element coordinates = document.createElement("coordinates");
                {
                    Element latitude = document.createElement("Latitude");
                    latitude.appendChild(document.createTextNode(Double.toString(p.getPosition().getLatitude())));
                    coordinates.appendChild(latitude);

                    Element longitude = document.createElement("Latitude");
                    longitude.appendChild(document.createTextNode(Double.toString(p.getPosition().getLatitude())));
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
            //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(document);
            transformer.transform(source, new StreamResult(outputFile));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}


/*

try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("point");
            document.appendChild(root);



        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

*/