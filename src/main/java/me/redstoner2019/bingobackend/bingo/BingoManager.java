package me.redstoner2019.bingobackend.bingo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BingoManager {
    private static HashMap<String, Bingo> bingos = new HashMap<>();

    public static void addBingo(Bingo bingo) {
        if(!bingos.containsKey(bingo.getBingoId())){
            bingos.put(bingo.getBingoId(), bingo);
        }
    }

    public static Bingo getBingo(String bingoId) {
        return bingos.get(bingoId);
    }

    public static void removeBingo(String bingoId) {
        bingos.remove(bingoId);
    }

    public static List<Bingo> getAllBingos() {
        return new ArrayList<>(bingos.values());
    }
}
