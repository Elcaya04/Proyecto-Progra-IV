package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.PuestoService;
import PrograIV.bolsaEmpleo.data.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PuestoService puestoService;

    @Autowired
    private PrograIV.bolsaEmpleo.data.CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.PuestoRepository puestoRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.OferenteRepository oferenteRepository;

    @Autowired
    private PrograIV.bolsaEmpleo.data.OferenteCaracteristicaRepository oferenteCaracteristicaRepository;


    private Empresa obtenerEmpresaLogueada(Principal principal) {

        return empresaRepository.findById(principal.getName()).orElse(null);
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


        model.addAttribute("caracteristicas", caracteristicaRepository.findAll());

        return "empresa/nuevo-puesto";
    }


    @PostMapping("/puestos/guardar")
    public String guardarPuesto(
            @RequestParam String descripcion,
            @RequestParam Double salario,
            @RequestParam String tipo,
            @RequestParam(required = false) java.util.List<Long> caracteristicasIds,
            Principal principal) {


        Empresa empresa = obtenerEmpresaLogueada(principal);


        PrograIV.bolsaEmpleo.Logic.Puesto nuevoPuesto = new PrograIV.bolsaEmpleo.Logic.Puesto();
        nuevoPuesto.setDescripcion(descripcion);
        nuevoPuesto.setSalario(salario);
        nuevoPuesto.setTipo(tipo);
        nuevoPuesto.setEmpresa(empresa);
        nuevoPuesto.setActivo(true);



        nuevoPuesto = puestoRepository.save(nuevoPuesto);


        if (caracteristicasIds != null) {
            for (Long caracId : caracteristicasIds) {

                PrograIV.bolsaEmpleo.Logic.Caracteristica carac = caracteristicaRepository.findById(caracId).orElse(null);

                if (carac != null) {

                    PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica pc = new PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica();
                    pc.setPuesto(nuevoPuesto);
                    pc.setCaracteristica(carac);
                    pc.setNivelRequerido(3);

                    puestoCaracteristicaRepository.save(pc);
                }
            }
        }


        return "redirect:/empresa/puestos";
    }


    @GetMapping("/puestos/{id}/candidatos")
    public String buscarCandidatos(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, Principal principal) {


        PrograIV.bolsaEmpleo.Logic.Puesto puesto = puestoService.buscarPorId(id);
        java.util.List<PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica> requisitosPuesto = puestoService.listarRequisitos(id);
        int totalRequisitos = requisitosPuesto.size();


        java.util.List<PrograIV.bolsaEmpleo.Logic.Oferente> todosOferentes = oferenteRepository.findByEstado(1);


        java.util.List<PrograIV.bolsaEmpleo.Logic.CandidatoMatch> listaCandidatos = new java.util.ArrayList<>();


        if (totalRequisitos > 0) {
            for (PrograIV.bolsaEmpleo.Logic.Oferente oferente : todosOferentes) {

                java.util.List<PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica> habsOferente =
                        oferenteCaracteristicaRepository.findByOferenteEmail(oferente.getEmail());

                int coincidencias = 0;


                for (PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica req : requisitosPuesto) {
                    for (PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica hab : habsOferente) {

                        if (req.getCaracteristica().getId().equals(hab.getCaracteristica().getId())) {
                            coincidencias++;
                            break;
                        }
                    }
                }


                if (coincidencias > 0) {
                    double porcentaje = ((double) coincidencias / totalRequisitos) * 100.0;
                    listaCandidatos.add(new PrograIV.bolsaEmpleo.Logic.CandidatoMatch(oferente, coincidencias, totalRequisitos, porcentaje));
                }
            }


            listaCandidatos.sort((c1, c2) -> Double.compare(c2.getPorcentajeCoincidencia(), c1.getPorcentajeCoincidencia()));
        }


        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", listaCandidatos);

        return "empresa/candidatos";
    }


    @GetMapping("/oferente/{email}")
    public String verDetalleOferente(@org.springframework.web.bind.annotation.PathVariable String email, Model model) {


        PrograIV.bolsaEmpleo.Logic.Oferente oferente = oferenteRepository.findById(email).orElse(null);


        java.util.List<PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica> habilidades =
                oferenteCaracteristicaRepository.findByOferenteEmail(email);

        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);

        return "empresa/detalle-oferente";
    }
    @PostMapping("/puestos/desactivar")
    public String desactivarPuesto(@RequestParam Long id) {
        puestoService.desactivar(id);
        return "redirect:/empresa/puestos";
    }
}