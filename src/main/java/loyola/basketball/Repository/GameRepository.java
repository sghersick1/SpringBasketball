package loyola.basketball.Repository;

import loyola.basketball.Entity.Game;
import loyola.basketball.Mapper.GameMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameRepository {
    private final JdbcTemplate jdbc;
    public GameRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public List<Game> schedule(){
        String sqlScript = "SELECT\n" +
                "    g.id_game,\n" +
                "    g.date,\n" +
                "    g.time,\n" +
                "    g.location,\n" +
                "    t1.id_team  AS id_team_1,\n" +
                "    t1.points   AS score_team_1,\n" +
                "    t2.id_team  AS id_team_2,\n" +
                "    t2.points   AS score_team_2\n" +
                "FROM Game g\n" +
                "JOIN Team_Plays_Game t1\n" +
                "    using(id_game)\n" +
                "JOIN Team_Plays_Game t2 using(id_game)\n" +
                "WHERE t1.id_team < t2.id_team\n" +
                "order by g.date, g.time;";
        List<Game> game = jdbc.query(sqlScript, new GameMapper());

        game.stream().forEach(System.out::println);
        return game;
    }
}
