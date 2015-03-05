/**
 * Location class will process the request for editing locations in the database. Assuming all the locations already exist in the database(a complete list).
 * 
 * @author Group 6
 * @version 1.0
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Location
{
    private Document dom;
    private NodeList nl;
    private Element eElement;
    private Element docEle;
    private Node nNode;
    private int maxID;
    
    /**
     * Constructor for objects of class Location
     */
    public Location()
    {
        ParseLocation();
        maxID = getLocationCount();
    }
    
    /**
     * ParseLocation is a method used for reading the locations from the database.xml file.
     */
    public void ParseLocation()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("database.xml");
            docEle = dom.getDocumentElement();
            nl = docEle.getElementsByTagName("Locations");
            nNode = nl.item(0);
            eElement = (Element) nNode;
        }catch(ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * searchLocation is used for searching a given location name.
     * 
     * @param name Name of the location that is passed for searching.
     * @return Returns the location ID of the given name.
     */
    public int searchLocation(String name)
    {
        if(name.length() > 0) {
            for(int i = 0;i < maxID;i++) {
                if(name.equals(eElement.getElementsByTagName("name").item(i).getTextContent()))
                    return i+1;
            }
            return -1;
        }
        else
            return -1;
    }
    
    /**
     * getNewsFromLocation will generate an ArrayList of news IDs of the given location name.
     * 
     * @param name Name of the expected location.
     * @return Returns an ArrayList of news IDs under the specified location.
     */
    public ArrayList<Integer> getNewsFromLocation(String name)
    {
        int index = searchLocation(name);
        ArrayList<Integer> newsList = new ArrayList<>();
        if(index != -1) {
            index--;
            String[] news = eElement.getElementsByTagName("ids").item(index).getTextContent().split(",");
            for(int i = 0;i < news.length;i++) {
                newsList.add(Integer.parseInt(news[i]));
            }
        }
        return newsList;
    }
    
    /**
     * addNewsToLocation will save the updated news list under the specified location.
     * 
     * @param id ID number of the news being added.
     * @param name Name of the location that the news belongs to.
     * @return Returns if the adding process is successful.
     */
    public boolean addNewsToLocation(int id,String name)
    {
        int index = searchLocation(name);
        if(id > 0 && index != -1) {
            index--;
            if(eElement.getElementsByTagName("ids").item(index).getTextContent().isEmpty())
                eElement.getElementsByTagName("ids").item(index).setTextContent(Integer.toString(id));
            else {
                eElement.getElementsByTagName("ids").item(index).setTextContent(eElement.getElementsByTagName("ids").item(index).getTextContent() + "," + Integer.toString(id));
            }
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(dom);
                StreamResult file = new StreamResult(new File("database.xml"));
                transformer.transform(source, file);
                return true;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        else
            return false;
    }
    
    /**
     * getLocationCount will return the count of different locations in the database.
     * 
     * @return Return the count of the locations.
     */
    public int getLocationCount()
    {
        return eElement.getElementsByTagName("location").getLength();
    }
}