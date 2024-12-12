package school.sptech.crudrisecanvas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.address.AddressViacepDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.repositories.AddressRepository;
import school.sptech.crudrisecanvas.utils.annotations.CEPUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    public Address saveByCep(String cep, Integer number, String complement) {

        Optional<Address> fromDatabase = repository.findByCepAndNumberAndComplement(cep, number, complement);
        if (fromDatabase.isPresent()) {
            return fromDatabase.get();
        }

        RestClient client = RestClient.builder()
                .baseUrl("https://%s/viacep/ws/".formatted(CEPUtil.getFrontIp()))
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        cep = cep.replace("-", "");

        AddressViacepDto viacep = client.get()
                .uri(cep + "/json")
                .retrieve()
                .body(AddressViacepDto.class);

        if (viacep.getCep() == null) {
            throw new BadRequestException("CEP inválido");
        }

        viacep.setCep(viacep.getCep().replace("-", ""));

        Optional<Address> addressOpt = repository.findByCepAndNumber(viacep.getCep(), number);

        if (addressOpt.isPresent()) {
            return addressOpt.get();
        } else {
            Address address = AddressMapper.toEntity(viacep, number, complement);
            return repository.save(address);
        }
    }

    public Address save(Address address) {
        return repository.save(address);
    }
}
