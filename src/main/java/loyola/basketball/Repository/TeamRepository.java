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
}
