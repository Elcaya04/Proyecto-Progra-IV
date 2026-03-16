package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PuestoService puestoService;

    @Autowired
    private OferenteService oferenteService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // ---- DASHBOARD ----

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("usuarioEmail");
        model.addAttribute("empresa", empresaService.buscarPorEmail(email));
        return "empresa/dashboard";
    }

    // ---- MIS PUESTOS ----

    @GetMapping("/puestos")
    public String misPuestos(HttpSession session, Model model) {
        String email = (String) session.getAttribute("usuarioEmail");
        model.addAttribute("puestos", puestoService.listarPorEmpresa(email));
        return "empresa/puestos";
    }

    @GetMapping("/puestos/publicar")
    public String formPublicarPuesto(Model model) {
        model.addAttribute("puesto", new Puesto());
        model.addAttribute("caracteristicas",
                caracteristicaService.listarTodas());
        return "empresa/publicarPuesto";
    }

    @PostMapping("/puestos/publicar")
    public String publicarPuesto(
            @ModelAttribute Puesto puesto,
            HttpSession session) {
        String email = (String) session.getAttribute("usuarioEmail");
        Empresa empresa = empresaService.buscarPorEmail(email);
        puesto.setEmpresa(empresa);
        puestoService.publicar(puesto);
        return "redirect:/empresa/puestos";
    }

    @PostMapping("/puestos/desactivar/{id}")
    public String desactivarPuesto(@PathVariable Integer id) {
        puestoService.desactivar(id);
        return "redirect:/empresa/puestos";
    }

    // ---- BUSCAR CANDIDATOS ----

    @GetMapping("/candidatos/buscar")
    public String buscarCandidatos(
            @RequestParam Integer puestoId,
            Model model) {
        model.addAttribute("puesto",
                puestoService.buscarPorId(puestoId));
        model.addAttribute("candidatos",
                oferenteService.buscarCandidatos(puestoId));
        return "empresa/candidatos";
    }

    @GetMapping("/candidatos/{email}")
    public String verDetallesCandidato(
            @PathVariable String email,
            Model model) {
        model.addAttribute("oferente",
                oferenteService.buscarPorEmail(email));
        model.addAttribute("habilidades",
                oferenteService.listarHabilidades(email));
        return "empresa/detallesCandidato";
    }
}
