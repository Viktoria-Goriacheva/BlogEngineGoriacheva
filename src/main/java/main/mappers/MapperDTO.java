package main.mappers;

import main.api.response.PostIdResponse;
import main.dto.CommentDTO;
import main.dto.PostDTO;
import main.dto.UserDTOForPost;
import main.dto.UserDTOForPostId;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MapperDTO {

  PostDTO PostToPostDto(Post post);

  PostIdResponse PostToPostIdResponse(Post post);

  CommentDTO PostCommentToPostDTO(PostComment post);

  UserDTOForPost UserToUserDTOForPost(User user);

  UserDTOForPostId UserToUserDTOForPostId(User user);
}
