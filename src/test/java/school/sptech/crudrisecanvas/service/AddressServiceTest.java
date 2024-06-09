package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.repositories.AddressRepository;
import school.sptech.crudrisecanvas.unittestutils.AddressMocks;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service")
class AddressServiceTest {

    @Nested
    @DisplayName("saveByCep()")
    public class saveByCep {

        @InjectMocks
        private AddressService service;
        @Mock
        private AddressRepository repository;

        @Test
        @DisplayName("V. Caso endereço ainda não exista, salvar no banco")
        void noAddress() {
            String cep = "05334050";
            Integer number = 25;
            String complement = "";
            Address address = AddressMocks.getAddress();

            Mockito.when(repository.findByCepAndNumber(cep, number)).thenReturn(Optional.empty());
            Mockito.when(repository.save(any())).thenReturn(address);

            Address savedAddress = service.saveByCep(cep, number, complement);

            assertEquals(savedAddress.getId(), address.getId());
            assertEquals(savedAddress.getCep(), address.getCep());
            assertEquals(savedAddress.getState(), address.getState());
            assertEquals(savedAddress.getCity(), address.getCity());
            assertEquals(savedAddress.getNeighbourhood(), address.getNeighbourhood());
            assertEquals(savedAddress.getStreet(), address.getStreet());
            assertEquals(savedAddress.getNumber(), address.getNumber());
            assertEquals(savedAddress.getComplement(), address.getComplement());

            Mockito.verify((repository), Mockito.times(1)).save(any());
        }

        @Test
        @DisplayName("V. Caso endereço já exista, retornar endereço existente")
        void alreadyExistsAddress() {
            String cep = "05334050";
            Integer number = 25;
            String complement = "";
            Address address = AddressMocks.getAddress();

            Mockito.when(repository.findByCepAndNumber(cep, number)).thenReturn(Optional.of(address));

            Address savedAddress = service.saveByCep(cep, number, complement);

            assertEquals(savedAddress.getId(), address.getId());
            assertEquals(savedAddress.getCep(), address.getCep());
            assertEquals(savedAddress.getState(), address.getState());
            assertEquals(savedAddress.getCity(), address.getCity());
            assertEquals(savedAddress.getNeighbourhood(), address.getNeighbourhood());
            assertEquals(savedAddress.getStreet(), address.getStreet());
            assertEquals(savedAddress.getNumber(), address.getNumber());
            assertEquals(savedAddress.getComplement(), address.getComplement());
        }

        @Test
        @DisplayName("F. Caso CEP seja inválido, deve lançar exceção")
        void invalidCep() {
            String cep = "00000000";
            Integer number = 25;
            String complement = "";

            BadRequestException exception = assertThrows(
                    BadRequestException.class,
                    () -> service.saveByCep(cep, number, complement)
            );

            assertEquals("CEP inválido", exception.getLocalizedMessage());
        }
    }
}