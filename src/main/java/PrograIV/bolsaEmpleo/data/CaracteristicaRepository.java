package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    // Raíces del árbol jerárquico (las que no tienen padre)
    List<Caracteristica> findByPadreIsNull();

    // Hijos directos de una característica padre
    List<Caracteristica> findByPadreId(Long padreId);
}