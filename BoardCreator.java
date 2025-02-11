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
                    NodeList neighbors = set.getElementsByTagName("neighbor");
                    for (int j = 0; j < neighbors.getLength(); j++) {
                        Element neighbor = (Element) neighbors.item(j);
                        String neighborName = neighbor.getAttribute("name");
                        // TODO FIGURE OUT HOW TO STORE THIS -- just as a string?
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
                    
                    // assign attributes
                    rooms[i].setName(setName);
                    rooms[i].setNeighbors();  // TODO
                    rooms[i].setX(Integer.parseInt(x));
                    rooms[i].setY(Integer.parseInt(y));
                    rooms[i].setW(Integer.parseInt(w));
                    rooms[i].setH(Integer.parseInt(h));
                    rooms[i].setTakes(takes);
                }

                // casting office
                Element castingOffice = (Element) root.getElementsByTagName("office").item(0);
                rooms[11] = new CastingOffice();
                if (castingOffice != null) {
                    rooms[11].setName("Office");
                    // extract neighbors
                    NodeList castingOfficeNeighbors = castingOffice.getElementsByTagName("neighbor");
                    for (int j = 0; j < castingOfficeNeighbors.getLength(); j++) {
                        Element neighbor = (Element) castingOfficeNeighbors.item(j);
                        // TODO ASSIGN SOMEHOW
                    }

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
                    }
                    rooms[11].setRanks(ranks);  // TODO fix?
                }

                // trailer
            }
            // assign board
            newBoard.setRooms(rooms);
            return newBoard;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
}
