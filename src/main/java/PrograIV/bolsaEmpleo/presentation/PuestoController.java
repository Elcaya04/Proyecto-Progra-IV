package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Caracteristica;
import PrograIV.bolsaEmpleo.Logic.CaracteristicaService;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import PrograIV.bolsaEmpleo.Logic.PuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/puestos")
public class PuestoController {

    @Autowired
    private PuestoService puestoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping("/buscar-por-caracteristicas")
    public String buscarPuestos(
            @RequestParam(required = false) List<Long> ids,
            Model model,
            Principal principal) {

        List<Caracteristica> raices = caracteristicaService.listarRaices();
        model.addAttribute("raices", raices);
        model.addAttribute("idsSeleccionados", ids != null ? ids : new ArrayList<>());

        List<Puesto> puestosEncontrados;

        if (ids == null || ids.isEmpty()) {
            puestosEncontrados = puestoService.listarTodosActivos();
        } else {
            puestosEncontrados = new ArrayList<>();
            for (Long id : ids) {
                List<Puesto> porCarac = puestoService.listarActivosPorCaracteristica(id);
                for (Puesto p : porCarac) {
                    if (!puestosEncontrados.contains(p)) {
                        puestosEncontrados.add(p);
                    }
                }
            }
        }

        if (principal == null) {
            puestosEncontrados = puestosEncontrados.stream()
                    .filter(p -> "PUBLICO".equalsIgnoreCase(p.getTipo()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("puestos", puestosEncontrados);
        return "puestos/buscar";
    }
}