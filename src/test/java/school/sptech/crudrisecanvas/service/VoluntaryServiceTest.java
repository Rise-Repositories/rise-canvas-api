package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.repositories.VoluntaryRepository;
import school.sptech.crudrisecanvas.unittestutils.VoluntaryMocks;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Voluntary Service")
class VoluntaryServiceTest {

    @InjectMocks
    private VoluntaryService service;

    @Mock
    private VoluntaryRepository repository;

    @Nested
    @DisplayName("createVoluntary()")
    public class createVoluntary {

        @Test
        @DisplayName("V. Quando dados estiverem corretos, deve retornar associação")
        void validData() {
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(repository.save(voluntary)).thenReturn(voluntary);

            Voluntary returnedVoluntary = service.createVoluntary(voluntary);

            assertEquals(returnedVoluntary.getUser(), voluntary.getUser());
            assertEquals(returnedVoluntary.getOng(), voluntary.getOng());
            assertEquals(returnedVoluntary.getRole(), voluntary.getRole());

            Mockito.verify(repository, Mockito.times(1)).save(voluntary);
        }

    }
}