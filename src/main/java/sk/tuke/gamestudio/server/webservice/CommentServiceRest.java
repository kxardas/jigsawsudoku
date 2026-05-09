package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

  private final CommentService commentService;

  public CommentServiceRest(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("/{game}")
  public List<Comment> getComments(@PathVariable String game) {
    return commentService.getComments(game);
  }

  @PostMapping
  public void addComment(@RequestBody Comment comment) {
    commentService.addComment(comment);
  }
}
