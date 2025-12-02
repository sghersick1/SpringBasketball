/**
 * Team object including id, name, players, and games
 */
package loyola.basketball.Entity;

import java.util.List;

public class Team {
    // Attributes
    private int teamId;
    private String teamName;
    private List<Player> players;
    private List<Game> games;
    private Statistics stats;

    // Constructors
    public Team(){
        stats = new Statistics();
    }

    public Team(String teamName) {
        this.teamName = teamName;
        stats = new Statistics();
    }

    public Team(String teamName, List<Player> players) {
        this.teamName = teamName;
        this.players = players;
        stats = new Statistics();
    }

    // Getters & Setters
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Statistics getStats() {
        return stats;
    }

    public void setStats(Statistics stats) {
        this.stats = stats;
    }

    // Additional Methods
    public List<Game> upcomingGames(){
        return null;
    }

    public List<Game> completedGames(){
        return null;
    }

    /**
     * Iterate through all completed games and update statistics object
     * @return this team's league statistics
     */
    public Statistics calculateStats(){
        games.stream()
                .filter(game -> game.isComplete() == true)
                .forEach(game -> game.updateStatistic(teamId, stats));
        ;

        return stats;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", players=" + players +
                ", games=" + games +
                ", stats=" + stats +
                '}';
    }
}
