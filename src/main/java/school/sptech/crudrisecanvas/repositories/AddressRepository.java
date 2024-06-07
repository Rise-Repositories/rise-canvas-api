package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.sptech.crudrisecanvas.entities.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Integer> findIdByCepAndNumber(String cep, Integer number);

    Optional<Address> findByCepAndNumber(String cep, Integer number);
}
