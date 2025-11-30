package loyola.basketball.Entity;

// Enum year -> freshman, sophomore, senior, super-senior
enum Year{ FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, SUPER_SENIOR }

public class Player {
    private int playerId;
    private String name;
    private Year year;
    private String draftPick;

    public Player(String name, Year year, String draftPick) {
        this.name = name;
        this.year = year;
        this.draftPick = draftPick;
    }

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

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getDraftPick() {
        return draftPick;
    }

    public void setDraftPick(String draftPick) {
        this.draftPick = draftPick;
    }
}
