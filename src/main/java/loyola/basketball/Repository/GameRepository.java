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
    private final GameMapper mapper;
    public GameRepository(JdbcTemplate jdbc, GameMapper mapper){
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    /**
     * Create a game in the Database + update game object Id
     * @param game object
     * @throws RuntimeException if the insert fails or no key is generated
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

        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Failed to generate game ID after insert");
        }

        game.setGameId(keyHolder.getKey().intValue());

        int rowsAffected = jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(teamGameScript);
            ps.setInt(1, game.getHome());
            ps.setInt(2, game.getGameId());
            ps.setObject(3, game.getHomeScore());
            ps.setInt(4, game.getAway());
            ps.setInt(5, game.getGameId());
            ps.setObject(6, game.getAwayScore());
            return ps;
        });

        if (rowsAffected != 2) {
            throw new RuntimeException("Failed to create team associations for game. Expected 2 rows, got " + rowsAffected);
        }
    }

    /**
     * Update game in the Database
     * @param game Game object
     * @throws RuntimeException if the game is not found or update fails
     */
    public void updateGame(Game game){
        updateGame(game.getGameId(), game.getHome(), game.getHomeScore(), game.getAway(), game.getAwayScore());
        updateGame(game.getGameId(), game.getDate(), game.getTime(), game.getLocation());
    }

    /**
     * Update game details (date, time, location) in the Database
     * @param gameId ID of game to update
     * @param date New game date
     * @param time New game time
     * @param location New game location
     * @throws RuntimeException if the game is not found or update fails
     */
    public void updateGame(int gameId, java.sql.Date date, java.sql.Time time, String location){
        String updateScript = "UPDATE game SET date = ?, time = ?, location = ? WHERE id_game = ?";
        int rowsAffected = jdbc.update(updateScript, date, time, location, gameId);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update game details. Game with ID " + gameId + " not found");
        }
    }
    
    /**
     * Update game scores in the Database
     * @param gameId ID of game to update
     * @param homeTeamId ID of home team
     * @param homeScore Score of home team
     * @param awayTeamId ID of away team
     * @param awayScore Score of away team
     * @throws RuntimeException if the game is not found or update fails
     */
    public void updateGame(int gameId, int homeTeamId, Integer homeScore, int awayTeamId, Integer awayScore){
        String updateScript = "UPDATE team_plays_game SET points = ? WHERE id_game = ? AND id_team = ?";
        
        int homeRowsAffected = jdbc.update(updateScript, homeScore, gameId, homeTeamId);
        int awayRowsAffected = jdbc.update(updateScript, awayScore, gameId, awayTeamId);
        
        if (homeRowsAffected == 0 || awayRowsAffected == 0) {
            throw new RuntimeException("Failed to update game scores. Game with ID " + gameId + " not found or team associations missing");
        }
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
                "WHERE t1.id_team = ? and t2.id_team <> ?\n" +
                "order by g.date, g.time;";

        return jdbc.query(con ->{
            PreparedStatement ps = con.prepareStatement(sqlScript);
            ps.setInt(1, teamId);
            ps.setInt(2, teamId);
            return ps;
        }, mapper);

    }

    /**
     * Get all games
     * @return List of all games
     */
    public List<Game> getAllGames(){
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
        return jdbc.query(sqlScript, mapper);
    }
    
    /**
     * Delete a Game from the Database
     * @param gameId ID of game to delete
     * @throws RuntimeException if no game is found with the given ID
     */
    public void deleteGame(int gameId){
        // First delete team associations to avoid foreign key constraint issues
        jdbc.update("DELETE FROM team_plays_game WHERE id_game = ?", gameId);
        
        // Then delete the game
        int rowsAffected = jdbc.update("DELETE FROM game WHERE id_game = ?", gameId);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Game with ID " + gameId + " not found");
        }
    }
}
