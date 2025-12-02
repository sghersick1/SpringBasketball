package loyola.basketball.Service;

import loyola.basketball.Entity.Team;
import loyola.basketball.Repository.GameRepository;
import loyola.basketball.Repository.PlayerRepository;
import loyola.basketball.Repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.Temporal;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository, GameRepository gameRepository, PlayerRepository playerRepository){
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public List<Team> getAllTeams(){
        List<Team> teams = teamRepository.getAllTeams();
        return teams.stream()
                .map(team -> updateGameStatistics(team))
                //.sorted(StatisticsComparator::compare)
                .toList();
    }

    public Team getTeam(int teamId){
        return updateGameStatistics(teamRepository.getTeamById(teamId));
    }

    /**
     * Update teams game list from DB, and calculate league statistics
     * @param t Team
     */
    private Team updateGameStatistics(Team t){
        t.setPlayers(playerRepository.getPlayersByTeam(t.getTeamId()));
        t.setGames(gameRepository.getGameByTeamId(t.getTeamId()));
        t.calculateStats();
        return t;
    }
}
