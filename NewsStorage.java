/**
 * NewsStorage handles reading and editing the news related information in the database.
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

public class NewsStorage
{
    private Document dom;
    private NodeList nl;
    private Element eElement;
    private Node nNode;
    private int maxID;
    /**
     * Constructor for objects of class NewsStorage
     */
    public NewsStorage()
    {
        ParseNews();
        maxID = getCurrentID();
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
     * addNewsToFile is a simulated process for adding the newly pulled news to the database file.
     * 
     * @param ele A Node sent from the News class containing all the information of the news.
     * @return Returns the ID number of the newly added news back to News class.
     */
    public int addNewsToFile(Element ele)
    {
        if(ele != null) {
            int id = getCurrentID()+1;
            Element newsNode = dom.createElement("news");
            newsNode.setAttribute("id", Integer.toString(id));
            newsNode.appendChild(createNode("title",ele.getElementsByTagName("title").item(0).getTextContent()));
            newsNode.appendChild(createNode("body",ele.getElementsByTagName("body").item(0).getTextContent()));
            newsNode.appendChild(createNode("year",ele.getElementsByTagName("year").item(0).getTextContent()));
            newsNode.appendChild(createNode("month",ele.getElementsByTagName("month").item(0).getTextContent()));
            newsNode.appendChild(createNode("day",ele.getElementsByTagName("day").item(0).getTextContent()));
            newsNode.appendChild(createNode("time",ele.getElementsByTagName("time").item(0).getTextContent()));
            newsNode.appendChild(createNode("like","0"));
            newsNode.appendChild(createNode("dislike","0"));
            nNode.appendChild(newsNode);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(dom);
                StreamResult file = new StreamResult(new File("database.xml"));
                transformer.transform(source, file);
                return id;
            }catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        else
            return -1;
    }
    
    /**
     * createNode is used within NewsStorage class for generating a node containing neccessary information(not important,just for easy implementation).
     * 
     * @param name Name of the tag that needed to be added to the database.
     * @param content Content of the node that needed to be added.
     * @return Returns a processed node containing neccessary information for adding.
     */
    private Element createNode(String name,String content)
    {
        Element newNode = dom.createElement(name);
        newNode.setTextContent(content);
        return newNode;
    }
    
    /**
     * getTitle will return the title of the specified news.
     * 
     * @param id ID number of the news.
     * @return Returns the title of the news that is being passed.
     */
    public String getTitle(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return null;
        return eElement.getElementsByTagName("title").item(id).getTextContent();
    }
    
    /**
     * getContent will return the content of the given news.
     * 
     * @param id ID number of the news.
     * @return Returns the content of the news that is being passed.
     */
    public String getContent(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return null;
        return eElement.getElementsByTagName("body").item(id).getTextContent();
    }
    
    /**
     * getTime will return a formatted time when the expected news happened.
     * 
     * @param id ID number of the news.
     * @return Returns a string representation of the time when the news happened.
     */
    public String getTime(int id)
    {
        if(id > 0 && id <= maxID)
            id--;
        else
            return null;
        return eElement.getElementsByTagName("month").item(id).getTextContent() + "/" + 
            eElement.getElementsByTagName("day").item(id).getTextContent() + "/" +
            eElement.getElementsByTagName("year").item(id).getTextContent() + " " +
            eElement.getElementsByTagName("time").item(id).getTextContent();
    }
    
    /**
     * getCurrentID will return the current count of the news that is in the database.
     * 
     * @return Returns the count of the news in the database.
     */
    public int getCurrentID()
    {
        return eElement.getElementsByTagName("news").getLength();
    }
}