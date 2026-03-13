package PrograIV.bolsaEmpleo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta anotación es vital: le dice a Spring que arranque el servidor
// y que busque las @Entity dentro de la carpeta Logic.
@SpringBootApplication
public class BolsaEmpleoApplication {

    public static void main(String[] args) {
        // Aquí es donde arranca la magia de Spring Boot y Tomcat
        SpringApplication.run(BolsaEmpleoApplication.class, args);
    }
}