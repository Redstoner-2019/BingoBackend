package me.redstoner2019.bingobackend.bingoPreset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class BingoPreset {
    @Id
    private String id;

    @Column(name="sameCards")
    private boolean sameCards;

    @Column(name="owner")
    private String owner;

    @Column(name="name")
    private String name;

    @Column(name="public")
    private boolean isPublic;

    @Lob
    @Column(name="cards", columnDefinition = "MEDIUMTEXT")
    private String cards;

    @Column(name="createdAt")
    private long createdAt;

    @Column(name="width")
    private int width;

    public BingoPreset() {

    }

    public BingoPreset(String id, boolean sameCards, String owner, String name, boolean isPublic, String cards, long createdAt, int width) {
        this.id = id;
        this.sameCards = sameCards;
        this.owner = owner;
        this.name = name;
        this.isPublic = isPublic;
        this.cards = cards;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSameCards() {
        return sameCards;
    }

    public void setSameCards(boolean sameCards) {
        this.sameCards = sameCards;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
