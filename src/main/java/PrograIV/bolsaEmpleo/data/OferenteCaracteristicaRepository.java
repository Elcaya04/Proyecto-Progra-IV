package PrograIV.bolsaEmpleo.data;
import PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OferenteCaracteristicaRepository extends JpaRepository<OferenteCaracteristica, Long> {
    List<OferenteCaracteristica> findByOferenteEmail(String email);
}
