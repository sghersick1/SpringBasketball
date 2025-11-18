package loyola.basketball.Entity.Team;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamMapper implements RowMapper<Team> {
    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();
        team.setTeamId(rs.getInt("id_team"));
        team.setTeamName(rs.getString("name_team"));
        return team;
    }

}
