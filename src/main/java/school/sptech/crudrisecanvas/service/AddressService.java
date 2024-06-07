package school.sptech.crudrisecanvas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.address.AddressViacepDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.repositories.AddressRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    public Integer getIdByCepAndNumber(String cep, Integer number) {
        Optional<Integer> idOpt = repository.findIdByCepAndNumber(cep, number);
        if (idOpt.isEmpty()) {
            return -1;
        } else {
            return idOpt.get();
        }
    }

    public Address saveByCep(String cep, Integer number, String complement) {
        RestClient client = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws/")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        cep = cep.replace("-", "");

        AddressViacepDto viacep = client.get()
                .uri(cep + "/json")
                .retrieve()
                .body(AddressViacepDto.class);

        viacep.setCep(viacep.getCep().replace("-", ""));

        Optional<Address> addressOpt = repository.findByCepAndNumber(viacep.getCep(), number);

        if (addressOpt.isPresent()) {
            return addressOpt.get();
        } else {
            Address address = AddressMapper.toEntity(viacep, number, complement);
            return repository.save(address);
        }
    }
}
