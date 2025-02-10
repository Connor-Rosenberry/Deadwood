import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

// reads in xml file and creates all 40 scenes (including roles), setting them in the scene array
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
            
            // for all scenes
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Scene newScene = new Scene();

                    // card attributes
                    Element card = (Element) node;
                    String sceneName = card.getAttribute("name");
                    String img = card.getAttribute("img");
                    String budget = card.getAttribute("budget");
                    // assign attributes
                    newScene.setName(sceneName);
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
                    NodeList partList = card.getElementsByTagName("part");
                    Role[] roles = new Role[partList.getLength()];

                    // for all parts
                    for (int j = 0; j <partList.getLength(); j++) {
                        Role newRole = new Role();

                        // part attributes
                        Element part = (Element) partList.item(j);
                        String roleName = part.getAttribute("name");
                        String level = part.getAttribute("level");

                        // get dialogue
                        Element line = (Element) part.getElementsByTagName("line").item(0);
                        String dialogue = line.getTextContent().trim();

                        // get area
                        Element area = (Element) part.getElementsByTagName("area").item(0);
                        String x = area.getAttribute("x");
                        String y = area.getAttribute("y");
                        String w = area.getAttribute("w");
                        String h = area.getAttribute("h");

                        // assign attributes
                        newRole.setName(roleName);
                        newRole.setRankToAct(Integer.parseInt(level));
                        newRole.setStatus("not active");  // not on board yet
                        newRole.setPriority(partList.getLength() - j);  // ordered in reverse priority
                        newRole.setOnCard(true);  // all these roles are from cards
                        newRole.setScene(newScene);  // set to current scene
                        newRole.setDialogue(dialogue);
                        newRole.setX(Integer.parseInt(x));
                        newRole.setY(Integer.parseInt(y));
                        newRole.setW(Integer.parseInt(w));
                        newRole.setH(Integer.parseInt(h))

                        // populate scenes roles array
                        roles[j] = newRole;
                    }

                    // assign roles array to scene
                    newScene.setRoles(roles);  // STOPPED HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
