import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

// reads in xml file and creates all 40 scenes, setting them in the scene array
public class SceneCreator {
    //  VARIABLES
    public Scene[] scenes;

    // CONSTRUCTORS

    // GETS/SETS

    // METHODS
    // parse the cards xml and populare scenes
    // NOTE: cards.xml MUST be in the same directory as SceneCreator.java
    public void parseSceneCards() {
        scenes = new Scene[40];  // 40 total scenes
        try {
            // create documentbuilder (used to parse file)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // parse the cards.xml
            File file = new File("cards.xml");
            Document document = builder.parse(file);
            
            // normalize (makes the structure cleaner)
            document.getDocumentElement().normalize();
            
            // get the root
            // root is parent to all other elements
            Element root = document.getDocumentElement();
            
            // get all card nodes
            NodeList nodeList = root.getElementsByTagName("card");
            
            // iterate over employee nodes and store info
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Scene newScene = new Scene();

                    // card attributes
                    Element card = (Element) node;
                    String name = card.getAttribute("name");
                    String img = card.getAttribute("img");
                    String budget = card.getAttribute("budget");
                    // assign attributes
                    newScene.setName(name);
                    newScene.setImg(img);
                    newScene.setBudget(Integer.parseInt(budget));

                    // scene attributes
                    Element scene = (Element) card.getElementsByTagName("scene").item(0);
                    String number = scene.getAttribute("number");
                    String description = scene.getTextContent().trim();
                    // assign attributes
                    newScene.setNumber(Integer.parseInt(number));
                    newScene.setDescription(description);

                    // get role info
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
