package me.redstoner2019.bingobackend.bingoPreset;

import me.redstoner2019.bingobackend.bingoCard.BingoCard;
import me.redstoner2019.bingobackend.bingoCard.BingoCardController;
import me.redstoner2019.bingobackend.util.TokenChecker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class BingoPresetController {

    private final BingoPresetJpaRepository bingoPresetJpaRepository;
    public static BingoPresetJpaRepository bingoPresetJpaRepositoryPublic;

    public BingoPresetController(final BingoPresetJpaRepository bingoPresetJpaRepository) {
        this.bingoPresetJpaRepository = bingoPresetJpaRepository;
        bingoPresetJpaRepositoryPublic = bingoPresetJpaRepository;

        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 1",true,"[ ]",0));
        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 2",false,"[ ]",0));
        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 3",true,"[ ]",0));
        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 4",false,"[ ]",0));
        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 5",true,"[ ]",0));
        //bingoPresetJpaRepository.save(new BingoPreset(UUID.randomUUID().toString(),true,"lukas","Test 6",false,"[ ]",0));
    }

    @PostMapping("/bingo/preset/find")
    public ResponseEntity<String> preset(@RequestBody String body){
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED: Invalid or expired token");
            }

            if(!request.has("isPublic")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing isPublic");
            }

            boolean isPublic = request.getBoolean("isPublic");

            JSONArray response = new JSONArray();

            for(BingoPreset bp : bingoPresetJpaRepository.findAll()){
                if(isPublic == bp.isPublic()){
                    if(!isPublic){
                        if(!bp.getOwner().equals(TokenChecker.getUsername(request.getString("token")))) continue;
                    }
                    JSONObject obj = new JSONObject();
                    obj.put("id", bp.getId());
                    obj.put("name", bp.getName());
                    response.put(obj);
                }
            }

            return ResponseEntity.ok(response.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/preset/create")
    public ResponseEntity<String> createPreset(@RequestBody String body){
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(401).body("Unauthenticated");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(401).body("Unauthenticated: Invalid or expired token");
            }

            BingoPreset preset = new BingoPreset(UUID.randomUUID().toString(),true,TokenChecker.getUsername(request.getString("token")),"Bingo",false,"[ ]",System.currentTimeMillis());

            bingoPresetJpaRepository.save(preset);

            JSONObject result = new JSONObject();

            result.put("id", preset.getId());
            result.put("same-cards", preset.isSameCards());
            result.put("owner", preset.getOwner());
            result.put("public", preset.isPublic());
            result.put("cards", preset.getCards());
            result.put("created-at", preset.getCreatedAt());
            result.put("name",preset.getName());

            return ResponseEntity.ok(result.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/preset/get")
    public ResponseEntity<String> getPreset(@RequestBody String body){
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED: Invalid or expired token");
            }

            if(!request.has("id")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing id");
            }

            Optional<BingoPreset> preset = bingoPresetJpaRepository.findById(request.getString("id"));

            if(preset.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preset not found");
            }

            JSONObject result = new JSONObject();

            result.put("id", preset.get().getId());
            result.put("same-cards", preset.get().isSameCards());
            result.put("owner", preset.get().getOwner());
            result.put("public", preset.get().isPublic());
            result.put("cards", preset.get().getCards());
            result.put("created-at", preset.get().getCreatedAt());
            result.put("name",preset.get().getName());

            return ResponseEntity.ok(result.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }

    @PostMapping("/bingo/preset/modify")
    public ResponseEntity<String> modifyPreset(@RequestBody String body){
        try{
            JSONObject request = new JSONObject(body);

            if(!request.has("token")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
            }

            if(!TokenChecker.isValidToken(request.getString("token"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED: Invalid or expired token");
            }

            if(!request.has("id")){
                return ResponseEntity.status(HttpStatus.OK).body("Missing id");
            }

            Optional<BingoPreset> presetOpt = bingoPresetJpaRepository.findById(request.getString("id"));

            if(presetOpt.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preset not found");
            }

            BingoPreset preset = presetOpt.get();

            if(!preset.getOwner().equals(TokenChecker.getUsername(request.getString("token")))){
                return ResponseEntity.status(HttpStatus.OK).body("UNAUTHORIZED: Not Owner of this Preset");
            }

            if(request.has("name")){
                preset.setName(request.getString("name"));
            }

            if(request.has("same-cards")){
                preset.setSameCards(request.getBoolean("same-cards"));
            }

            if(request.has("public")){
                preset.setPublic(request.getBoolean("public"));
            }

            if(request.has("cards")){
                JSONArray array = request.getJSONArray("cards");
                JSONArray cleaned = new JSONArray();

                for (int i = 0; i < array.length(); i++) {
                    Optional<BingoCard> bcOpt = BingoCardController.bingoCardJpaRepositoryPublic.findById(array.getString(i));
                    bcOpt.ifPresent(bingoCard -> cleaned.put(bingoCard.getId()));
                }

                preset.setCards(cleaned.toString());
            }

            bingoPresetJpaRepository.save(preset);

            JSONObject result = new JSONObject();

            result.put("id", preset.getId());
            result.put("same-cards", preset.isSameCards());
            result.put("owner", preset.getOwner());
            result.put("public", preset.isPublic());
            result.put("cards", preset.getCards());
            result.put("created-at", preset.getCreatedAt());
            result.put("name",preset.getName());

            return ResponseEntity.ok(result.toString());
        }catch (JSONException e){
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.status(200).body(response.toString());
        }
    }
}
