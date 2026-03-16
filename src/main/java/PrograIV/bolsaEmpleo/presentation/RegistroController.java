package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.EmpresaService;
import PrograIV.bolsaEmpleo.Logic.Oferente;
import PrograIV.bolsaEmpleo.Logic.OferenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OferenteService oferenteService;

    // ---- EMPRESA ----

    @GetMapping("/registro/empresa")
    public String formRegistroEmpresa(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "registro/empresa";
    }

    @PostMapping("/registro/empresa")
    public String registrarEmpresa(@ModelAttribute Empresa empresa, Model model) {
        try {
            empresaService.registrar(empresa);
            model.addAttribute("mensaje",
                    "Registro exitoso. Espere la aprobación del administrador.");
            return "registro/confirmacion";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro/empresa";
        }
    }

    // ---- OFERENTE ----

    @GetMapping("/registro/oferente")
    public String formRegistroOferente(Model model) {
        model.addAttribute("oferente", new Oferente());
        return "registro/oferente";
    }

    @PostMapping("/registro/oferente")
    public String registrarOferente(@ModelAttribute Oferente oferente, Model model) {
        try {
            oferenteService.registrar(oferente);
            model.addAttribute("mensaje",
                    "Registro exitoso. Espere la aprobación del administrador.");
            return "registro/confirmacion";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro/oferente";
        }
    }
}
