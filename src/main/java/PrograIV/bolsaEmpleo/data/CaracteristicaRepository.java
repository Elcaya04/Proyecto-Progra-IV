package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {


    List<Caracteristica> findByPadreIsNull();

    List<Caracteristica> findByPadreId(Long padreId);
}