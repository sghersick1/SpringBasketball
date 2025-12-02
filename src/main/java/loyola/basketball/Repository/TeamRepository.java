package loyola.basketball.Repository;


import loyola.basketball.Entity.Team;
import loyola.basketball.Mapper.TeamMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
     */
    public Team getTeamById(int teamId){
        String script = "SELECT * from Team WHERE id_team = "+teamId+";";

        List<Team> teams = jdbc.query(script, new TeamMapper());
        assert(teams.size() == 1);
        return teams.get(0);
    }

}
