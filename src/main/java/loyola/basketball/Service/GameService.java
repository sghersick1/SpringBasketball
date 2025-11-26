package loyola.basketball.Service;

import loyola.basketball.Entity.Game;
import loyola.basketball.Repository.GameRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class GameService {

    GameRepository gameRepo;

    public GameService(GameRepository gameRepo){this.gameRepo = gameRepo;}


    public List<Game> schedule(){
        return gameRepo.schedule();
    }
    public Game createGame(int homeId, Integer homePoints, int awayId, Integer awayPoints, String location, Date date, Time time){
        Game game = new Game();
        game.setHome(homeId);
        game.setHomeScore(homePoints);
        game.setAway(awayId);
        game.setAwayScore(awayPoints);
        game.setLocation(location);
        game.setDate(date);
        game.setTime(time);

        gameRepo.createGame(game);
        return game;
    }
}
