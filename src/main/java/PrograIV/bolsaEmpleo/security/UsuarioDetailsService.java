package PrograIV.bolsaEmpleo.security;

import PrograIV.bolsaEmpleo.Logic.Usuario;
import PrograIV.bolsaEmpleo.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Le decimos a Spring que este es un servicio de lógica
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos al usuario por su correo en la base de datos
        Usuario usuario = usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));

        // 2. Verificamos si el administrador ya lo aprobó (estado == 1)
        // Spring Security usará este boolean para dejarlo entrar o bloquearlo
        boolean isEnabled = (usuario.getEstado() == 1);

        // 3. Convertimos nuestro 'Usuario' al formato 'UserDetails' que entiende Spring Security
        return new User(
                usuario.getEmail(),
                usuario.getClave(), // La clave encriptada de la BD
                isEnabled,          // ¿Está habilitado/aprobado?
                true,               // La cuenta no ha expirado
                true,               // Las credenciales no han expirado
                true,               // La cuenta no está bloqueada
                // Le asignamos su rol (ej. ROLE_EMPRESA, ROLE_OFERENTE, ROLE_ADMIN)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario()))
        );
    }
}