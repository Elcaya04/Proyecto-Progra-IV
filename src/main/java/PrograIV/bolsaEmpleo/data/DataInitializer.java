package PrograIV.bolsaEmpleo.data;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Le dice a Spring: "Ejecuta esta clase cuando arranques"
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Solo metemos datos si la base de datos está vacía
        if (puestoRepository.count() == 0) {
            System.out.println("Cargando datos de prueba en la base de datos...");

            // 1. Crear la Empresa (Empresa hereda de Usuario)
            Empresa empresa = new Empresa();
            empresa.setEmail("contacto@techcr.com"); // Llave primaria heredada
            empresa.setClave("12345"); // Heredada
            empresa.setEstado(1); // Heredada
            empresa.setTipoUsuario("EMPRESA"); // ¡Importante agregar el rol!
            empresa.setNombre("Tech Solutions CR");
            empresa.setLocalizacion("San José, Costa Rica");
            empresa.setTelefono("2222-3333");
            empresa.setDescripcion("Empresa líder en desarrollo de software");

            // Guardamos la empresa PRIMERO (Requisito por la llave foránea)
            empresaRepository.save(empresa);

            // 2. Crear Puestos de prueba
            Puesto p1 = new Puesto();
            p1.setDescripcion("Desarrollador Junior Java Spring Boot");
            p1.setSalario(850000.0);
            p1.setActivo(true);
            p1.setTipo("PUBLICO");
            p1.setEmpresa(empresa); // ¡AQUÍ ESTÁ LA MAGIA! Le pasamos el objeto empresa completo

            Puesto p2 = new Puesto();
            p2.setDescripcion("Especialista en Bases de Datos MySQL");
            p2.setSalario(950000.0);
            p2.setActivo(true);
            p2.setTipo("PUBLICO");
            p2.setEmpresa(empresa); // Ambos puestos son de la misma empresa

            // Guardamos los puestos
            puestoRepository.save(p1);
            puestoRepository.save(p2);

            System.out.println("¡Datos de prueba cargados con éxito!");
        }
    }
}