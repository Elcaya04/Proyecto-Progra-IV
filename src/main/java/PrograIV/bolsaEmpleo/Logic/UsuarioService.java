package PrograIV.bolsaEmpleo.Logic;

import PrograIV.bolsaEmpleo.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String email, String clave) {
        return usuarioRepository.findByEmailAndClave(email, clave)
                .orElseThrow(() ->
                        new IllegalArgumentException("Correo o clave incorrectos"));
    }
}
