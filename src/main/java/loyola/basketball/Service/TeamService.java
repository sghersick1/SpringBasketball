package loyola.basketball.Service;

import loyola.basketball.Repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public int getTeamCount(){
        return teamRepository.getTeamCount();
    }
}
