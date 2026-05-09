package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "gamestudio.console.enabled=false",
        "debug=false"
})
class JigsawsudokuApplicationTests {

  @Test
  void contextLoads() {
  }

}
