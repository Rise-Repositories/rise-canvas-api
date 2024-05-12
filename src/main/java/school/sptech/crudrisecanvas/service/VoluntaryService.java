package school.sptech.crudrisecanvas.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.repositories.VoluntaryRepository;

@Service
@RequiredArgsConstructor
public class VoluntaryService {
    private final VoluntaryRepository voluntaryRepository;
    public Voluntary createVoluntary(Voluntary voluntary){
        return  voluntaryRepository.save(voluntary);
    }

    
}
