package loyola.basketball.Service;

import loyola.basketball.Entity.Player;
import loyola.basketball.Repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepo;
    public PlayerService(PlayerRepository playerRepo){ this.playerRepo = playerRepo; }

    public Player createPlayer(Player p){return playerRepo.createPlayer(p);}
    public Player readPlayer(int playerId){return playerRepo.readPlayerById(playerId);}
    public void deletePlayer(int playerId){playerRepo.deletePlayerById(playerId);}
}
