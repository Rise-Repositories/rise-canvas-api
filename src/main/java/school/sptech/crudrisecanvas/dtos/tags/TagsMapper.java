package school.sptech.crudrisecanvas.dtos.tags;

import java.util.List;

import school.sptech.crudrisecanvas.entities.Tags;

public class TagsMapper {
    public static TagsResponseDto toResponse(Tags tags) {
        TagsResponseDto tagsReponseDto = new TagsResponseDto();
        tagsReponseDto.setId(tags.getId());
        tagsReponseDto.setName(tags.getName());
        return tagsReponseDto;
    }

    public static Tags toEntity(TagsRequestDto tagsRequestDto) {
        Tags tags = new Tags();
        tags.setName(tagsRequestDto.getName());
        return tags;
    }

    public static List<Tags> toEntity(List<TagsRequestDto> tagsRequestDto) {
        return tagsRequestDto == null
            ? null
            : tagsRequestDto.stream().map(TagsMapper::toEntity).toList();
    }

    public static List<TagsResponseDto> toResponse(List<Tags> tags) {
        return tags == null
            ? null
            : tags.stream().map(TagsMapper::toResponse).toList();
    }
}
