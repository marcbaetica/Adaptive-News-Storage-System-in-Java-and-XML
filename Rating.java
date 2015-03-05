/**
 * This Rating class handles the "like" and "dislike" button in the system. It keeps track of both like and dislike for each piece of news.
 * 
 * @author Group 6
 * @version 1.0
 */
import java.io.File;
import java.io.IOException;
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

public class Rating
{
    private Document dom;
    private NodeList nl;
    private Element eElement;
    private Node nNode;
    private NewsStorage nc;
    private int maxID;
    /**
     * Constructor for objects of class Rating
     */
    public Rating()
    {
        ParseNews();
        nc = new NewsStorage();
        maxID = nc.getCurrentID();
    }
    
    /**
     * ParseNews is a method used for reading the database.xml file.
     */
    public void ParseNews()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("database.xml");
            Element docEle = dom.getDocumentElement();
            nl = docEle.getElementsByTagName("NewsList");
            nNode = nl.item(0);
            eElement = (Element) nNode;
        }catch(ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * getLike is used to get the count of the "like" given a specified news id.
     * 
     * @param id ID of the expected news.
     * @return Returns the count of the "like" of the given news.
     */
    public int getLike(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return -1;
        return Integer.parseInt(eElement.getElementsByTagName("like").item(id).getTextContent());
    }
    
    /**
     * incrementLike will increment the count of "like" once called.
     * 
     * @param id ID of the expected news.
     * @return Returns if the increment on "like" is successful.
     */
    public boolean incrementLike(int id)
    {
        int like = getLike(id);
        if(like == -1)
            return false;
        else {
            id--;
            like++;
            eElement.getElementsByTagName("like").item(id).setTextContent(Integer.toString(like));
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
     * getDislike is used to get the count of the "dislike" given a specified news id.
     * 
     * @param id ID of the expected news.
     * @return Returns the count of the "dislike" of the given news.
     */
    public int getDislike(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return -1;
        return Integer.parseInt(eElement.getElementsByTagName("dislike").item(id).getTextContent());
    }
    
    /**
     * incrementDislike will increment the count of "dislike" once called.
     * 
     * @param id ID of the expected news.
     * @return Returns if the increment on "dislike" is successful.
     */
    public boolean incrementDislike(int id)
    {
        int dislike = getDislike(id);
        if(dislike == -1)
            return false;
        else {
            id--;
            dislike++;
            eElement.getElementsByTagName("dislike").item(id).setTextContent(Integer.toString(dislike));
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
}