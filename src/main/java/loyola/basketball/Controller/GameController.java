package loyola.basketball.Controller;

import loyola.basketball.Entity.Game;
import loyola.basketball.Service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
