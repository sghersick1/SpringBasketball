package loyola.basketball.ControllerTests;

import loyola.basketball.Controller.TeamController;
import loyola.basketball.Entity.Team;
import loyola.basketball.Exception.TeamNotFoundException;
import loyola.basketball.Service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {
    
    @Mock
    private TeamService teamService;
    
    @InjectMocks
    private TeamController teamController;
    
    /* Get Team By ID Tests */
    @Test
    public void getTeamById_TeamExists_ReturnsOkWithTeam(){
        // Arrange
        int teamId = 1;
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName("Test Team");
        
        when(teamService.getTeam(teamId)).thenReturn(team);
        
        // Act
        ResponseEntity<Team> response = teamController.getTeamById(teamId);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teamId, response.getBody().getTeamId());
        assertEquals("Test Team", response.getBody().getTeamName());
    }
    
    @Test
    public void getTeamById_TeamDoesNotExist_ThrowsTeamNotFoundException(){
        // Arrange
        int teamId = 999;
        when(teamService.getTeam(teamId)).thenThrow(new TeamNotFoundException(teamId));
        
        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> {
            teamController.getTeamById(teamId);
        });
    }
    
    @Test
    public void getTeamById_ValidTeamId_CallsServiceWithCorrectId(){
        // Arrange
        int teamId = 5;
        Team team = new Team();
        team.setTeamId(teamId);
        team.setTeamName("Another Team");
        
        when(teamService.getTeam(teamId)).thenReturn(team);
        
        // Act
        ResponseEntity<Team> response = teamController.getTeamById(teamId);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
