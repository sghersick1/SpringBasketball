/**
 * Testing File for TeamService
 * author: Sam Hersick
 * version: 1.0.0
 * date: 12/8/2025
 */
package loyola.basketball.ServiceTests;

import loyola.basketball.Entity.Game;
import loyola.basketball.Entity.Grade;
import loyola.basketball.Entity.Player;
import loyola.basketball.Entity.Team;
import loyola.basketball.Exception.TeamNotFoundException;
import loyola.basketball.Repository.GameRepository;
import loyola.basketball.Repository.PlayerRepository;
import loyola.basketball.Repository.TeamRepository;
import loyola.basketball.Service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    // Mock Dependencies
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlayerRepository playerRepository;

    // Component we are testing
    @InjectMocks
    private TeamService teamService;

    /* Get Team Tests */
    @Test
    public void getTeam_TeamExistsWithPlayersAndGames_ReturnsTeamWithStatistics(){
        // Arrange
        int teamId = 1;
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName("Test Team");
        
        Player player1 = new Player();
        player1.setPlayerId(1);
        player1.setName("Lebron James");
        player1.setYear(Grade.SENIOR);
        player1.setTeamId(teamId);
        
        Player player2 = new Player();
        player2.setPlayerId(2);
        player2.setName("Steph Curry");
        player2.setYear(Grade.JUNIOR);
        player2.setTeamId(teamId);
        
        Game game1 = new Game(teamId, 2);
        game1.setGameId(1);
        game1.setHomeScore(85);
        game1.setAwayScore(72);
        
        List<Player> players = List.of(player1, player2);
        List<Game> games = List.of(game1);
        
        when(teamRepository.getTeamById(teamId)).thenReturn(team);
        when(playerRepository.getPlayersByTeam(teamId)).thenReturn(players);
        when(gameRepository.getGameByTeamId(teamId)).thenReturn(games);
        
        // Act
        Team result = teamService.getTeam(teamId);
        
        // Assert
        assertNotNull(result);
        assertEquals(teamId, result.getTeamId());
        assertEquals("Test Team", result.getTeamName());
        assertNotNull(result.getPlayers());
        assertEquals(2, result.getPlayers().size());
        assertNotNull(result.getGames());
        assertEquals(1, result.getGames().size());
        assertNotNull(result.getStats());
    }

    @Test
    public void getTeam_TeamDoesNotExist_ThrowsTeamNotFoundException(){
        // Arrange
        int teamId = 999;
        when(teamRepository.getTeamById(teamId)).thenThrow(new TeamNotFoundException(teamId));
        
        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> {
            teamService.getTeam(teamId);
        });
        
        verify(teamRepository).getTeamById(teamId);
        verify(playerRepository, never()).getPlayersByTeam(anyInt());
        verify(gameRepository, never()).getGameByTeamId(anyInt());
    }

    @Test
    public void getTeam_TeamExistsWithNoPlayersOrGames_ReturnsTeamWithEmptyLists(){
        // Arrange
        int teamId = 1;
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName("Empty Team");
        
        when(teamRepository.getTeamById(teamId)).thenReturn(team);
        when(playerRepository.getPlayersByTeam(teamId)).thenReturn(Collections.emptyList());
        when(gameRepository.getGameByTeamId(teamId)).thenReturn(Collections.emptyList());
        
        // Act
        Team result = teamService.getTeam(teamId);
        
        // Assert
        assertNotNull(result);
        assertEquals(teamId, result.getTeamId());
        assertEquals("Empty Team", result.getTeamName());
        assertNotNull(result.getPlayers());
        assertTrue(result.getPlayers().isEmpty());
        assertNotNull(result.getGames());
        assertTrue(result.getGames().isEmpty());
        assertNotNull(result.getStats());
    }
}
