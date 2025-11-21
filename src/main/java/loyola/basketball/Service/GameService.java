package loyola.basketball.Service;

import loyola.basketball.Entity.Game;
import loyola.basketball.Repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    GameRepository gameRepo;

    public GameService(GameRepository gameRepo){this.gameRepo = gameRepo;}


    public List<Game> schedule(){
        return gameRepo.schedule();
    }
}
