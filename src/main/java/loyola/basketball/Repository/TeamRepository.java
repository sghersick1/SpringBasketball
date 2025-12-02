package loyola.basketball.Repository;


import loyola.basketball.Entity.Team;
import loyola.basketball.Mapper.TeamMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepository {
    private final JdbcTemplate jdbc;

    public TeamRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    /**
     * Get number of teams
     * @return number of teams
     */
    public int getTeamCount(){
        Integer teamCount = jdbc.queryForObject(
                "SELECT count(*) FROM TEAM",
                Integer.class
        );

        return teamCount;
    }

    /**
     * Get all Teams
     * @return List of Teams
     */
    public List<Team> getAllTeams(){
        return jdbc.query("Select * from Team", new TeamMapper());
    }

    /**
     * Get Team by ID
     * @param teamId ID of requested team
     */
    public Team getTeamById(int teamId){
        String script = "SELECT * from Team WHERE id_team = "+teamId+";";

        List<Team> teams = jdbc.query(script, new TeamMapper());
        assert(teams.size() == 1);
        return teams.get(0);
    }

}
