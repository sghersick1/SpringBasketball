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
    public Game(int home, int away){
        this.home = home;
        this.away = away;
    }

    public Game(int gameId, Date date, Time time, String location, int home, Integer homeScore, int away, Integer awayScore) {
        this.gameId = gameId;
        this.date = date;
        this.time = time;
        this.location = location;
        this.home = home;
        this.homeScore = homeScore;
        this.away = away;
        this.awayScore = awayScore;
    }

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

    // Additional Methods
    public boolean isComplete(){
        return homeScore != null && awayScore != null;
    }

    public void updateStatistic(int teamId, Statistics stats) throws IllegalStateException{
        if(!isComplete() || (teamId != home && teamId != away)) throw new IllegalStateException();

        if(teamId == home){ // If home team
            if(homeScore > awayScore){
                stats.incWins();
            }else if(awayScore > homeScore){
                stats.incLosses();
            }else stats.incTies();
            stats.addPointsFor(homeScore);
            stats.addPointsAgainst(awayScore);
        }else{ // If away team
            if(awayScore > homeScore) stats.incWins();
            else if (homeScore > awayScore) stats.incLosses();
            else stats.incTies();
            stats.addPointsFor(awayScore);
            stats.addPointsAgainst(homeScore);
        }
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
