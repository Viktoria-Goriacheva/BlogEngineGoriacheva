package main.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final PostRepository postRepository;

  public CalendarResponse calendar(String year) {
    CalendarResponse calendarResponse = new CalendarResponse();
    List<Post> allPosts = postRepository.findAllPost();
    Set<Integer> years = setYears(allPosts);
    Map<String, Long> posts = findPosts(allPosts, year);

    calendarResponse.setPosts(posts);
    calendarResponse.setYears(years);
    return calendarResponse;
  }

  private Set<Integer> setYears(List<Post> allPosts) {
    Set<Integer> result = new TreeSet<>();
    for (Post post : allPosts) {
      result.add(post.getTime().getYear());
    }
    return result;
  }

  private Map<String, Long> findPosts(List<Post> allPosts, String year) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    List<String> dates = new ArrayList<>();
    for (Post post : allPosts) {
      if (String.valueOf(post.getTime().getYear()).equals(year)) {
        LocalDateTime datePost = post.getTime();
        String date = datePost.format(format);
        dates.add(date);
      }
    }
    return dates.stream().collect(Collectors.groupingBy(
        Function.identity(), Collectors.counting()));

  }
}
