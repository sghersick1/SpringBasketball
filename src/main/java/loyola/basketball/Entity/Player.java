package loyola.basketball.Entity;

public class Player {
    private int playerId;
    private String name;
    private Grade year;
    private Integer draftPick;
    private boolean isCaptain;
    private int teamId;

    public Player(){}
    public Player(String name, Grade year, Integer draftPick, boolean isCaptain, int teamId) {
        this.name = name;
        this.year = year;
        this.draftPick = draftPick;
        this.isCaptain = isCaptain;
        this.teamId = teamId;
    }
    public Player(int playerId, String name, Grade year, Integer draftPick, boolean isCaptain, int teamId) {
        this.playerId = playerId;
        this.name = name;
        this.year = year;
        this.draftPick = draftPick;
        this.isCaptain = isCaptain;
        this.teamId = teamId;
    }

    // Getters & Setters
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getYear() {
        return year;
    }

    public void setYear(Grade year) {
        this.year = year;
    }

    public Integer getDraftPick() {
        return draftPick;
    }

    public void setDraftPick(Integer draftPick) {
        this.draftPick = draftPick;
    }

    public boolean isCaptain() {
        return isCaptain;
    }

    public void setCaptain(boolean captain) {
        isCaptain = captain;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
