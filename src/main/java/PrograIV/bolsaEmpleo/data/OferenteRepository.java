package PrograIV.bolsaEmpleo.data;
import PrograIV.bolsaEmpleo.Logic.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OferenteRepository extends JpaRepository<Oferente, String> {
    List<Oferente> findByEstado(int estado);
}
