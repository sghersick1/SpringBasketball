package loyola.basketball.Service;

import loyola.basketball.Entity.Team.Team;
import loyola.basketball.Repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public int getTeamCount(){
        return teamRepository.getTeamCount();
    }
    public List<Team> getAllTeams(){return teamRepository.getAllTeams();}
}
