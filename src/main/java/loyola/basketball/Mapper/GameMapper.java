package loyola.basketball.Mapper;

import loyola.basketball.Entity.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game>
{
    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setId_game(rs.getInt("id_game"));
        game.setDate(rs.getDate("date"));
        game.setTime(rs.getTime("time"));
        game.setLocation(rs.getString("location"));
        game.setHome(rs.getInt("id_team_1"));
        game.setHomeScore(rs.getInt("score_team_1"));
        game.setAway(rs.getInt("id_team_2"));
        game.setAwayScore(rs.getInt("score_team_2"));
        return game;
    }
}
