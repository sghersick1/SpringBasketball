package loyola.basketball.Repository;

import loyola.basketball.Entity.Game;
import loyola.basketball.Mapper.GameMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameRepository {
    private final JdbcTemplate jdbc;
    public GameRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public List<Game> schedule(){
        String sqlScript = "SELECT\n" +
                "    g.id_game,\n" +
                "    g.date,\n" +
                "    g.time,\n" +
                "    g.location,\n" +
                "    t1.id_team  AS id_team_1,\n" +
                "    t1.points   AS score_team_1,\n" +
                "    t2.id_team  AS id_team_2,\n" +
                "    t2.points   AS score_team_2\n" +
                "FROM Game g\n" +
                "JOIN Team_Plays_Game t1\n" +
                "    using(id_game)\n" +
                "JOIN Team_Plays_Game t2 using(id_game)\n" +
                "WHERE t1.id_team < t2.id_team\n" +
                "order by g.date, g.time;";
        List<Game> game = jdbc.query(sqlScript, new GameMapper());
        return game;
    }

    /**
     * Create a game in the Database + update game object Id
     * @param game object
     */
    public void createGame(Game game){
        String gameScript = "INSERT INTO game(date, time, location, completed, voided) " +
                "VALUES(?, ?, ?, FALSE, FALSE);";

        String teamGameScript = "INSERT INTO team_plays_game(id_team, id_game, points) VALUES" +
                "(?, ?, ?)," +
                "(?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(gameScript, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, game.getDate());
            ps.setTime(2, game.getTime());
            ps.setString(3, game.getLocation());
            return ps;
        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(teamGameScript);
            ps.setInt(1, game.getHome());
            ps.setInt(2, game.getGameId());
            ps.setObject(3, game.getHomeScore());
            ps.setInt(4, game.getAway());
            ps.setInt(5, game.getGameId());
            ps.setObject(6, game.getAwayScore());
            return ps;
        });
    }

    /**
     * Get all the games played by a team
     * @param teamId of team
     * @return List of games that include the team
     */
    public List<Game> getGameByTeamId(int teamId){
        String sqlScript = "SELECT\n" +
                "    g.id_game,\n" +
                "    g.date,\n" +
                "    g.time,\n" +
                "    g.location,\n" +
                "    t1.id_team  AS id_team_1,\n" +
                "    t1.points   AS score_team_1,\n" +
                "    t2.id_team  AS id_team_2,\n" +
                "    t2.points   AS score_team_2\n" +
                "FROM Game g\n" +
                "JOIN Team_Plays_Game t1\n" +
                "    using(id_game)\n" +
                "JOIN Team_Plays_Game t2 using(id_game)\n" +
                "WHERE t1.id_team = "+teamId+" and t2.id_team <> "+teamId+"\n" +
                "order by g.date, g.time;";

        return jdbc.query(sqlScript, new GameMapper());
    }
}
