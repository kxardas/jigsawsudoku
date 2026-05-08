package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.service.auth.AuthenticatedUserService;
import sk.tuke.gamestudio.service.comment.CommentService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

  private final CommentService commentService;
  private final AuthenticatedUserService authenticatedUserService;

  public CommentServiceRest(
          CommentService commentService,
          AuthenticatedUserService authenticatedUserService
  ) {
    this.commentService = commentService;
    this.authenticatedUserService = authenticatedUserService;
  }

  @GetMapping("/{game}")
  public List<Comment> getComments(@PathVariable String game) {
    return commentService.getComments(game);
  }

  @PostMapping
  public void addComment(
          @RequestBody Comment comment,
          HttpServletRequest request
  ) {
    User user = authenticatedUserService.getAuthenticatedUser(request);

    if (comment.getGame() == null || comment.getGame().isBlank()) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Game name is required"
      );
    }

    if (comment.getText() == null || comment.getText().trim().isEmpty()) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Comment cannot be empty"
      );
    }

    String text = comment.getText().trim();

    if (text.length() > 500) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Comment is too long"
      );
    }

    Comment safeComment = new Comment(
            comment.getGame(),
            user.getUsername(),
            text,
            new Date()
    );

    commentService.addComment(safeComment);
  }
}