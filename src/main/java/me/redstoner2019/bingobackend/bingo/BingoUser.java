package me.redstoner2019.bingobackend.bingo;

import java.util.Objects;

public class BingoUser {
    private String username;
    private String displayname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public BingoUser(String username, String displayname) {
        this.username = username;
        this.displayname = displayname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BingoUser bingoUser = (BingoUser) o;
        return Objects.equals(username, bingoUser.username) && Objects.equals(displayname, bingoUser.displayname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, displayname);
    }
}
