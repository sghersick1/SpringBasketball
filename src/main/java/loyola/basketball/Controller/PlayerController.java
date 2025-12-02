package loyola.basketball.Controller;

import loyola.basketball.Entity.Grade;
import loyola.basketball.Entity.Player;
import loyola.basketball.Service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    /**
     * Get a player by id
     * @param playerId
     * @return player
     */
    @GetMapping()
    public ResponseEntity<Player> readPlayer(@RequestParam int playerId){
        return new ResponseEntity(playerService.readPlayer(playerId), HttpStatus.OK);
    }

    /**
     * Create a new player
     * @param playerName
     * @param grade
     * @param draftPick
     * @param isCaptain
     * @param teamId
     * @return player that was created
     */
    @PostMapping()
    public ResponseEntity<Player> createPlayer(@RequestParam String playerName,
                                               @RequestParam String grade,
                                               @RequestParam Integer draftPick,
                                               @RequestParam(required = false) boolean isCaptain,
                                               @RequestParam int teamId){
        Player p = playerService.createPlayer(new Player(playerName, Grade.valueOf(grade), draftPick, isCaptain, teamId));
        return ResponseEntity.created(URI.create("/player?playerId="+p.getPlayerId())).body(p);
    }

    /**
     * Delete a player by id
     * @param playerId
     */
    @DeleteMapping()
    public void deletePlayer(@RequestParam int playerId){
        playerService.deletePlayer(playerId);
    }
}
