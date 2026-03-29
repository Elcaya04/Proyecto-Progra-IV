package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import PrograIV.bolsaEmpleo.Logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Revisando datos iniciales...");


        if (usuarioRepository.findById("admin@una.cr").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@una.cr");
            admin.setClave(passwordEncoder.encode("admin123"));
            admin.setEstado(1);
            admin.setTipoUsuario("ADMIN");

            usuarioRepository.save(admin);
            System.out.println(" Administrador creado: admin@una.cr / Clave: admin123");
        }


        if (puestoRepository.count() == 0) {


            Empresa empresa = new Empresa();
            empresa.setEmail("contacto@techcr.com");
            empresa.setClave(passwordEncoder.encode("12345"));
            empresa.setEstado(1);
            empresa.setTipoUsuario("EMPRESA");
            empresa.setNombre("Tech Solutions CR");
            empresa.setLocalizacion("San José, Costa Rica");
            empresa.setTelefono("2222-3333");
            empresa.setDescripcion("Empresa líder en desarrollo de software");

            empresaRepository.save(empresa);


            Puesto p1 = new Puesto();
            p1.setDescripcion("Desarrollador Junior Java Spring Boot");
            p1.setSalario(850000.0);
            p1.setActivo(true);
            p1.setTipo("PUBLICO");
            p1.setEmpresa(empresa);

            Puesto p2 = new Puesto();
            p2.setDescripcion("Especialista en Bases de Datos MySQL");
            p2.setSalario(950000.0);
            p2.setActivo(true);
            p2.setTipo("PUBLICO");
            p2.setEmpresa(empresa);

            puestoRepository.save(p1);
            puestoRepository.save(p2);

            System.out.println(" Datos de prueba (Empresa y Puestos) cargados con éxito!");
        }
    }
}