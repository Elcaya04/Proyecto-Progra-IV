package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.data.PuestoRepository;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller // Convierte esta clase en el oficial de tránsito
public class IndexController {

    @Autowired // Inyección de Dependencias: Le pedimos a Spring que nos traiga nuestro robot bibliotecario
    private PuestoRepository puestoRepository;

    // Cuando el usuario entre a "http://localhost:8080/" (ruta raíz), se ejecuta este método
    @GetMapping("/")
    public String mostrarLandingPage(Model model) {

        // 1. Le pedimos al repositorio todos los puestos (luego afinaremos esto para que sean solo 5 públicos)
        List<Puesto> puestos = puestoRepository.findAll();

        // 2. Metemos los puestos en la "maleta" (Model) para enviarlos a la página HTML
        model.addAttribute("puestos", puestos);

        // 3. Retornamos el nombre EXACTO del archivo HTML al que vamos a ir (sin el .html)
        return "index";
    }
}