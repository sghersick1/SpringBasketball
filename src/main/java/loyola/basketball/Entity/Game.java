package loyola.basketball.Entity;

import java.sql.Time;
import java.sql.Date;

public class Game {
    private int gameId;

    // Game Attributes
    private Date date;
    private Time time;
    private String location;

    // Participating Teams
    private int home;
    private Integer homeScore;
    private int away;
    private Integer awayScore;

    // Constructors
    public Game(){}

    // Getters & Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public int getAway() {
        return away;
    }

    public void setAway(int away) {
        this.away = away;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id_game=" + gameId +
                ", date=" + date +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", home=" + home +
                ", homeScore=" + homeScore +
                ", away=" + away +
                ", awayScore=" + awayScore +
                '}';
    }
}
