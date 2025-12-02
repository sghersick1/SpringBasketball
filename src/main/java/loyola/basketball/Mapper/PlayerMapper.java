package loyola.basketball.Mapper;

import loyola.basketball.Entity.Grade;
import loyola.basketball.Entity.Player;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException{
        return new Player(
                rs.getInt("id_player"),
                rs.getString("name_player"),
                Grade.valueOf(rs.getString("grade")),
                rs.getObject("draft_pick", Integer.class),
                rs.getBoolean("is_captain"),
                rs.getInt("id_team")
        );
    }
}
