package loyola.basketball.Entity;

public class Team {
    private int teamId;
    private String teamName;

    private int wins;
    private int losses;
    private int ties;
    private int pointsFor;
    private int pointsAgainst;

    // Constructors
    public Team(){}

    public Team(int teamId, String teamName){
        this.teamId = teamId;
        this.teamName = teamName;
    }

    // Getters + Setters
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

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", wins=" + wins +
                ", losses=" + losses +
                ", ties=" + ties +
                ", pointsFor=" + pointsFor +
                ", pointsAgainst=" + pointsAgainst +
                '}';
    }
}
