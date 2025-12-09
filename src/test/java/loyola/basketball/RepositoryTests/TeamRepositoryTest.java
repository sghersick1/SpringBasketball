package loyola.basketball.RepositoryTests;

import loyola.basketball.Entity.Team;
import loyola.basketball.Exception.TeamNotFoundException;
import loyola.basketball.Mapper.TeamMapper;
import loyola.basketball.Repository.TeamRepository;
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
public class TeamRepositoryTest {

    @Mock
    private JdbcTemplate jdbc;
    @Mock
    private TeamMapper mapper;  
    @InjectMocks
    private TeamRepository teamRepository;

    /* Get Team By ID Tests */
    @Test
    public void getTeamById_TeamExists_ReturnsTeam(){
        // Arrange
        Team team = new Team();
        team.setTeamId(1);
        team.setTeamName("Test Team");
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(List.of(team));
        
        // Act
        Team result = teamRepository.getTeamById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(team, result);
        assertEquals(1, result.getTeamId());
        assertEquals("Test Team", result.getTeamName());
    }

    @Test
    public void getTeamById_TeamDoesNotExist_ThrowsTeamNotFoundException(){
        // Arrange
        int teamId = 999;
        when(jdbc.query(any(PreparedStatementCreator.class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());
        
        // Act & Assert
        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class, () -> {
            teamRepository.getTeamById(teamId);
        });
    }

    /* Get All Teams Tests */
    @Test
    public void getAllTeams_TeamsExist_ReturnsListOfTeams(){
        // Arrange
        Team team1 = new Team();
        team1.setTeamId(1);
        team1.setTeamName("Team One");
        
        Team team2 = new Team();
        team2.setTeamId(2);
        team2.setTeamName("Team Two");
        
        List<Team> expectedTeams = List.of(team1, team2);
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedTeams);
        
        // Act
        List<Team> result = teamRepository.getAllTeams();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTeams, result);
        assertEquals("Team One", result.get(0).getTeamName());
        assertEquals("Team Two", result.get(1).getTeamName());
    }

    @Test
    public void getAllTeams_NoTeamsExist_ReturnsEmptyList(){
        // Arrange
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());
        
        // Act
        List<Team> result = teamRepository.getAllTeams();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    /* Create Team Tests */
    @Test
    public void createTeam_ValidTeam_ReturnsTeamWithId(){
        // Arrange
        Team team = new Team();
        team.setTeamName("New Team");
        int generatedId = 42;
        
        doAnswer(invocation -> {
            GeneratedKeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id_team", generatedId)); // Simulate Generated Keys
            return 1; // Simulate # of rows affected
        }).when(jdbc).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
        
        // Act
        Team result = teamRepository.createTeam(team);
        
        // Assert
        assertNotNull(result);
        assertEquals(generatedId, result.getTeamId());
        assertEquals("New Team", result.getTeamName());
    }

    /* Update Team Tests */
    @Test
    public void updateTeam_TeamExists_UpdatesSuccessfully(){
        // Arrange
        int teamId = 1;
        String newTeamName = "Updated Team Name";
        when(jdbc.update(anyString(), eq(newTeamName), eq(teamId)))
                .thenReturn(1); // One row affected
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            teamRepository.updateTeam(teamId, newTeamName);
        });
    }

    @Test
    public void updateTeam_TeamDoesNotExist_ThrowsTeamNotFoundException(){
        // Arrange
        int teamId = 999;
        String newTeamName = "Updated Team Name";
        when(jdbc.update(anyString(), eq(newTeamName), eq(teamId)))
                .thenReturn(0); // No rows affected
        
        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> {
            teamRepository.updateTeam(teamId, newTeamName);
        });
    }

    /* Delete Team Tests */
    @Test
    public void deleteTeam_TeamExists_DeletesSuccessfully(){
        // Arrange
        int teamId = 1;
        when(jdbc.update(anyString(), eq(teamId)))
                .thenReturn(1); // One row affected
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            teamRepository.deleteTeam(teamId);
        });
    }

    @Test
    public void deleteTeam_TeamDoesNotExist_ThrowsTeamNotFoundException(){
        // Arrange
        int teamId = 999;
        when(jdbc.update(anyString(), eq(teamId)))
                .thenReturn(0); // No rows affected
        
        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> {
            teamRepository.deleteTeam(teamId);
        });
    }
}
