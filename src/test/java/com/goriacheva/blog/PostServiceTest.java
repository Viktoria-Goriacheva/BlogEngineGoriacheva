package com.goriacheva.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.goriacheva.blog.api.response.PostResponse;
import com.goriacheva.blog.dto.PostDto;
import com.goriacheva.blog.dto.UserDtoForPost;
import com.goriacheva.blog.model.Post;
import com.goriacheva.blog.model.PostComment;
import com.goriacheva.blog.model.PostVote;
import com.goriacheva.blog.model.Tag;
import com.goriacheva.blog.model.User;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.service.PostMode;
import com.goriacheva.blog.service.PostService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostService postService;

  private Post post;

  @BeforeEach
  public void setup() {
    User user = User.builder().id(1).isModerator((byte) 1).email("grumblya@mail.ru").name("name")
        .regTime(LocalDateTime.now()).password("password").build();
    List<PostComment> comments = new ArrayList<>();
    List<PostVote> postVotesList = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    post = Post.builder()
        .id(1)
        .text("texttexttexttexttexttexttexttexttexttexttexttexttext1")
        .time(LocalDateTime.of(2022, 8, 1, 0, 0))
        .isActive((byte) 1)
        .title("title")
        .user(user)
        .viewCount(5)
        .comments(comments)
        .postVotes(postVotesList)
        .tags(tags)
        .build();
  }

  @DisplayName("JUnit test for getAllPosts method")
  @Test
  public void getAllPostsTest() {

    PostResponse expectedResponse = new PostResponse();
    List<Post> postList = Arrays.asList(post);
    when(postRepository.findAllPost()).thenReturn(postList);
    expectedResponse.setCount(getExpectedPostDtoList(post).size());
    expectedResponse.setPostsDto(getExpectedPostDtoList(post));

    PostResponse actualResponse = postService.getAllPosts(PostMode.RECENT, 0, 5);
    assertEquals(expectedResponse.getCount(), actualResponse.getCount());
  }

  @Test
  @DisplayName("Find post with query")
  void getSearchPostQueryTest() {
    PostResponse expectedResponse = new PostResponse();
    List<Post> postList = Arrays.asList(post);
    String query = "title";
    when(postRepository.findAllByQuery(query)).thenReturn(postList);
    expectedResponse.setCount(getExpectedPostDtoList(post).size());
    expectedResponse.setPostsDto(getExpectedPostDtoList(post));
    PostResponse actualResponse = postService.getSearchPostQuery(0, 5, query);
    assertEquals(expectedResponse.getCount(), actualResponse.getCount());

  }

  @Test
  @DisplayName("Get post by correct date.")
  void getByDateTest() {
    PostResponse expectedResponse = new PostResponse();
    List<Post> postList = Arrays.asList(post);
    when(postRepository.findByDate(LocalDateTime.of(2022, 8, 1, 0, 0))).thenReturn(postList);
    expectedResponse.setCount(getExpectedPostDtoList(post).size());
    expectedResponse.setPostsDto(getExpectedPostDtoList(post));

    PostResponse actualResponse = postService.getByDate("2022-08-01", 0, 5);
    assertEquals(expectedResponse.getPostsDto().get(0).getId(),
        actualResponse.getPostsDto().get(0).getId());
  }

  private List<PostDto> getExpectedPostDtoList(Post post) {
    List<PostDto> postDtoListExpected = new ArrayList<>();
    LocalDateTime localDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
    Duration duration = Duration.between(localDateTime, LocalDateTime.now());
    long secondsAfterCreatePost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    UserDtoForPost userDtoForPost1 = UserDtoForPost.builder().id(1).name("name").build();
    PostDto postDtoExpected = PostDto.builder()
        .id(1)
        .viewCount(post.getViewCount())
        .announce(post.getText())
        .likeCount(1)
        .dislikeCount(1)
        .title("title")
        .user(userDtoForPost1)
        .timestamp(secondsAfterCreatePost)
        .build();

    postDtoListExpected.add(postDtoExpected);

    return postDtoListExpected;
  }
}
