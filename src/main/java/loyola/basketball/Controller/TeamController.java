package loyola.basketball.Controller;

import loyola.basketball.Entity.Team;
import loyola.basketball.Service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/team-count")
    public int getTeamCount(){
        return teamService.getTeamCount();
    }

    @GetMapping("/teams")
    public List<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody Team team){

        URI getEndpoint = URI.create("/team/"+team.getTeamId());

        return ResponseEntity.created(getEndpoint).body(team);
    }

    @GetMapping()
    public ResponseEntity<Team> getTeamById(@RequestParam int teamId){
        return new ResponseEntity(teamService.getTeam(teamId), HttpStatus.OK);
    }

}
