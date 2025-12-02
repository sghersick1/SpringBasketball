/**
 * Detailed Team object including league statistics (wins, losses, points, etc.)
 */
package loyola.basketball.Entity;

import org.apache.logging.log4j.util.PropertySource;

import java.util.Comparator;
import java.util.List;

public class Statistics {
    // Attributes
    private int wins;
    private int losses;
    private int ties;
    private int pointsFor;
    private int pointsAgainst;

    // Constructor
    public Statistics(){}

    // Getters & Setters
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(int pointsFor) {
        this.pointsFor = pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(int pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    // Additional Methods
    public int incWins(){
        return ++wins;
    }
    public int incLosses(){
        return ++losses;
    }
    public int incTies(){
        return ++ties;
    }
    public int addPointsFor(int amt){
        pointsFor += amt;
        return pointsFor;
    }
    public int addPointsAgainst(int amt){
        pointsAgainst += amt;
        return pointsAgainst;
    }
}
