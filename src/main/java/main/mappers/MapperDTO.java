package main.mappers;

import main.dto.PostDTO;
import main.dto.TagDTO;
import main.model.Post;
import main.model.Tag;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MapperDTO {

  PostDTO PostToPostDto(Post post);

  TagDTO TagToTagDto(Tag tag);

}
