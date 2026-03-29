package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {


    List<Puesto> findByActivoTrueAndTipo(String tipo);
    List<Puesto> findByEmpresaEmail(String email);
    @Query("SELECT DISTINCT p FROM Puesto p JOIN PuestoCaracteristica pc " +
            "ON pc.puesto = p WHERE pc.caracteristica.id IN :ids AND p.activo = true")
    List<Puesto> findByCaracteristicas(@Param("ids") List<Long> ids);
}