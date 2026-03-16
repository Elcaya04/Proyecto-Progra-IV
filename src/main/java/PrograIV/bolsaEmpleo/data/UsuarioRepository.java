package PrograIV.bolsaEmpleo.data;
import PrograIV.bolsaEmpleo.Logic.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmailAndClave(String email, String clave);
}
