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

    // Método auxiliar para saber qué empresa está logueada
    private Empresa obtenerEmpresaLogueada(Principal principal) {
        // Principal contiene el email del usuario logueado gracias a Spring Security
        return empresaRepository.findById(principal.getName()).orElse(null);
    }

    // ---- VENTANA 1: DASHBOARD ----
    @GetMapping("/dashboard")
    public String verDashboard(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);
        return "empresa/dashboard";
    }

    // ---- VENTANA 2: MIS PUESTOS ----
    @GetMapping("/puestos")
    public String verMisPuestos(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);
        // Traemos solo los puestos de esta empresa
        model.addAttribute("puestos", puestoService.listarPorEmpresa(empresa.getEmail()));
        return "empresa/puestos";
    }

    // ---- VENTANA 2.1: FORMULARIO PARA CREAR PUESTO ----
    @GetMapping("/puestos/nuevo")
    public String nuevoPuesto(Model model, Principal principal) {
        Empresa empresa = obtenerEmpresaLogueada(principal);
        model.addAttribute("empresa", empresa);

        // Pasamos todas las características a la vista para que la empresa pueda elegirlas
        model.addAttribute("caracteristicas", caracteristicaRepository.findAll());

        return "empresa/nuevo-puesto";
    }

    // ---- VENTANA 2.2: GUARDAR EL PUESTO EN LA BASE DE DATOS ----
    @PostMapping("/puestos/guardar")
    public String guardarPuesto(
            @RequestParam String descripcion,
            @RequestParam Double salario,
            @RequestParam String tipo,
            @RequestParam(required = false) java.util.List<Long> caracteristicasIds,
            Principal principal) {

        // 1. Obtenemos la empresa que está logueada
        Empresa empresa = obtenerEmpresaLogueada(principal);

        // 2. Creamos el nuevo Puesto con los datos del formulario
        PrograIV.bolsaEmpleo.Logic.Puesto nuevoPuesto = new PrograIV.bolsaEmpleo.Logic.Puesto();
        nuevoPuesto.setDescripcion(descripcion);
        nuevoPuesto.setSalario(salario);
        nuevoPuesto.setTipo(tipo);
        nuevoPuesto.setEmpresa(empresa);
        nuevoPuesto.setActivo(true);
        // (La fechaCreacion se pone sola gracias al cambio que hicimos antes)

        // 3. ¡Guardamos el puesto en la base de datos!
        // Al guardarlo, Spring le asigna un ID automáticamente
        nuevoPuesto = puestoRepository.save(nuevoPuesto);

        // 4. Guardamos las características (habilidades) seleccionadas
        if (caracteristicasIds != null) {
            for (Long caracId : caracteristicasIds) {
                // Buscamos la característica en la base de datos
                PrograIV.bolsaEmpleo.Logic.Caracteristica carac = caracteristicaRepository.findById(caracId).orElse(null);

                if (carac != null) {
                    // Creamos la relación entre el Puesto y esta Característica
                    PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica pc = new PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica();
                    pc.setPuesto(nuevoPuesto);
                    pc.setCaracteristica(carac);
                    pc.setNivelRequerido(3); // Por defecto le ponemos nivel 3 (Intermedio)

                    puestoCaracteristicaRepository.save(pc);
                }
            }
        }

        // 5. Redirigimos a la pantalla de "Mis Puestos" para que la empresa vea su creación
        return "redirect:/empresa/puestos";
    }

    // ---- VENTANA 3: BUSCAR CANDIDATOS (EL MATCH) ----
    @GetMapping("/puestos/{id}/candidatos")
    public String buscarCandidatos(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, Principal principal) {

        // 1. Buscamos el puesto y sus requisitos
        PrograIV.bolsaEmpleo.Logic.Puesto puesto = puestoService.buscarPorId(id);
        java.util.List<PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica> requisitosPuesto = puestoService.listarRequisitos(id);
        int totalRequisitos = requisitosPuesto.size();

        // 2. Traemos a todos los oferentes activos del sistema
        java.util.List<PrograIV.bolsaEmpleo.Logic.Oferente> todosOferentes = oferenteRepository.findByEstado(1);

        // Aquí guardaremos a los que hagan match
        java.util.List<PrograIV.bolsaEmpleo.Logic.CandidatoMatch> listaCandidatos = new java.util.ArrayList<>();

        // 3. Algoritmo de Coincidencia (Match)
        if (totalRequisitos > 0) {
            for (PrograIV.bolsaEmpleo.Logic.Oferente oferente : todosOferentes) {
                // Buscamos qué habilidades tiene este oferente
                java.util.List<PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica> habsOferente =
                        oferenteCaracteristicaRepository.findByOferenteEmail(oferente.getEmail());

                int coincidencias = 0;

                // Comparamos los requisitos del puesto con las habilidades del oferente
                for (PrograIV.bolsaEmpleo.Logic.PuestoCaracteristica req : requisitosPuesto) {
                    for (PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica hab : habsOferente) {
                        // Si tienen la misma característica, es un Match!
                        if (req.getCaracteristica().getId().equals(hab.getCaracteristica().getId())) {
                            coincidencias++;
                            break; // Pasamos al siguiente requisito
                        }
                    }
                }

                // Si tiene al menos 1 coincidencia, lo agregamos a la lista
                if (coincidencias > 0) {
                    double porcentaje = ((double) coincidencias / totalRequisitos) * 100.0;
                    listaCandidatos.add(new PrograIV.bolsaEmpleo.Logic.CandidatoMatch(oferente, coincidencias, totalRequisitos, porcentaje));
                }
            }

            // 4. Ordenamos la lista para que los de 100% salgan de primeros
            listaCandidatos.sort((c1, c2) -> Double.compare(c2.getPorcentajeCoincidencia(), c1.getPorcentajeCoincidencia()));
        }

        // Enviamos los datos al HTML
        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", listaCandidatos);

        return "empresa/candidatos";
    }

    // ---- VENTANA 4: DETALLE DEL OFERENTE ----
    @GetMapping("/oferente/{email}")
    public String verDetalleOferente(@org.springframework.web.bind.annotation.PathVariable String email, Model model) {

        // 1. Buscamos la información personal del oferente
        PrograIV.bolsaEmpleo.Logic.Oferente oferente = oferenteRepository.findById(email).orElse(null);

        // 2. Buscamos la lista de habilidades que él registró
        java.util.List<PrograIV.bolsaEmpleo.Logic.OferenteCaracteristica> habilidades =
                oferenteCaracteristicaRepository.findByOferenteEmail(email);

        // 3. Lo mandamos a la pantalla
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);

        return "empresa/detalle-oferente";
    }

}