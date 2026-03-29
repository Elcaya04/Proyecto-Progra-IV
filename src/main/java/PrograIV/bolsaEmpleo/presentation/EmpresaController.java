package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PuestoService puestoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private OferenteService oferenteService;


    private Empresa obtenerEmpresaLogueada(Principal principal) {
        return empresaService.buscarPorEmail(principal.getName());
    }

    @GetMapping("/dashboard")
    public String verDashboard(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);
        return "empresa/dashboard";
    }

    @GetMapping("/puestos")
    public String verMisPuestos(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);
        model.addAttribute("puestos", puestoService.listarPorEmpresa(empresa.getEmail()));
        return "empresa/puestos";
    }

    @GetMapping("/puestos/nuevo")
    public String nuevoPuesto(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);

        model.addAttribute("caracteristicas", caracteristicaService.listarTodas());
        return "empresa/nuevo-puesto";
    }

    @PostMapping("/puestos/desactivar")
    public String desactivarPuesto(@RequestParam Long id) {
        puestoService.desactivar(id);
        return "redirect:/empresa/puestos";
    }

    @PostMapping("/puestos/guardar")
    public String guardarPuesto(
            @RequestParam String descripcion,
            @RequestParam Double salario,
            @RequestParam String tipo,
            @RequestParam(required = false) List<Long> caracteristicasIds,
            HttpServletRequest request,
            Principal principal) {

        Empresa empresa = obtenerEmpresaLogueada(principal);

        Puesto nuevoPuesto = new Puesto();
        nuevoPuesto.setDescripcion(descripcion);
        nuevoPuesto.setSalario(salario);
        nuevoPuesto.setTipo(tipo);
        nuevoPuesto.setEmpresa(empresa);
        nuevoPuesto.setActivo(true);

        Map<Long, Integer> niveles = new HashMap<>();
        if (caracteristicasIds != null) {
            for (Long caracId : caracteristicasIds) {
                String nivelParam = request.getParameter("nivel_" + caracId);
                int nivel = 1;
                if (nivelParam != null && !nivelParam.isBlank()) {
                    try {
                        nivel = Integer.parseInt(nivelParam);
                    } catch (NumberFormatException ignored) {}
                }
                niveles.put(caracId, nivel);
            }
        }


        puestoService.guardarConCaracteristicas(nuevoPuesto, caracteristicasIds, niveles);

        return "redirect:/empresa/puestos";
    }

    @GetMapping("/puestos/{id}/candidatos")
    public String buscarCandidatos(@PathVariable Long id, Model model) {
        Puesto puesto = puestoService.buscarPorId(id);


        List<CandidatoMatch> candidatos = matchService.buscarCandidatos(id);

        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", candidatos);
        return "empresa/candidatos";
    }

    @GetMapping("/oferente/{email}")
    public String verDetalleOferente(@PathVariable String email, Model model) {
        Oferente oferente = oferenteService.buscarPorEmail(email);
        List<OferenteCaracteristica> habilidades = oferenteService.listarHabilidades(email);

        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);
        return "empresa/detalle-oferente";
    }
}
