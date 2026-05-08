package sk.tuke.gamestudio.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;

@Entity
@NamedQuery(
        name = "Score.getTopScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC"
)
@NamedQuery(
        name = "Score.resetScores",
        query = "DELETE FROM Score"
)
public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private double points;
    private Date playedOn;
    @Transient
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public Score(String game, String player, double points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    public Score() { }

    public int getIdent() { return ident; }
    public String getGame() { return game; }
    public String getPlayer() { return player; }
    public double getPoints() { return points; }
    public Date getPlayedOn() { return playedOn; }
    public String getFormattedDate() { return format.format(playedOn); }

    public void setIdent(int ident) { this.ident = ident; }
    public void setGame(String game) { this.game = game; }
    public void setPlayer(String player) { this.player = player; }
    public void setPoints(double points) { this.points = points; }
    public void setPlayedOn(Date playedOn) { this.playedOn = playedOn; }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedOn=" + playedOn +
                '}';
    }

}
