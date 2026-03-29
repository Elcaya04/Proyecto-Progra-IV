package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.*;
import PrograIV.bolsaEmpleo.data.OferenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/oferente")
public class OferenteController {

    @Autowired
    private OferenteService oferenteService;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.OferenteCaracteristicaRepository oferenteCaracteristicaRepository;


    private Oferente obtenerOferenteLogueado(Principal principal) {
        if (principal == null) return null;
        return oferenteRepository.findById(principal.getName()).orElse(null);
    }



    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {

        Oferente oferente = obtenerOferenteLogueado(principal);
        model.addAttribute("oferente", oferente);
        return "oferente/dashboard";
    }



    @GetMapping("/habilidades")
    public String verHabilidades(@RequestParam(required = false) Long padreId, Model model, Principal principal) {
        Oferente oferente = obtenerOferenteLogueado(principal);
        if (oferente == null) return "redirect:/login";


        model.addAttribute("misHabilidades", oferenteCaracteristicaRepository.findByOferenteEmail(oferente.getEmail()));


        List<PrograIV.bolsaEmpleo.Logic.Caracteristica> subcategorias;
        PrograIV.bolsaEmpleo.Logic.Caracteristica padreActual = null;

        if (padreId == null) {
            subcategorias = caracteristicaRepository.findByPadreIsNull();
        } else {
            padreActual = caracteristicaRepository.findById(padreId).orElse(null);
            subcategorias = caracteristicaRepository.findByPadreId(padreId);
        }

        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("padreActual", padreActual);

        return "oferente/habilidades";
    }

    @PostMapping("/habilidades/agregar")
    public String agregarHabilidad(@RequestParam Long caracteristicaId, @RequestParam Integer nivel, Principal principal) {
        Oferente oferente = obtenerOferenteLogueado(principal);
        PrograIV.bolsaEmpleo.Logic.Caracteristica carac = caracteristicaRepository.findById(caracteristicaId).orElse(null);

        if (carac != null && oferente != null) {

            PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica nueva = new PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica();
            nueva.setOferente(oferente);
            nueva.setCaracteristica(carac);
            nueva.setNivel(nivel);
            oferenteCaracteristicaRepository.save(nueva);
        }
        return "redirect:/oferente/habilidades";
    }



    @GetMapping("/cv")
    public String verCurriculo(Principal principal, Model model) {
        Oferente oferente = obtenerOferenteLogueado(principal);
        model.addAttribute("oferente", oferente);
        return "oferente/curriculo";
    }

    @PostMapping("/curriculo/subir")
    public String subirCurriculo(@RequestParam("archivo") MultipartFile archivo, Principal principal) throws IOException {

        if (archivo.isEmpty()) return "redirect:/oferente/curriculo?error=vacio";

        String email = principal.getName();
        String nombreArchivo = email.replace("@", "_") + ".pdf";


        Path ruta = Paths.get("uploads/curriculos/" + nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.write(ruta, archivo.getBytes());

        oferenteService.guardarRutaCurriculo(email, nombreArchivo);
        return "redirect:/oferente/curriculo?exito=true";
    }
}