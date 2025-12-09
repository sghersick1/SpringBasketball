package loyola.basketball.Repository;

import loyola.basketball.Entity.Player;
import loyola.basketball.Exception.PlayerNotFoundException;
import loyola.basketball.Mapper.PlayerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PlayerRepository {
    private final JdbcTemplate jdbc;
    private final PlayerMapper mapper;

    public PlayerRepository(JdbcTemplate jdbc, PlayerMapper mapper){
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    /**
     * Create a new Player in the Database + update player object ID
     * @param p Player object to create
     * @return Player with generated ID
     * @throws RuntimeException if the insert fails or no key is generated
     */
    public Player createPlayer(Player p){
        String sqlScript = "INSERT INTO players(name_player, grade, draft_pick, is_captain, id_team)" +
                "VALUES(?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlScript, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getYear().name());
            ps.setObject(3, p.getDraftPick());
            ps.setBoolean(4, p.isCaptain());
            ps.setInt(5, p.getTeamId());
            return ps;
        }, keyHolder);
        
        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Failed to generate player ID after insert");
        }
        
        p.setPlayerId(keyHolder.getKey().intValue());
        return p;
    }

    /**
     * Get Player by ID
     * @param playerId ID of requested player
     * @return Player with the given ID
     * @throws PlayerNotFoundException if no player is found with the given ID
     */
    public Player readPlayerById(int playerId){
        String sqlScript = "SELECT * from players where id_player = ?;";
        List<Player> players = jdbc.query(con -> {
                                    PreparedStatement ps = con.prepareStatement(sqlScript);
                                    ps.setInt(1, playerId);
                                    return ps;
                                }, mapper);
        
        if (players.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }
        
        return players.get(0);
    }

    public List<Player> getPlayersByTeam(int teamId){
        String sqlScript = "SELECT * from players where id_team = ?;";
        return jdbc.query(con -> {
            PreparedStatement ps = con.prepareStatement(sqlScript);
            ps.setInt(1, teamId);
            return ps;
        }, mapper);
    }
    
    /**
     * Delete a Player from the Database
     * @param playerId ID of player to delete
     * @throws PlayerNotFoundException if no player is found with the given ID
     */
    public void deletePlayerById(int playerId){
        int rowsAffected = jdbc.update("DELETE FROM players WHERE id_player = ?;", playerId);
        
        if (rowsAffected == 0) {
            throw new PlayerNotFoundException(playerId);
        }
    }
}
