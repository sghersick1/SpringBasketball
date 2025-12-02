package loyola.basketball.Service;

import loyola.basketball.Entity.Team;
import loyola.basketball.Repository.GameRepository;
import loyola.basketball.Repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    public TeamService(TeamRepository teamRepository, GameRepository gameRepository){
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    public int getTeamCount(){
        return teamRepository.getTeamCount();
    }
    public List<Team> getAllTeams(){return teamRepository.getAllTeams();}

    public Team getTeam(int teamId){
        Team t = teamRepository.getTeamById(teamId);
        t.setGames(gameRepository.getGameByTeamId(teamId));
        t.calculateStats();
        return t;
    }
}
