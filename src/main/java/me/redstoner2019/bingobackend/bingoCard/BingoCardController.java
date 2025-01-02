package me.redstoner2019.bingobackend.bingoCard;

import me.redstoner2019.bingobackend.bingoPreset.BingoPreset;
import me.redstoner2019.bingobackend.util.TokenChecker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class BingoCardController {

    private final BingoCardJpaRepository bingoCardJpaRepository;
    public static BingoCardJpaRepository bingoCardJpaRepositoryPublic;

    public BingoCardController(BingoCardJpaRepository bingoCardJpaRepository) {
        this.bingoCardJpaRepository = bingoCardJpaRepository;
        bingoCardJpaRepositoryPublic = bingoCardJpaRepository;

        //BingoCard card = new BingoCard(UUID.randomUUID().toString(),"Lukas","lukas");
        //bingoCardJpaRepository.save(card);
        //card = new BingoCard(UUID.randomUUID().toString(),"ist","lukas");
        //bingoCardJpaRepository.save(card);
        //card = new BingoCard(UUID.randomUUID().toString(),"sehr","lukas");
        //bingoCardJpaRepository.save(card);
        //card = new BingoCard(UUID.randomUUID().toString(),"dumm","lukas");
        //bingoCardJpaRepository.save(card);
    }

    @PostMapping("/bingo/card/findAll")
    public ResponseEntity<String> findAll(@RequestBody String body) {
        List<BingoCard> data = bingoCardJpaRepository.findAll();

        JSONArray jsonArray = new JSONArray();

        for(BingoCard card : data){
            JSONObject o = new JSONObject();
            o.put("name",card.getText());
            o.put("owner",card.getOwner());
            o.put("id",card.getId());
            jsonArray.put(o);
        }

        return ResponseEntity.ok(jsonArray.toString());
    }

    @PostMapping("/bingo/card/findById")
    public ResponseEntity<String> findById(@RequestBody String body) {
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or expired token");
            }

            if(!request.has("id")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing id");
            }

            JSONObject response = new JSONObject();

            Optional<BingoCard> ocard = bingoCardJpaRepository.findById(request.getString("id"));

            if (ocard.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            BingoCard card = ocard.get();

            response.put("name",card.getText());
            response.put("owner",card.getOwner());
            response.put("id",card.getId());

            return ResponseEntity.ok(response.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/card/delete")
    public ResponseEntity<String> delete(@RequestBody String body) {
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or expired token");
            }

            if(!request.has("id")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing id");
            }

            JSONObject response = new JSONObject();

            Optional<BingoCard> ocard = bingoCardJpaRepository.findById(request.getString("id"));

            if (ocard.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            BingoCard card = ocard.get();

            if(card.getOwner().equals(TokenChecker.getUsername(request.getString("token")))){
                bingoCardJpaRepository.delete(card);
            }

            return ResponseEntity.ok(response.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/card/create")
    public ResponseEntity<String> preset(@RequestBody String body){
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or expired token");
            }

            if(!request.has("name")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing name");
            }

            JSONObject response = new JSONObject();

            BingoCard card = new BingoCard(UUID.randomUUID().toString(),request.getString("name"),TokenChecker.getUsername(request.getString("token")));

            bingoCardJpaRepository.save(card);

            response.put("name",card.getText());
            response.put("owner",card.getOwner());
            response.put("id",card.getId());

            return ResponseEntity.ok(response.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }
}
