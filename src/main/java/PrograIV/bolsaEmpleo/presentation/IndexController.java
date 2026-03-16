package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.PuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private PuestoService puestoService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("puestos", puestoService.listarUltimosPublicos());
        return "index";
    }

    @GetMapping("/puestos/buscar-por-caracteristicas")
    public String buscarPuestos(
            @RequestParam(required = false) List<Integer> caracteristicas,
            Model model) {
        model.addAttribute("todasCaracteristicas",
                puestoService.buscarPorCaracteristicas(caracteristicas));
        model.addAttribute("caracteristicasSeleccionadas", caracteristicas);
        return "buscarPuestos";
    }
}