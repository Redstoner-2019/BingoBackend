package me.redstoner2019.bingobackend.bingoCard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BingoCard {
    @Id
    private String id;
    @Column(name="text")
    private String text;
    @Column(name="owner")
    private String owner;

    public BingoCard() {

    }

    public BingoCard(String id, String text, String owner) {
        this.id = id;
        this.text = text;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
