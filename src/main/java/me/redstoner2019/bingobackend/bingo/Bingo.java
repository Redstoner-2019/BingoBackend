package me.redstoner2019.bingobackend.bingo;

import me.redstoner2019.bingobackend.bingoCard.BingoCard;
import me.redstoner2019.bingobackend.bingoCard.BingoCardController;
import me.redstoner2019.bingobackend.bingoPreset.BingoPreset;
import me.redstoner2019.bingobackend.bingoPreset.BingoPresetController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Bingo {
    private HashMap<String, BingoField> fields = new HashMap<>();
    private List<String> bingoCards = new ArrayList<>();
    private final String bingoId;
    private final String creator;
    private String name;
    private final String presetId;
    private HashMap<String, JSONArray> notifications = new HashMap<>();
    private long lastInteraction = System.currentTimeMillis();
    private BingoPreset bp;

    public Bingo(String creator, String id){
        System.out.println("Creating Bingo");
        System.out.println("Fetching Preset...");

        Optional<BingoPreset> bpOpt = BingoPresetController.bingoPresetJpaRepositoryPublic.findById(id);

        System.out.println("Setting data");

        this.presetId = id;
        bingoId = UUID.randomUUID().toString();
        this.creator = creator;

        if(bpOpt.isPresent()){
            bp = bpOpt.get();
            this.name=bp.getName();
            JSONArray cards = new JSONArray(bp.getCards());

            System.out.println("Fetching Cards...");
            for (int i = 0; i < cards.length(); i++) {
                Optional<BingoCard> bcOpt = BingoCardController.bingoCardJpaRepositoryPublic.findById(cards.getString(i));

                System.out.println("[" + (i + 1) + "/" + cards.length() + "]");

                if(bcOpt.isPresent()){
                    BingoCard bc = bcOpt.get();
                    bingoCards.add(bc.getText());
                }
            }

            System.out.println("Complete. - " + id);
        }
    }

    public Bingo(List<String> bingoCards, String creator, String name, String presetId){
        this.bingoCards = bingoCards;
        this.creator = creator;
        //this.name = name;
        this.presetId = presetId;
        bingoId = UUID.randomUUID().toString();
        this.lastInteraction = System.currentTimeMillis();
    }

    public BingoField getBf(String username){
        this.lastInteraction = System.currentTimeMillis();
        return fields.get(username);
    }

    public void toggleCard(String username, int x0, int y0){
        this.lastInteraction = System.currentTimeMillis();
        BingoField bf = fields.get(username);
        bf.setSelected(x0,y0,!bf.getSelected(x0,y0));

        for (int x = 0; x < bp.getWidth(); x++) {
            for (int y = 0; y < bp.getWidth(); y++) {
                if(!bf.getSelected(x,y)) {
                    bf.clearBingoAchieved("x=" + x);
                    break;
                }
                if(y == bp.getWidth() - 1) {
                    if(!bf.isBingoAchieved("x=" + x))  {
                        System.out.println("Bingo Found x = " + x);
                        bf.addBingoAchieved("x=" + x);
                        for(String s : notifications.keySet()){
                            JSONArray notification = notifications.get(s);
                            JSONObject noti = new JSONObject();
                            noti.put("type","bingo");
                            noti.put("username",username);
                            notification.put(noti);
                        }
                    }
                }
            }
        }

        for (int y = 0; y < bp.getWidth(); y++) {
            for (int x = 0; x < bp.getWidth(); x++) {
                if(!bf.getSelected(x,y)) {
                    bf.clearBingoAchieved("y=" + y);
                    break;
                }
                if(x == bp.getWidth() - 1) {
                    if(!bf.isBingoAchieved("y=" + y))  {
                        System.out.println("Bingo Found y = " + y);
                        bf.addBingoAchieved("y=" + y);
                        for(String s : notifications.keySet()){
                            JSONArray notification = notifications.get(s);
                            JSONObject noti = new JSONObject();
                            noti.put("type","bingo");
                            noti.put("username",username);
                            notification.put(noti);
                        }
                    }
                }
            }
        }


        for (int i = 0; i < bp.getWidth(); i++) {
            if(!bf.getSelected(i,i)) {
                bf.clearBingoAchieved("diagonal-top-left");
                break;
            }
            if(i == bp.getWidth() - 1) {
                if(!bf.isBingoAchieved("diagonal-top-left"))  {
                    System.out.println("Bingo diagonal top left found");
                    bf.addBingoAchieved("diagonal-top-left");
                    for(String s : notifications.keySet()){
                        JSONArray notification = notifications.get(s);
                        JSONObject noti = new JSONObject();
                        noti.put("type","bingo");
                        noti.put("username",username);
                        notification.put(noti);
                    }
                }
            }
        }

        for (int i = 0; i < bp.getWidth(); i++) {
            if(!bf.getSelected(bp.getWidth()-1-i,i)) {
                bf.clearBingoAchieved("diagonal-top-right");
                break;
            }
            if(i == bp.getWidth()-1) {
                if(!bf.isBingoAchieved("diagonal-top-right"))  {
                    System.out.println("Bingo diagonal top right found");
                    bf.addBingoAchieved("diagonal-top-right");
                    for(String s : notifications.keySet()){
                        JSONArray notification = notifications.get(s);
                        JSONObject noti = new JSONObject();
                        noti.put("type","bingo");
                        noti.put("username",username);
                        notification.put(noti);
                    }
                }
            }
        }
    }

    public JSONArray getNotifications(String username) {
        this.lastInteraction = System.currentTimeMillis();
        return notifications.getOrDefault(username, new JSONArray());
    }

    public void clearNotifications(String username) {
        this.lastInteraction = System.currentTimeMillis();
        notifications.put(username, new JSONArray());
    }

    public boolean hasPlayer(String username){
        this.lastInteraction = System.currentTimeMillis();
        return fields.containsKey(username);
    }

    public int playerCount(){
        return fields.size();
    }

    public String getCreator(){
        return creator;
    }

    public String getName() {
        return name;
    }

    public String getPresetId() {
        return presetId;
    }

    public List<String> getBingoCards() {
        return bingoCards;
    }

    public void joinPlayer(String username){
        this.lastInteraction = System.currentTimeMillis();
        if(!fields.containsKey(username)){
            fields.put(username, new BingoField(bp.getWidth()));
            notifications.put(username, new JSONArray());

            System.out.println("Joined player " + username + " to game " + getBingoId());
        }
    }

    public String getBingoId() {
        return bingoId;
    }

    public long getLastInteraction() {
        return lastInteraction;
    }
}
