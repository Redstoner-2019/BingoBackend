package me.redstoner2019.bingobackend;

import me.redstoner2019.bingobackend.bingo.Bingo;
import me.redstoner2019.bingobackend.bingo.BingoManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BingoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BingoBackendApplication.class, args);

        List<String> cards = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            cards.add("Bingo " + i);
        }

        Bingo bingo = new Bingo(cards,"Server UwU", "Maximus Primus","test");

        BingoManager.start();

        BingoManager.addBingo(bingo);
    }

}
