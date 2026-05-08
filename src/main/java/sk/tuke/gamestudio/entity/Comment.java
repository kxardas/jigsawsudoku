package sk.tuke.gamestudio.entity;


import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;

@Entity
@NamedQuery(
        name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.createdAt DESC"
)
@NamedQuery(
        name = "Comment.deleteComments",
        query = "DELETE FROM Comment"
)
public class Comment {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String username;
    private String text;
    private Date createdAt;
    @Transient
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM");
    @Transient
    private final SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public Comment(String game, String username, String text, Date createdAt) {
        this.game = game;
        this.username = username;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Comment() { }

    public int getIdent() { return ident; }
    public String getGame() { return game;}
    public String getUserName() { return username; }
    public String getText() { return text; }
    public Date getCreatedAt() { return createdAt; }
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        return format2.format(createdAt);
    }

    public void setIdent(int ident) { this.ident = ident; }
    public void setGame(String game) { this.game = game; }
    public void setUserName(String username) { this.username = username; }
    public void setText(String text) { this.text = text; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return format.format(createdAt) + " | " + username + ": " + text ;
    }
}
