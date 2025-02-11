public class Role {
    // VARIABLES
    private String name;
    private int rankToAct;
    private String status;  // "not active", "active", or "wrapped"
    private int priority;
    private boolean onCard;
    private int practiceChips;
    private Player actor;
    private Scene scene;
    private String dialogue;
    private int x;
    private int y;
    private int w;
    private int h;

    // CONSTRUCTORS
    public Role(String name, int rankToAct, String status, int priority, boolean onCard, int practiceChips, Player actor, Scene scene, String dialogue, int x, int y, int w, int h) {
        this.name = name;
        this.rankToAct = rankToAct;
        this.status = status;
        this.priority = priority;
        this.onCard = onCard;
        this.practiceChips = practiceChips;
        this.actor = actor;
        this.scene = scene;
        this.dialogue = dialogue;
        this.x = x;
        this.y = y;
        this.w = w;
        this.y = y;
    }

    //GETS/SETS
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRankToAct() {
        return this.rankToAct;
    }

    public void setRankToAct(int rankToAct) {
        this.rankToAct = rankToAct;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getOnCard() {
        return this.onCard;
    }

    public void setOnCard(boolean onCard) {
        this.onCard = onCard;
    }

    public int getPracticeChips() {
        return this.practiceChips;
    }

    public void setPracticeChips(int practiceChips) {
        this.practiceChips = practiceChips;
    }

    public Player getActor() {
        return this.actor;
    }

    public void setActor(Player actor) {
        this.actor = actor;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    // METHODS
    // #TODO
    public void takeRole(Player player) {

    }

    // #TODO
    public void finishRole() {

    }

    public void addPracticeChip() {
        this.practiceChips++;
    }
}