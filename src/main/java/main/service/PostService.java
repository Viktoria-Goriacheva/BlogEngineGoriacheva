package main.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import main.api.response.PostIdResponse;
import main.api.response.PostResponse;
import main.api.response.StatusResponse;
import main.dto.CommentDTO;
import main.dto.PostDTO;
import main.dto.UserDTOForPost;
import main.dto.UserDTOForPostId;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.PostStatus;
import main.model.PostVote;
import main.model.Tag;
import main.model.Tag2Post;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostCommentRepository postCommentRepository;
  private final TagRepository tagRepository;
  private final Tag2PostRepository tag2PostRepository;

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

  public PostResponse getAllPostsForModeratoin(ModerationStatus status, int offset, int limit) {
    PostResponse response = new PostResponse();
    List<Post> allPosts = new ArrayList<>();
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Integer moderatorId = userRepository.findByIdUserForMod(email);
    switch (status) {
      case NEW:
        allPosts = postRepository.findByPendingMod(moderatorId);
        break;
      case DECLINED:
        allPosts = postRepository.findByDeclinedMod(moderatorId);
        break;
      case ACCEPTED:
        allPosts = postRepository.findByPublishedMod(moderatorId);
        break;
    }
    List<PostDTO> postDtoList = preparePost(allPosts);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    response.setPostsDTO(postDtoList);
    response.setCount(postDtoList.size());
    return response;
  }

  public StatusResponse addPost(long timestamp, byte active, String title, List<String> tags,
      String text) {
    StatusResponse response = new StatusResponse();
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findUserByEmail(userEmail);
    Map<String, String> errors = checkTitleAndText(text, title);
    if (!errors.isEmpty()) {
      response.setErrors(errors);
      response.setResult(false);
      return response;
    }
    String textWithoutHTMLTags = Jsoup.parse(text).text();
    Post post = Post.builder()
        .isActive(active)
        .moderationStatus(ModerationStatus.NEW)
        .title(title)
        .viewCount(0)
        .user(user)
        .text(textWithoutHTMLTags)
        .time(checkTime(timestamp))
        .build();
    postRepository.save(post);
    post.setTags(addTags(tags, post));
    response.setResult(true);
    return response;
  }

  public StatusResponse changePost(long timestamp, byte active, String title, List<String> tags,
      String text, Integer ID) {
    Post post = postRepository.getOne(ID);
    StatusResponse response = new StatusResponse();
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findUserByEmail(userEmail);
    Map<String, String> errors = checkTitleAndText(text, title);
    if (!errors.isEmpty() || !user.equals(post.getUser())) {
      response.setErrors(errors);
      response.setResult(false);
      return response;
    }
    String textWithoutHTMLTags = Jsoup.parse(text).text();
    post.setIsActive(active);
    post.setText(textWithoutHTMLTags);
    post.setTitle(title);
    post.setTime(checkTime(timestamp));

    if (user.getIsModerator() == 0) {
      post.setModerationStatus(ModerationStatus.NEW);
    }
    postRepository.save(post);
    post.setTags(addTags(tags, post));
    response.setResult(true);
    return response;
  }

  private List<Tag> addTags(List<String> tags, Post post) {
    if (tags.isEmpty()) {
      return null;
    }
    if (!post.getTags().isEmpty()) {
      post.getTags().clear();
    }
    Set<Tag> result = new HashSet<>();

    for (String tagString : tags) {
      Tag tag = new Tag(tagString.toUpperCase());
      result.add(tag);
      tagRepository.save(tag);
    }
    List<Tag2Post> relations = new ArrayList<>();
    result.forEach(id -> relations.add(new Tag2Post(post, id)));
    tag2PostRepository.saveAll(relations);
    return result.stream().collect(Collectors.toList());
  }

  private LocalDateTime checkTime(long timestamp) {
    LocalDateTime timePost = LocalDateTime.now();
    if (System.currentTimeMillis() / 1000L > timestamp) {
      return timePost;
    } else {
      ZoneOffset localZone = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now());
      timePost = LocalDateTime.ofEpochSecond(timestamp, 0, localZone);
    }
    return timePost;
  }

  private Map<String, String> checkTitleAndText(String text, String title) {
    Map<String, String> result = new HashMap<>();
    if (text.isEmpty()) {
      result.put("text", "Текст не установлен");
    }
    if (text.length() < 50) {
      result.put("text", "Текст публикации слишком короткий");
    }
    if (title.isEmpty()) {
      result.put("title", "Заголовок не установлен");
    }
    if (title.length() < 3) {
      result.put("title", "Заголовок публикации слишком короткий");
    }
    return result;
  }

  public PostIdResponse getPostId(Integer id) {
    Post post = postRepository.getOne(id);
    List<PostVote> postVotesList = post.getPostVotes();
    int like = 0;
    int dislike = 0;
    for (PostVote postVote : postVotesList) {
      if (postVote.getValue() == 1) {
        like++;
      }
      dislike++;
    }
    Duration duration = Duration.between(post.getTime(), LocalDateTime.now());
    long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    List<String> list = new ArrayList<>();
    List<Tag> tags = post.getTags();
    for (Tag tag : tags) {
      list.add(tag.getName());
    }
    User userDTO = userRepository.findByIdUserForPostId(postRepository.findIdUser(id));
    UserDTOForPost user = UserDTOForPost.builder().id(userDTO.getId()).name(userDTO.getName())
        .build();
    PostIdResponse postId = PostIdResponse.builder()
        .id(post.getId())
        .active(getActive(post))
        .timestamp(secondsAfterCreatePost)
        .comments(getCommentsForPostId(id))
        .likeCount(like)
        .dislikeCount(dislike)
        .viewCount(getViewCounts(post))
        .tags(list)
        .text(post.getText())
        .title(post.getTitle())
        .user(user)
        .build();
    return postId;
  }

  private boolean getActive(Post post) {
    if (post.getIsActive() == 1) {
      return true;
    }
    return false;
  }

  private Integer getViewCounts(Post post) {
    int viewCounts = post.getViewCount();
    User userAuthorPost = post.getUser();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      String emailAuthUser = SecurityContextHolder.getContext().getAuthentication().getName();
      User userAuth = userRepository.findByEmail(emailAuthUser).orElseThrow();
      if (userAuth.getIsModerator() == 1 || emailAuthUser.equals(userAuthorPost.getEmail())) {
        return viewCounts;
      }
    }
    viewCounts++;
    post.setViewCount(viewCounts);
    postRepository.save(post);
    return viewCounts;
  }

  public PostResponse getMyPosts(int offset, int limit, PostStatus status) {
    PostResponse postResponse = new PostResponse();
    List<Post> allMyPosts = new ArrayList<>();
    String emailAuthUser = SecurityContextHolder.getContext().getAuthentication().getName();
    switch (status) {
      case INACTIVE:
        allMyPosts = postRepository.findByInactive(emailAuthUser);
        break;
      case PENDING:
        allMyPosts = postRepository.findByPending(emailAuthUser);
        break;
      case DECLINED:
        allMyPosts = postRepository.findByDeclined(emailAuthUser);
        break;
      case PUBLISHED:
        allMyPosts = postRepository.findByPublished(emailAuthUser);
        break;
      default:
        break;
    }
    List<PostDTO> postDtoList = preparePost(allMyPosts);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(allMyPosts.size());
    return postResponse;
  }

  public PostResponse getSearchPostQuery(int offset, int limit, String query) {
    if (query.isEmpty()) {
      getAllPosts(PostMode.RECENT, offset, limit);
    }
    PostResponse postResponse = new PostResponse();
    List<Post> allPostsListQuery = postRepository.findAllByQuery(query);
    List<PostDTO> postDtoList = preparePost(allPostsListQuery);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(allPostsListQuery.size());
    return postResponse;
  }

  private List<CommentDTO> getCommentsForPostId(Integer id) {
    List<CommentDTO> comments = new ArrayList<>();
    List<PostComment> postComment = postCommentRepository.findCommentsForPostId(id);
    for (PostComment comment : postComment) {
      Duration duration = Duration.between(comment.getRegTime(), LocalDateTime.now());
      long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
      User user = comment.getUser();
      UserDTOForPostId userDTOForPostId = UserDTOForPostId.builder()
          .id(user.getId())
          .name(user.getName())
          .photo(user.getPhoto())
          .build();
      CommentDTO commentDTO = CommentDTO.builder()
          .id(comment.getId())
          .text(comment.getText())
          .timestamp(secondsAfterCreatePost)
          .user(userDTOForPostId)
          .build();
      comments.add(commentDTO);
      comments.sort(Collections.reverseOrder(COMPARE_BY_REG_TIME));
    }
    return comments;
  }

  private List<PostDTO> preparePost(List<Post> posts) {
    List<PostDTO> result = new ArrayList<>();
    for (Post post : posts) {
      UserDTOForPost userDTOForPost = UserDTOForPost.builder()
          .id(post.getUser().getId())
          .name(post.getUser().getName())
          .build();
      PostDTO postDto = PostDTO.builder()
          .id(post.getId())
          .title(post.getTitle())
          .viewCount(post.getViewCount())
          .user(userDTOForPost)
          .commentCount(post.getComments().size())
          .build();
      setPostAnnounce(post, postDto);
      setPostDtoVotesCount(post, postDto);
      setPostTimestamp(post, postDto);
      result.add(postDto);
    }
    return result;
  }

  private void setPostAnnounce(Post post, PostDTO postDto) {
    int limit = 150;
    String text =
        (post.getText().length() > limit) ? post.getText().substring(0, limit).concat("...")
            : post.getText();
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

  public PostResponse getByDate(String date, int offset, int limit) {
    PostResponse postResponse = new PostResponse();
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime localDate = LocalDate.parse(date, newFormatter).atStartOfDay();
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

