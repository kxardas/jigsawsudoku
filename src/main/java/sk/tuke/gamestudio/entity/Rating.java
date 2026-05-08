package sk.tuke.gamestudio.entity;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rating_game_username", columnNames = {"game", "username"})
        }
)
@NamedQuery(
        name = "Rating.getAllRatings",
        query = "SELECT r FROM Rating r WHERE r.game=:game"
)
@NamedQuery(
        name = "Rating.resetRatings",
        query = "DELETE FROM Rating"
)
@NamedQuery(
        name = "Rating.getRating",
        query = "SELECT r FROM Rating r WHERE r.game=:game AND r.username=:username"
)
@NamedQuery(
        name = "Rating.updateRating",
        query = "UPDATE Rating r SET r.value = :value, r.createdAt = :createdAt " +
                "WHERE r.game = :game AND r.username = :username"
)
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String username;
    private int value;
    private Date createdAt;
    @Transient
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM");

    public Rating(String game, String username, int value, Date createdAt) {
        this.game = game;
        this.username = username;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Rating() { }

    public int getIdent() { return ident; }
    public String getGame() { return game; }
    public String getUsername() { return username; }
    public int getValue() { return value; }
    public Date getCreatedAt() { return createdAt; }

    public void setIdent(int ident) { this.ident = ident; }
    public void setGame(String game) { this.game = game; }
    public void setUsername(String username) { this.username = username; }
    public void setValue(int value) { this.value = value; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
    return format.format(createdAt) + " | " + username + ": " + value + "/10";
  }
}
