package PrograIV.bolsaEmpleo.data;
import PrograIV.bolsaEmpleo.Logic.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
}
