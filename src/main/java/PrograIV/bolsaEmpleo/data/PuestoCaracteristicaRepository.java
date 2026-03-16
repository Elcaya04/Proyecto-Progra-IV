package PrograIV.bolsaEmpleo.data;
import PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Long> {
    List<PuestoCaracteristica> findByPuestoId(Integer puestoId);
}
