package school.sptech.crudrisecanvas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Tags;
import school.sptech.crudrisecanvas.repositories.TagsRepository;

@Service
@RequiredArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;

    public List<Tags> getAll(){
        List<Tags> tags = tagsRepository.findAll();
        return tags;
    }

    public List<Tags> getManyByIds(List<Integer> ids){
        List<Tags> tags = tagsRepository.findAllById(ids);
        return tags;
    }
}
