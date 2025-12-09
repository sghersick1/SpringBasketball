package loyola.basketball.Repository;


import loyola.basketball.Entity.Team;
import loyola.basketball.Exception.TeamNotFoundException;
import loyola.basketball.Mapper.TeamMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeamRepository {
    private final JdbcTemplate jdbc;
    private final TeamMapper mapper;

    public TeamRepository(JdbcTemplate jdbc, TeamMapper mapper){
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    /**
     * Get all Teams
     * @return List of Teams
     */
    public List<Team> getAllTeams(){
        return jdbc.query("Select * from Team", mapper);
    }

    /**
     * Get Team by ID
     * @param teamId ID of requested team
     * @return Team with the given ID
     * @throws TeamNotFoundException if no team is found with the given ID
     */
    public Team getTeamById(int teamId){
        String sqlScript = "SELECT * from Team WHERE id_team = ?";
        List<Team> teams = jdbc.query(con -> {
            PreparedStatement ps = con.prepareStatement(sqlScript);
            ps.setInt(1, teamId);
            return ps;
        }, mapper);
        
        if (teams.isEmpty()) {
            throw new TeamNotFoundException(teamId);
        }
        
        return teams.get(0);
    }

    /**
     * Create a new Team in the Database + update team object ID
     * @param team Team object to create
     * @return Team with generated ID
     * @throws RuntimeException if the insert fails or no key is generated
     */
    public Team createTeam(Team team){
        String sqlScript = "INSERT INTO Team(name_team) VALUES(?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlScript, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, team.getTeamName());
            return ps;
        }, keyHolder);
        
        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Failed to generate team ID after insert");
        }
        
        team.setTeamId(keyHolder.getKey().intValue());
        return team;
    }

    /**
     * Update a Team's name in the Database
     * @param teamId ID of team to update
     * @param teamName New team name
     * @throws TeamNotFoundException if no team is found with the given ID
     */
    public void updateTeam(int teamId, String teamName){
        String sqlScript = "UPDATE Team SET name_team = ? WHERE id_team = ?";
        int rowsAffected = jdbc.update(sqlScript, teamName, teamId);
        
        if (rowsAffected == 0) {
            throw new TeamNotFoundException(teamId);
        }
    }

    /**
     * Delete a Team from the Database
     * @param teamId ID of team to delete
     * @throws TeamNotFoundException if no team is found with the given ID
     */
    public void deleteTeam(int teamId){
        int rowsAffected = jdbc.update("DELETE FROM Team WHERE id_team = ?", teamId);
        
        if (rowsAffected == 0) {
            throw new TeamNotFoundException(teamId);
        }
    }

}
