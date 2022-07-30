package main.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.api.response.PostResponse;
import main.dto.PostDTO;
import main.mappers.MapperDTO;
import main.model.Post;
import main.model.PostVote;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final MapperDTO mapperDTO;

  public PostResponse getAllPosts(PostMode mode, int offset, int limit) {
    PostResponse postResponse = new PostResponse();
    List<Post> allPostsList = postRepository.findAll();
    List<PostDTO> postDtoList = preparePost(allPostsList);
    sortCollection(mode, postDtoList);
    postDtoList = getCollectionsByOffsetLimit(offset, limit, postDtoList);
    postResponse.setPostsDTO(postDtoList);
    postResponse.setCount(postDtoList.size());
    return postResponse;
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

  private void setPostCommentsCount(Post post, PostDTO postDto) {
    postDto.setCommentCount(post.getComments().size());
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
      case popular:
        postDtoList.sort(COMPARE_BY_COMMENT_COUNT);
        break;
      case best:
        postDtoList.sort(COMPARE_BY_LIKE_COUNT);
        break;
      case early:
        postDtoList.sort(Collections.reverseOrder(COMPARE_BY_TIME));
        break;
      default:
        postDtoList.sort(COMPARE_BY_TIME);
        break;
    }
  }

  private List<PostDTO> getCollectionsByOffsetLimit(int offset, int limit,
      List<PostDTO> postDtoList) {

    if (offset + limit > postDtoList.size()) {
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

  private static final Comparator<PostDTO>
      COMPARE_BY_TIME = (o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()),
      COMPARE_BY_LIKE_COUNT = (o1, o2) -> Integer.compare(o2.getLikeCount(), o1.getLikeCount()),
      COMPARE_BY_COMMENT_COUNT = (o1, o2) -> Integer.compare(o2.getCommentCount(),
          o1.getCommentCount());

}

