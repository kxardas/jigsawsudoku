package sk.tuke.gamestudio.server.webservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

  private final ScoreService scoreService;

  public ScoreServiceRest(ScoreService scoreService) {
    this.scoreService = scoreService;
  }

  @GetMapping("/{game}")
  public List<Score> getTopScores(@PathVariable String game) {
    return scoreService.getTopScores(game);
  }

  @PostMapping
  public void addScore(@RequestBody Score score) {
    throw new ResponseStatusException(
            HttpStatus.GONE,
            "Use /api/engine/games/{gameId}/score to submit score"
    );
  }
}