package loyola.basketball.Controller;

import loyola.basketball.Entity.Game;
import loyola.basketball.Service.GameService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/schedule")
    public List<Game> schedule(){
        return gameService.schedule();
    }

    @PostMapping()
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
