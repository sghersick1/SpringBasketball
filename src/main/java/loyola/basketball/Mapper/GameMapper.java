package loyola.basketball.Mapper;

import loyola.basketball.Entity.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class GameMapper implements RowMapper<Game>
{
    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setGameId(rs.getInt("id_game"));
        game.setDate(rs.getDate("date"));
        game.setTime(rs.getTime("time"));
        game.setLocation(rs.getString("location"));
        game.setHome(rs.getInt("id_team_1"));
        game.setHomeScore(rs.getObject("score_team_1", Integer.class));
        game.setAway(rs.getInt("id_team_2"));
        game.setAwayScore(rs.getObject("score_team_2", Integer.class));
        return game;
    }
}
