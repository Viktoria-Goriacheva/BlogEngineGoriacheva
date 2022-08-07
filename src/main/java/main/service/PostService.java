package main.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.api.response.PostIdResponse;
import main.api.response.PostResponse;
import main.dto.CommentDTO;
import main.dto.PostDTO;
import main.dto.UserDTOForPost;
import main.dto.UserDTOForPostId;
import main.mappers.MapperDTO;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostCommentRepository postCommentRepository;
  private final MapperDTO mapperDTO;

  public PostResponse getAllPosts(PostMode mode, int offset, int limit) {
    PostResponse postResponse = new PostResponse();
    List<Post> allPostsList = postRepository.findAllPost();
    List<PostDTO> postDtoList = preparePost(allPostsList);
    sortCollection(mode, postDtoList);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(allPostsList.size());
    return postResponse;
  }

  public PostIdResponse getPostId(Integer id) {
    PostIdResponse postId;
    Post post = postRepository.findByIdPost(id);
    postId = mapperDTO.PostToPostIdResponse(post);
    List<PostVote> postVotesList = post.getPostVotes();
    int like = 0;
    int dislike = 0;
    for (PostVote postVote : postVotesList) {
      if (postVote.getValue() == 1) {
        like++;
      }
      dislike++;
    }
    postId.setLikeCount(like);
    postId.setDislikeCount(dislike);
    Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
    long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    postId.setTimestamp(secondsAfterCreatePost);
    User userDTO = userRepository.findByIdUserForPostId(postRepository.findIdUser(id));
    UserDTOForPost user = mapperDTO.UserToUserDTOForPost(userDTO);
    postId.setUser(user);
    postId.setTags(getTagsForPostId(id));
    postId.setComments(getCommentsForPostId(id));
    postId.setActive(getActive(post));
    postId.setViewCount(getViewCounts(user, post));

    return postId;
  }

  private boolean getActive(Post post) {
    //Параметр active в ответе используется админ частью фронта, должно быть значение true если пост
    // опубликован и false если скрыт (при этом модераторы и автор поста будет его видеть)
//    if (post.getModerationStatus() == ModerationStatus.ACCEPTED)
//      return true;

    return false;
  }

  private List<String> getTagsForPostId(Integer id) {
    List<String> list = postRepository.findTagsList(id);
    return list;
  }

  private Integer getViewCounts(UserDTOForPost user, Post post) {
    //доработать после авторизации Если модератор авторизован, то не считаем его просмотры вообще
    //Если автор авторизован, то не считаем просмотры своих же публикаций
//   int viewCounts = post.getViewCount();
//    byte moderator = userRepository.findByIdUserId(user.getId()).getIsModerator();
//    if (moderator == 0) {
    int viewCounts = post.getViewCount() + 1;
    post.setViewCount(viewCounts);
//     postRepository.save(post);
//    }
    return viewCounts;
  }

  private List<CommentDTO> getCommentsForPostId(Integer id) {
    List<CommentDTO> comments = new ArrayList<>();
    List<PostComment> postComment = postCommentRepository.findCommentsForPostId(id);
//    Map<Integer, List<CommentDTO>> commentsWithParent = new HashMap<>();
    for (PostComment comment : postComment) {
//      Integer i = comment.getParentId();
      CommentDTO commentDTO = mapperDTO.PostCommentToPostDTO(comment);
      Duration duration = Duration.between(comment.getRegTime(), LocalDateTime.now());
      long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
      commentDTO.setTimestamp(secondsAfterCreatePost);
      UserDTOForPostId user = mapperDTO.UserToUserDTOForPostId(comment.getUser());
      commentDTO.setUser(user);
//      if (i == null) {
        comments.add(commentDTO);
//      } else {
//        List<CommentDTO> target = commentsWithParent.get(i);
//
//        if(target == null)
//        {
//          target = new ArrayList<>();
//          commentsWithParent.put(i,target);
//        }
//        target.add(commentDTO);
//        commentsWithParent.put(i, target);
//      }
//    }
   comments.sort(Collections.reverseOrder(COMPARE_BY_REG_TIME));
//    for (Map.Entry<Integer,  List<CommentDTO>> entry : commentsWithParent.entrySet()) {
//      for (CommentDTO commentDTO : comments) {
//        if (commentDTO.getId() == entry.getKey()) {
//          commentDTO.setListOfParent(entry.getValue());
//        }
//      }
    }
    return comments;
  }

  private List<PostDTO> preparePost(List<Post> posts) {
    List<PostDTO> result = new ArrayList<>();
    for (Post post : posts) {
      PostDTO postDto = mapperDTO.PostToPostDto(post);
      setPostAnnounce(post, postDto);
      setPostDtoVotesCount(post, postDto);
      setPostCommentsCount(post, postDto);
      setPostTimestamp(post, postDto);
      result.add(postDto);
    }
    return result;
  }

  public PostResponse getByDate(String date, int offset, int limit) {
    PostResponse postResponse = new PostResponse();
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.parse(date, newFormatter);
    List<Post> allPostsListWithDate = postRepository.findByDate(localDate);
    List<PostDTO> postDtoList = preparePost(allPostsListWithDate);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(allPostsListWithDate.size());
    return postResponse;
  }

  public PostResponse getByTag(String tag, int offset, int limit) {
    PostResponse postResponse = new PostResponse();
    List<Post> allPostsListWithTag = postRepository.findByTag(tag);
    List<PostDTO> postDtoList = preparePost(allPostsListWithTag);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(allPostsListWithTag.size());
    return postResponse;
  }

  private void setPostCommentsCount(Post post, PostDTO postDTO) {
    postDTO.setCommentCount(post.getComments().size());
  }

  private void setPostAnnounce(Post post, PostDTO postDto) {
    int limit = 150;
    String text = (post.getText().length() > limit) ? post.getText().concat("...") : post.getText();
    postDto.setAnnounce(text);
  }

  private void setPostDtoVotesCount(Post post, PostDTO postDto) {
    List<PostVote> postVotesList = post.getPostVotes();
    int like = 0;
    int dislike = 0;
    for (PostVote postVote : postVotesList) {
      if (postVote.getValue() == 1) {
        like++;
      }
      dislike++;
    }
    postDto.setLikeCount(like);
    postDto.setDislikeCount(dislike);
  }

  private void setPostTimestamp(Post post, PostDTO postDto) {
    Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
    long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    postDto.setTimestamp(secondsAfterCreatePost);
  }

  private void sortCollection(PostMode mode, List<PostDTO> postDtoList) {

    switch (mode) {
      case POPULAR:
        postDtoList.sort(COMPARE_BY_COMMENT_COUNT);
        break;
      case BEST:
        postDtoList.sort(COMPARE_BY_LIKE_COUNT);
        break;
      case EARLY:
        postDtoList.sort(Collections.reverseOrder(COMPARE_BY_TIME));
        break;
      default:
        postDtoList.sort(COMPARE_BY_TIME);
        break;
    }
  }

  private List<PostDTO> getCollectionsByOffsetLimit(int offset, int limit,
      List<PostDTO> postDtoList) {

    if (offset + limit >= postDtoList.size()) {
      postDtoList = postDtoList.subList(offset, postDtoList.size());
    } else {
      if (offset == 0) {
        postDtoList = postDtoList.subList(offset, limit);
      } else {
        int rightBorder = offset + limit;
        postDtoList = postDtoList.subList(offset, rightBorder);
      }
    }
    return postDtoList;
  }

  private static final Comparator<CommentDTO>
      COMPARE_BY_REG_TIME = Comparator.comparingLong(CommentDTO::getTimestamp);
  private static final Comparator<PostDTO>
      COMPARE_BY_TIME = (o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()),
      COMPARE_BY_LIKE_COUNT = (o1, o2) -> Integer.compare(o2.getLikeCount(), o1.getLikeCount()),
      COMPARE_BY_COMMENT_COUNT = (o1, o2) -> Integer.compare(o2.getCommentCount(),
          o1.getCommentCount());
}

