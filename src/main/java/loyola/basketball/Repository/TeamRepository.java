package loyola.basketball.Repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
