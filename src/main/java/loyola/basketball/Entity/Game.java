package loyola.basketball.Entity;

import java.sql.Time;
import java.sql.Date;

public class Game {
    private int id_game;

    // Game Attributes
    private Date date;
    private Time time;
    private String location;

    // Participating Teams
    private int home;
    private int homeScore;
    private int away;
    private int awayScore;

    // Constructors
    public Game(){}

    // Getters & Setters
    public int getId_game() {
        return id_game;
    }

    public void setId_game(int id_game) {
        this.id_game = id_game;
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

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAway() {
        return away;
    }

    public void setAway(int away) {
        this.away = away;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id_game=" + id_game +
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
