package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Le decimos a Spring que este es nuestro robot de base de datos
@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> { // <Entidad, Tipo de Llave Primaria>

    // ¡MAGIA DE SPRING DATA!
    // Al nombrar el método así, Spring crea automáticamente el SQL:
    // SELECT * FROM puesto WHERE activo = true AND tipo = ?
    List<Puesto> findByActivoTrueAndTipo(String tipo);
    List<Puesto> findByEmpresaEmail(String email);
}