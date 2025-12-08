package loyola.basketball.Controller;

import loyola.basketball.Entity.Team;
import loyola.basketball.Service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    /**
     * Get a team given the id
     * @param teamId
     * @return team object (JSON)
     */
    @GetMapping()
    public ResponseEntity<Team> getTeamById(@RequestParam int teamId){
        return new ResponseEntity(teamService.getTeam(teamId), HttpStatus.OK);
    }

    /**
     * Get all teams
     * @return List of all team objects (JSON)
     */
    @GetMapping("/teams")
    public List<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    /**
     * Create a new team
     * @param teamName of the team
     * @return team created + location to GET team in future (http location header)
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> createTeam(@RequestParam String teamName){
        Team t = new Team(teamName);
        URI getEndpoint = URI.create("/team/"+t.getTeamId());
        return ResponseEntity.created(getEndpoint).body(t);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam int ID){
        // Delete team from database
        // ...

        return ResponseEntity.status(HttpStatus.OK).body("Team "+ID+" Successfully Deleted");
    }

}
