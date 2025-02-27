# Deadwood
Implementation of the board game deadwood into a computer game

for instructions on how the game works check "deadwood-free-edition-rules.pdf"

In Order to compile and run this porgram successfully Please:
  1. Download all files from the github repository
  2. Compile all of the .java files (javac)
  3. Run the Deadwood.java file (java Deadwood.java)
  4. Enter the number of players
  5. Enjoy the game

# Commands
- move <destination>: move the active player to an adjacent room
- all players locations: displays the location of all players
- active player: displays information about the active player, such as their name, locaion, dollars & credits, rank, and role (if they have one)
- adjacent rooms: displays the adjacent rooms from the active player (the valid move destinations if the player were to move)
- work: displays the roles avaliable to work for the active player
- work <role>: assigns the active player a role (if they meet the requirements to work there)
- act: active player "acts" their role (if they have a role)
- rehearse: active player "rehearses" their role, gaining them one practice chip (if they have a role)
- upgrade: allows the active player to increase their rank, if they are in the Casting Office and meet the requirements
- end turn: ends the active players turn
- end day: ends the current day and starts a new day
- end game: ends the game and moves to scoring
- help: lists the avaliable commands listed above

# Information Files
- The DeadwoodA2ClassDiagram.png displays a diagram for all of the classes used in the deadwood game and how each of them is related to each other
- The cards.xml file holds the information for the 40 scene cards used in the game
- The board.xml file holds the information for the 12 rooms used in the game
