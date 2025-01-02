package me.redstoner2019.bingobackend.bingo;

import me.redstoner2019.bingobackend.bingoCard.BingoCard;
import me.redstoner2019.bingobackend.bingoCard.BingoCardController;
import me.redstoner2019.bingobackend.bingoPreset.BingoPreset;
import me.redstoner2019.bingobackend.bingoPreset.BingoPresetController;
import org.json.JSONArray;

import java.util.*;

public class Bingo {
    private HashMap<String, BingoField> fields = new HashMap<>();
    private List<String> bingoCards = new ArrayList<>();
    private final String bingoId;
    private final String creator;
    private String name;
    private final String presetId;

    public Bingo(String creator, String id){
        System.out.println("Creating Bingo");
        System.out.println("Fetching Preset...");

        Optional<BingoPreset> bpOpt = BingoPresetController.bingoPresetJpaRepositoryPublic.findById(id);

        System.out.println("Setting data");

        this.presetId = id;
        bingoId = UUID.randomUUID().toString();
        this.creator = creator;

        if(bpOpt.isPresent()){
            BingoPreset bp = bpOpt.get();
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
    }

    public void toggleCard(String username, String cardId){
        BingoField bf = fields.get(username);
    }

    public boolean hasPlayer(String username){
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
        if(!fields.containsKey(username)){

        }
    }

    public String getBingoId() {
        return bingoId;
    }
}
