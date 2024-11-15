package school.sptech.crudrisecanvas.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.tags.TagsMapper;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;
import school.sptech.crudrisecanvas.service.TagsService;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;
    
    @GetMapping
    public ResponseEntity<List<TagsResponseDto>> getTags() {
        List<TagsResponseDto> tags = TagsMapper.toResponse(tagsService.getAll());

        return ResponseEntity.status(200).body(tags);
    }
}
