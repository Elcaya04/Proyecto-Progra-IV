package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/oferente")
public class OferenteController {

    @Autowired
    private OferenteService oferenteService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // ---- DASHBOARD ----

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("usuarioEmail");
        model.addAttribute("oferente", oferenteService.buscarPorEmail(email));
        return "oferente/dashboard";
    }

    // ---- HABILIDADES ----

    @GetMapping("/habilidades")
    public String misHabilidades(HttpSession session, Model model) {
        String email = (String) session.getAttribute("usuarioEmail");
        model.addAttribute("habilidades",
                oferenteService.listarHabilidades(email));
        model.addAttribute("caracteristicas",
                caracteristicaService.listarTodas());
        return "oferente/habilidades";
    }

    @PostMapping("/habilidades/agregar")
    public String agregarHabilidad(
            @RequestParam Integer caracteristicaId,
            @RequestParam Integer nivel,
            HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        Oferente oferente = oferenteService.buscarPorEmail(email);
        Caracteristica caracteristica =
                caracteristicaService.buscarPorId(caracteristicaId);

        OferenteCaracteristica habilidad = new OferenteCaracteristica();
        habilidad.setOferente(oferente);
        habilidad.setCaracteristica(caracteristica);
        habilidad.setNivel(nivel);

        oferenteService.agregarHabilidad(habilidad);
        return "redirect:/oferente/habilidades";
    }

    // ---- CURRÍCULO ----

    @GetMapping("/curriculo")
    public String verCurriculo(HttpSession session, Model model) {
        String email = (String) session.getAttribute("usuarioEmail");
        model.addAttribute("oferente", oferenteService.buscarPorEmail(email));
        return "oferente/curriculo";
    }

    @PostMapping("/curriculo")
    public String subirCurriculo(
            @RequestParam MultipartFile archivo,
            HttpSession session) throws IOException {

        String email = (String) session.getAttribute("usuarioEmail");
        String nombreArchivo = email.replace("@", "_") + ".pdf";
        Path ruta = Paths.get("uploads/curriculos/" + nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.write(ruta, archivo.getBytes());

        oferenteService.guardarRutaCurriculo(email, nombreArchivo);
        return "redirect:/oferente/curriculo";
    }
}
