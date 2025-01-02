package me.redstoner2019.bingobackend.bingo;

import me.redstoner2019.bingobackend.bingoCard.BingoCard;
import me.redstoner2019.bingobackend.util.HttpRequests;
import me.redstoner2019.bingobackend.util.TokenChecker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class BingoController {

    @PostMapping("/bingo/create")
    public ResponseEntity<String> create(@RequestBody String jsonString) {
        try{
            JSONObject json = new JSONObject(jsonString);

            if(!json.has("token")){
                return ResponseEntity.status(401).body("Token not found: Unauthorized");
            }

            if(!json.has("presetId")){
                return ResponseEntity.status(400).body("presetId not found");
            }

            JSONObject request = new JSONObject();
            request.put("token", json.getString("token"));

            JSONObject tokenInfo = HttpRequests.httpPost("http://158.220.105.209:8080/tokenInfo", request.toString());

            Bingo bingo = new Bingo(tokenInfo.getString("username"),json.getString("presetId"));

            BingoManager.addBingo(bingo);

            bingo.joinPlayer(tokenInfo.getString("username"));

            System.out.println("Joining player " + tokenInfo.getString("username") + " to game " + bingo.getBingoId());

            JSONObject result = new JSONObject();

            result.put("bingoId", bingo.getBingoId());

            return ResponseEntity.ok(result.toString());
        }catch (Exception e){
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/interact")
    public ResponseEntity<String> interact(@RequestBody String jsonString) {
        try{
            JSONObject json = new JSONObject(jsonString);

            if(!json.has("token")){
                return ResponseEntity.status(401).body("Token not found: Unauthorized");
            }

            if(!json.has("cardId")){
                return ResponseEntity.status(400).body("cardId not found");
            }

            if(!json.has("gameId")){
                return ResponseEntity.status(400).body("gameId not found");
            }

            JSONObject tokenInfo = TokenChecker.getTokenInfo(json.getString("token"));

            Bingo bingo = BingoManager.getBingo(tokenInfo.getString("gameId"));

            if(bingo.hasPlayer(tokenInfo.getString("username"))){
                bingo.toggleCard(tokenInfo.getString("username"),json.getString("cardId"));
            } else {
                return ResponseEntity.status(401).body("Not in this game: Unauthorized");
            }

            JSONObject result = new JSONObject();

            result.put("bingoId", bingo.getBingoId());

            return ResponseEntity.ok(result.toString());
        }catch (Exception e){
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/getAll")
    public ResponseEntity<String> getAll() {
        JSONObject response = new JSONObject();
        JSONArray ids = new JSONArray();
        for(Bingo b : BingoManager.getAllBingos()){
            ids.put(b.getBingoId());
            JSONObject bingoInfo = new JSONObject();
            bingoInfo.put("players",b.playerCount());
            bingoInfo.put("creator",b.getCreator());
            bingoInfo.put("name",b.getName());
            bingoInfo.put("presetId",b.getPresetId());
            response.put(b.getBingoId(),bingoInfo);
        }
        response.put("ids",ids);
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/bingo/get")
    public ResponseEntity<String> get(@RequestBody String jsonString) {
        try{
            JSONObject json = new JSONObject(jsonString);

            if(!json.has("token")){
                return ResponseEntity.status(401).body("Token not found: Unauthorized");
            }

            if(!json.has("id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing id");
            }

            Bingo bingo = BingoManager.getBingo(json.getString("id"));

            JSONObject result = new JSONObject();

            result.put("bingoId", bingo.getBingoId());
            result.put("name", bingo.getName());
            result.put("creator", bingo.getCreator());
            result.put("presetId", bingo.getPresetId());

            JSONArray cardsArray = new JSONArray();

            List<String> cards = new ArrayList<>(bingo.getBingoCards());
            //List<String> cards = List.copyOf(bingo.getBingoCards());

            Random random = new Random((TokenChecker.getUsername(json.getString("token")) + bingo.getBingoId()).hashCode());

            for (int i = 0; i < 25; i++) {
                if(!cards.isEmpty()){
                    if(cards.size() == 1){
                        cardsArray.put(cards.get(0));
                        cards.remove(0);
                        continue;
                    }
                    int index = random.nextInt(cards.size() - 1);
                    cardsArray.put(cards.get(index));
                    cards.remove(index);
                } else {
                    cardsArray.put(" - ");
                }
            }

            result.put("cards", cardsArray);
            return ResponseEntity.ok(result.toString());
        }catch (Exception e){
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/join")
    public ResponseEntity<String> join() {
        return ResponseEntity.ok("Hello World");
    }
}
