import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

// reads in xml file and populates a board
public class BoardCreator {
    //  VARIABLES

    // CONSTRUCTORS

    // GETS/SETS

    // METHODS
    public Board parseBoard() {
        Board newBoard = new Board();
        Room[] rooms = new Room[12];  // 12 rooms in a board

        try {
            // create documentbuilder (used to parse file)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // parse the board.xml
            File file = new File("board.xml");
            Document document = builder.parse(file);
            
            // normalize (makes the structure cleaner)
            document.getDocumentElement().normalize();
            
            // get the root
            // root is parent to all other elements
            Element root = document.getDocumentElement();

            // get all set nodes
            NodeList setList = root.getElementsByTagName("set");
            for (int i = 0; i < setList.getLength(); i++) {
                Node setNode = setList.item(i);
                if (setNode.getNodeType() == Node.ELEMENT_NODE) {
                    rooms[i] = new Set();

                    // scene attributes
                    Element set = (Element) setNode;
                    String setName = set.getAttribute("name");

                    // neighbor attributes
                    NodeList neighborsList = set.getElementsByTagName("neighbor");
                    String[] neighbors = new String[neighborsList.getLength()];
                    for (int j = 0; j < neighborsList.getLength(); j++) {
                        Element neighbor = (Element) neighborsList.item(j);
                        // assign neighors name
                        neighbors[i] = neighbor.getAttribute("name");
                    }
                    
                    // area attributes
                    Element area = (Element) set.getElementsByTagName("area").item(0);
                    String x = area.getAttribute("x");
                    String y = area.getAttribute("y");
                    String w = area.getAttribute("w");
                    String h = area.getAttribute("h");

                    // takes attributes
                    NodeList takeList = set.getElementsByTagName("take");
                    Take[] takes = new Take[takeList.getLength()];
                    for (int j = 0; j < takeList.getLength(); j++) {
                        Element take = (Element) takeList.item(j);
                        String takeNumber = take.getAttribute("number");

                        // take area
                        Element takeArea = (Element) take.getElementsByTagName("area"). item(0);
                        String takeX = takeArea.getAttribute("x");
                        String takeY = takeArea.getAttribute("y");
                        String takeW = takeArea.getAttribute("w");
                        String takeH = takeArea.getAttribute("h");
                        
                        // assign attributes
                        takes[j] = new Take();
                        takes[j].setNumber(Integer.parseInt(takeNumber));
                        takes[j].setX(Integer.parseInt(takeX));
                        takes[j].setY(Integer.parseInt(takeY));
                        takes[j].setW(Integer.parseInt(takeW));
                        takes[j].setH(Integer.parseInt(takeH));
                    }

                    // role (part) attributes
                    NodeList roleList = set.getElementsByTagName("part");
                    Role[] roles = new Role[roleList.getLength()];
                    for (int j = 0; j < roleList.getLength(); j++) {
                        Element role = (Element) roleList.item(j);
                        Role newRole = new Role();

                        // role attributes
                        String roleName = role.getAttribute("name");
                        String roleLevel = role.getAttribute("level");

                        // get dialouge
                        Element line = (Element) role.getElementsByTagName("line").item(0);
                        String roleDialogue = line.getTextContent().trim();

                        // get area
                        Element roleArea = (Element) role.getElementsByTagName("area").item(0);
                        String roleX = area.getAttribute("x");
                        String roleY = area.getAttribute("y");
                        String roleW = area.getAttribute("w");
                        String roleH = area.getAttribute("h");

                        // assign to newRole
                        newRole.setName(roleName);
                        newRole.setRankToAct(Integer.parseInt(roleLevel));
                        newRole.setStatus("active");  // on board
                        newRole.setPriority(0);  // no priority for bonuses
                        newRole.setOnCard(false);  // on board, NOT on card
                        newRole.setScene(null);  // no scenes associated with it
                        newRole.setDialogue(roleDialogue);
                        newRole.setX(Integer.parseInt(roleX));
                        newRole.setY(Integer.parseInt(roleY));
                        newRole.setW(Integer.parseInt(roleW));
                        newRole.setH(Integer.parseInt(roleH));

                        // populate roles array
                        roles[i] = newRole;
                    }
                    
                    // assign attributes
                    rooms[i].setName(setName);
                    rooms[i].setNeighbors(neighbors);
                    rooms[i].setX(Integer.parseInt(x));
                    rooms[i].setY(Integer.parseInt(y));
                    rooms[i].setW(Integer.parseInt(w));
                    rooms[i].setH(Integer.parseInt(h));
                    rooms[i].setTakes(takes);  // Set attribute only -- error?
                    rooms[i].setRoles(roles);  // Set attribute only -- error?
                }

                // casting office
                Element castingOffice = (Element) root.getElementsByTagName("office").item(0);
                rooms[11] = new CastingOffice();
                if (castingOffice != null) {
                    rooms[11].setName("Office");
                    
                    // neighbor attributes
                    NodeList castingOfficeNeighbors = castingOffice.getElementsByTagName("neighbor");
                    String[] neighbors = new String[castingOfficeNeighbors.getLength()];
                    for (int j = 0; j < castingOfficeNeighbors.getLength(); j++) {
                        Element neighbor = (Element) castingOfficeNeighbors.item(j);
                        // assign neighors name
                        neighbors[i] = neighbor.getAttribute("name");
                    }
                    rooms[11].setNeighbors(neighbors);

                    // extract area
                    Element castingOfficeArea = (Element) castingOffice.getElementsByTagName("area").item(0);
                    if (castingOfficeArea != null) {
                        String cox = castingOfficeArea.getAttribute("x");
                        String coy = castingOfficeArea.getAttribute("y");
                        String cow = castingOfficeArea.getAttribute("w");
                        String coh = castingOfficeArea.getAttribute("h");

                        // assignment
                        rooms[11].setX(Integer.parseInt(cox));
                        rooms[11].setY(Integer.parseInt(coy));
                        rooms[11].setW(Integer.parseInt(cow));
                        rooms[11].setH(Integer.parseInt(coh));
                    }

                    // extract ranks
                    NodeList rankList = castingOffice.getElementsByTagName("upgrade");
                    Rank[] ranks = new Rank[rankList.getLength()/2];
                    for (int j = 0; j < rankList.getLength(); j++) {
                        // extract rank
                        Element rank = (Element) rankList.item(j);
                        String level = rank.getAttribute("level");
                        String currencyType = rank.getAttribute("amt");
                        String cost = rank.getAttribute("currency");

                        // assign rank
                        if (currencyType == "dollar") {  // if currency is in dollars, create a new Rank()
                            Rank newRank = new Rank();
                            newRank.setRankLevel(Integer.parseInt(level));
                            newRank.setDollarCost(Integer.parseInt(cost));
                            ranks[j] = newRank;
                        } else {  // else, rank already exists, input credit cost into same level's rank
                            ranks[j-5].setCreditCost(Integer.parseInt(cost));
                        }
                    }
                    rooms[11].setRanks(ranks);  // CastingOffice attribute only -- error?
                }

                // trailer
                Element trailer = (Element) root.getElementsByTagName("trailer").item(0);
                rooms[12] = new Room();
                if (trailer != null) {
                    rooms[12].setName("Trailer");

                    // neighbor attributes
                    NodeList trailerNeighbors = trailer.getElementsByTagName("neighbor");
                    String[] neighbors = new String[trailerNeighbors.getLength()];
                    for (int j = 0; j < trailerNeighbors.getLength(); j++) {
                        Element neighbor = (Element) trailerNeighbors.item(j);
                        // assign neighors name
                        neighbors[i] = neighbor.getAttribute("name");
                    }
                    rooms[12].setNeighbors(neighbors);

                    // extract area
                    Element trailerArea = (Element) trailer.getElementsByTagName("area").item(0);
                    if (trailerArea != null) {
                        String tx = trailerArea.getAttribute("x");
                        String ty = trailerArea.getAttribute("y");
                        String tw = trailerArea.getAttribute("w");
                        String th = trailerArea.getAttribute("h");

                        // assignment
                        rooms[12].setX(Integer.parseInt(tx));
                        rooms[12].setY(Integer.parseInt(ty));
                        rooms[12].setW(Integer.parseInt(tw));
                        rooms[12].setH(Integer.parseInt(th));
                    }
                }
            }
            // assign board
            newBoard.setRooms(rooms);
            return newBoard;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
