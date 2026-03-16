package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OferenteService oferenteService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // ---- DASHBOARD ----

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // ---- EMPRESAS PENDIENTES ----

    @GetMapping("/empresas/pendientes")
    public String empresasPendientes(Model model) {
        model.addAttribute("empresas", empresaService.listarPendientes());
        return "admin/empresasPendientes";
    }

    @PostMapping("/empresas/aprobar/{email}")
    public String aprobarEmpresa(@PathVariable String email) {
        empresaService.aprobar(email);
        return "redirect:/admin/empresas/pendientes";
    }

    // ---- OFERENTES PENDIENTES ----

    @GetMapping("/oferentes/pendientes")
    public String oferentesPendientes(Model model) {
        model.addAttribute("oferentes", oferenteService.listarPendientes());
        return "admin/oferentesPendientes";
    }

    @PostMapping("/oferentes/aprobar/{email}")
    public String aprobarOferente(@PathVariable String email) {
        oferenteService.aprobar(email);
        return "redirect:/admin/oferentes/pendientes";
    }

    // ---- CARACTERÍSTICAS ----

    @GetMapping("/caracteristicas")
    public String caracteristicas(
            @RequestParam(required = false) Integer padreId,
            Model model) {
        model.addAttribute("raices",
                caracteristicaService.listarRaices());
        model.addAttribute("todasParaPadre",
                caracteristicaService.listarTodas());
        model.addAttribute("nuevaCaracteristica",
                new Caracteristica());
        if (padreId != null) {
            model.addAttribute("hijos",
                    caracteristicaService.listarHijos(padreId));
            model.addAttribute("padreActual",
                    caracteristicaService.buscarPorId(padreId));
        }
        return "admin/caracteristicas";
    }

    @PostMapping("/caracteristicas/crear")
    public String crearCaracteristica(
            @ModelAttribute Caracteristica caracteristica) {
        caracteristicaService.crear(caracteristica);
        return "redirect:/admin/caracteristicas";
    }
}
