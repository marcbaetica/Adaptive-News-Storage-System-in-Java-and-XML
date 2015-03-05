/**
 * Class Source is used to process and store the RSS sources in the database.There is no error-check on the name or the link
 * of the source. Assuming this process is handled by admin and everything passed is verified.
 * 
 * @author Group 6
 * @version 1.0
 */
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Source
{
    private Document dom;
    private NodeList nl;
    private Element eElement;
    private Element docEle;
    private Node nNode;
    private int maxID;
    
    /**
     * Constructor for objects of class Source
     */
    public Source()
    {
        ParseSource();
        maxID = getSourceCount();
    }
    
    /**
     * ParseSource is a method used for reading the database.xml file.
     */
    public void ParseSource()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("database.xml");
            docEle = dom.getDocumentElement();
            nl = docEle.getElementsByTagName("Sources");
            nNode = nl.item(0);
            eElement = (Element) nNode;
        }catch(ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * getSourceName is a method for extracting the name of the source based on the source id(starts from 1).
     * 
     * @param id ID number of the expected source.
     * @return Returns a String representation of the name of the source.
     */
    public String getSourceName(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return null;
        return eElement.getElementsByTagName("name").item(id).getTextContent();
    }
    
    /**
     * setSourceName is the method for editing the name of the source.
     * 
     * @param id ID number of the expected source.
     * @param name Name of the source that expected to changed to.
     * @return Returns a boolean variable stating if the change is successful.
     */
    public boolean setSourceName(int id,String name)
    {
        if(id <= 0 || id > maxID || name.length() < 1)
            return false;
        else {
            id--;
            eElement.getElementsByTagName("name").item(id).setTextContent(name);
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
    }
    
    /**
     * getSourceLink is a method for extracting the http address of the source based on the source id(starts from 1).
     * 
     * @param id ID number of the expected source.
     * @return Returns a String representation of the link of the source.
     */
    public String getSourceLink(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return null;
        return eElement.getElementsByTagName("link").item(id).getTextContent();
    }
    
    /**
     * setSourceLink is the method for editing the http link of the source.
     * 
     * @param id ID number of the expected source.
     * @param link Link of the source that expected to changed to.
     * @return Returns a boolean variable stating if the change is successful.
     */
    public boolean setSourceLink(int id,String link)
    {
        if(id <= 0 || id > maxID || link.length() < 1)
            return false;
        else {
            id--;
            eElement.getElementsByTagName("link").item(id).setTextContent(link);
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
    }
    
    /**
     * addSource method is used to add a new source to the database.
     * 
     * @param name Name of the source that needed to be added.
     * @param link Link of the source that needed to be added.
     * @return Returns a boolean variable stating if the adding is successful.
     */
    public boolean addSource(String name,String link)
    {
        try {
            if(name.length() > 0 && link.length() > 0) {
                Element sourceNode = dom.createElement("source");
                Element nameNode = dom.createElement("name");
                Element linkNode = dom.createElement("link");
                sourceNode.setAttribute("id", Integer.toString(getSourceCount()+1));
                nameNode.setTextContent(name);
                linkNode.setTextContent(link);
                sourceNode.appendChild(nameNode);
                sourceNode.appendChild(linkNode);
                nNode.appendChild(sourceNode);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(dom);
                StreamResult file = new StreamResult(new File("database.xml"));
                transformer.transform(source, file);
                return true;
            }
            else
                return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * removeSource method is used to remove an existing source to the database.
     * 
     * @param id ID number of the source that needed to be removed
     * @return Returns a boolean variable stating if the removing is successful.
     */
    public boolean removeSource(int id)
    {
        try {
            if(id > 0 && id <= maxID) {
                id--;
                nNode.removeChild(eElement.getElementsByTagName("link").item(id).getParentNode());
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(dom);
                StreamResult file = new StreamResult(new File("database.xml"));
                transformer.transform(source, file);
                return true;
            }
            else
                return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * getSourceCount is used to get the current number of sources that is in the database.
     * 
     * @return Returns the count of sources in the database.
     */
    public int getSourceCount()
    {
        return eElement.getElementsByTagName("source").getLength();
    }
}