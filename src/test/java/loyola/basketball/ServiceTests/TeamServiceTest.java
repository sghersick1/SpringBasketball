/**
 * Testing File for TeamService
 * author: Sam Hersick
 * version: 1.0.0
 * date: 12/8/2025
 */
package loyola.basketball.ServiceTests;

import loyola.basketball.Entity.Team;
import loyola.basketball.Repository.GameRepository;
import loyola.basketball.Repository.PlayerRepository;
import loyola.basketball.Repository.TeamRepository;
import loyola.basketball.Service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    /**
     * Test getting a valid team from the database by id
     */
    @Test
    public void testGetTeamValid(){
        Team team = new Team();
        int requestedId = 1;
    }
}
