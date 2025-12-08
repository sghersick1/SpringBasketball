package loyola.basketball.Controller;

import loyola.basketball.Entity.Game;
import loyola.basketball.Service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    /**
     * All games
     * @return all games in database
     */
    @GetMapping("/schedule")
    public List<Game> schedule(){
        return gameService.schedule();
    }

    /**
     * Create a new game
     * @param homeId id of home team
     * @param homePoints score of home team
     * @param awayId id of away team
     * @param awayPoints score of away team
     * @param location
     * @param date
     * @param time
     * @return HTTP response with game data in body, and endpoint in "location" header
     */
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Game> create(@RequestParam int homeId,
                                       @RequestParam(required = false) Integer homePoints,
                                       @RequestParam int awayId,
                                       @RequestParam(required = false) Integer awayPoints,
                                       @RequestParam(required = false, defaultValue = "Newman Courts") String location,
                                       @RequestParam(required = false) Date date,
                                       @RequestParam(required = false) Time time){
        Game game = gameService.createGame(homeId, homePoints, awayId, awayPoints, location, date, time);
        URI getEndpoint = URI.create("/game/"+game.getGameId());

        return ResponseEntity.created(getEndpoint).body(game);
    }
}
