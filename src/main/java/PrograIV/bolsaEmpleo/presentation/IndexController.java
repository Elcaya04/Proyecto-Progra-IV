package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.PuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private PuestoService puestoService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("puestos", puestoService.listarUltimosPublicos());
        return "index";
    }

}