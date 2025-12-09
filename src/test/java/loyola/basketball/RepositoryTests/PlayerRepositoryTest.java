package loyola.basketball.RepositoryTests;

import loyola.basketball.Entity.Grade;
import loyola.basketball.Entity.Player;
import loyola.basketball.Exception.PlayerNotFoundException;
import loyola.basketball.Mapper.PlayerMapper;
import loyola.basketball.Repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerRepositoryTest {

    @Mock
    private JdbcTemplate jdbc;
    @Mock
    private PlayerMapper mapper;
    @InjectMocks
    private PlayerRepository playerRepository;

    /* Create Player Tests */
    @Test
    public void createPlayer_ValidPlayer_ReturnsPlayerWithId(){
        // Arrange
        Player player = new Player();
        player.setName("John Doe");
        player.setYear(Grade.SENIOR);
        player.setDraftPick(null);
        player.setCaptain(true);
        player.setTeamId(1);
        int generatedId = 42;
        
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id_player", generatedId)); // Simulate Generated Keys
            return 1; // Simulate # of rows affected
        }).when(jdbc).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
        
        // Act
        Player result = playerRepository.createPlayer(player);
        
        // Assert
        assertNotNull(result);
        assertEquals(generatedId, result.getPlayerId());
        assertEquals("John Doe", result.getName());
        assertEquals(Grade.SENIOR, result.getYear());
        assertNull(result.getDraftPick());
        assertTrue(result.isCaptain());
        assertEquals(1, result.getTeamId());
    }

    @Test
    public void createPlayer_KeyGenerationFails_ThrowsRuntimeException(){
        // Arrange
        Player player = new Player();
        player.setName("John Doe");
        player.setYear(Grade.SENIOR);
        player.setDraftPick(1);
        player.setCaptain(true);
        player.setTeamId(1);
        
        doAnswer(invocation -> {
            // Don't update Generated Key Holder
            return 1; // Simulate # of rows affected
        }).when(jdbc).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            playerRepository.createPlayer(player);
        });
    }

    /* Read Player By ID Tests */
    @Test
    public void readPlayerById_PlayerExists_ReturnsPlayer(){
        // Arrange
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("John Doe");
        player.setYear(Grade.SENIOR);
        player.setDraftPick(1);
        player.setCaptain(false);
        player.setTeamId(1);
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(List.of(player));
        
        // Act
        Player result = playerRepository.readPlayerById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPlayerId());
        assertEquals("John Doe", result.getName());
        assertEquals(Grade.SENIOR, result.getYear());
        assertEquals(1, result.getDraftPick());
        assertFalse(result.isCaptain());
        assertEquals(1, result.getTeamId());
    }

    @Test
    public void readPlayerById_PlayerDoesNotExist_ThrowsPlayerNotFoundException(){
        // Arrange
        int playerId = 999;
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());
        
        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> {
            playerRepository.readPlayerById(playerId);
        });
    }

    /* Get Players By Team Tests */
    @Test
    public void getPlayersByTeam_PlayersExist_ReturnsListOfPlayers(){
        // Arrange
        int teamId = 1;
        Player player1 = new Player();
        player1.setPlayerId(1);
        player1.setName("John Doe");
        player1.setYear(Grade.SENIOR);
        player1.setDraftPick(null);
        player1.setCaptain(true);
        player1.setTeamId(teamId);
        
        Player player2 = new Player();
        player2.setPlayerId(2);
        player2.setName("Jane Smith");
        player2.setYear(Grade.JUNIOR);
        player2.setDraftPick(1);
        player2.setCaptain(false);
        player2.setTeamId(teamId);
        
        List<Player> expectedPlayers = List.of(player1, player2);
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(expectedPlayers);
        
        // Act
        List<Player> result = playerRepository.getPlayersByTeam(teamId);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    public void getPlayersByTeam_NoPlayersExist_ReturnsEmptyList(){
        // Arrange
        int teamId = 1;
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());
        
        // Act
        List<Player> result = playerRepository.getPlayersByTeam(teamId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /* Update Player Tests */
    @Test
    public void updatePlayer_PlayerExists_UpdatesSuccessfully(){
        // Arrange
        int playerId = 1;
        String newPlayerName = "Updated Player Name";
        when(jdbc.update(anyString(), eq(newPlayerName), eq(playerId)))
                .thenReturn(1); // One row affected
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            playerRepository.updatePlayer(playerId, newPlayerName);
        });
    }

    @Test
    public void updatePlayer_PlayerDoesNotExist_ThrowsPlayerNotFoundException(){
        // Arrange
        int playerId = 999;
        String newPlayerName = "Updated Player Name";
        when(jdbc.update(anyString(), eq(newPlayerName), eq(playerId)))
                .thenReturn(0); // No rows affected
        
        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> {
            playerRepository.updatePlayer(playerId, newPlayerName);
        });
    }

    /* Delete Player Tests */
    @Test
    public void deletePlayerById_PlayerExists_DeletesSuccessfully(){
        // Arrange
        int playerId = 1;
        when(jdbc.update(anyString(), eq(playerId)))
                .thenReturn(1); // One row affected
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            playerRepository.deletePlayerById(playerId);
        });
    }

    @Test
    public void deletePlayerById_PlayerDoesNotExist_ThrowsPlayerNotFoundException(){
        // Arrange
        int playerId = 999;
        when(jdbc.update(anyString(), eq(playerId)))
                .thenReturn(0); // No rows affected
        
        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> {
            playerRepository.deletePlayerById(playerId);
        });
    }
}
