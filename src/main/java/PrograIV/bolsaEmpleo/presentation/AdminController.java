package PrograIV.bolsaEmpleo.presentation;

import PrograIV.bolsaEmpleo.Logic.Caracteristica;
import PrograIV.bolsaEmpleo.Logic.Empresa;
import PrograIV.bolsaEmpleo.Logic.Oferente;
import PrograIV.bolsaEmpleo.Logic.Puesto;
import PrograIV.bolsaEmpleo.data.CaracteristicaRepository;
import PrograIV.bolsaEmpleo.data.EmpresaRepository;
import PrograIV.bolsaEmpleo.data.OferenteRepository;
import PrograIV.bolsaEmpleo.data.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class AdminController {

    @Autowired
    private EmpresaRepository empresaRepository;


    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    // ---- PANTALLA PRINCIPAL DEL ADMINISTRADOR (DASHBOARD) ----
    @GetMapping("/admin/dashboard")
    public String verDashboard() {
        return "admin/dashboard"; // Llamará a nuestro nuevo HTML
    }


    // 1. Mostrar la pantalla con la tabla de empresas pendientes
    @GetMapping("/admin/empresas-pendientes")
    public String verEmpresasPendientes(Model model) {
        // Buscamos solo las que tienen estado 0
        List<Empresa> pendientes = empresaRepository.findByEstado(0);
        model.addAttribute("empresas", pendientes);
        return "admin/empresas-pendientes"; // Apunta al HTML que vamos a crear
    }

    // 2. Acción que se ejecuta al darle clic al botón "Aprobar"
    @PostMapping("/admin/aprobar-empresa")
    public String aprobarEmpresa(@RequestParam String email) {
        // Buscamos la empresa por su correo
        Empresa empresa = empresaRepository.findById(email).orElse(null);

        if (empresa != null) {
            empresa.setEstado(1); // ¡Le cambiamos el estado a 1 (Aprobado)!
            empresaRepository.save(empresa); // Guardamos el cambio en la base de datos
        }

        // Recargamos la misma página para que la empresa desaparezca de la lista
        return "redirect:/admin/empresas-pendientes";
    }

    // 1. Mostrar la pantalla con la tabla de oferentes pendientes
    @GetMapping("/admin/oferentes-pendientes")
    public String verOferentesPendientes(Model model) {
        // Buscamos solo los que tienen estado 0
        List<Oferente> pendientes = oferenteRepository.findByEstado(0);
        model.addAttribute("oferentes", pendientes);
        return "admin/oferentes-pendientes"; // Apunta al HTML que vamos a crear
    }

    // 2. Acción que se ejecuta al darle clic al botón "Aprobar"
    @PostMapping("/admin/aprobar-oferente")
    public String aprobarOferente(@RequestParam String email) {
        // Buscamos el oferente por su correo
        Oferente oferente = oferenteRepository.findById(email).orElse(null);

        if (oferente != null) {
            oferente.setEstado(1); // ¡Le cambiamos el estado a 1 (Aprobado)!
            oferenteRepository.save(oferente); // Guardamos el cambio en la base de datos
        }

        // Recargamos la misma página para que la empresa desaparezca de la lista
        return "redirect:/admin/oferentes-pendientes";
    }

    // ---- 1. MOSTRAR EL CATÁLOGO DE CARACTERÍSTICAS ----
    @GetMapping("/admin/caracteristicas")
    public String verCaracteristicas(@RequestParam(required = false) Long padreId, Model model) {
        List<Caracteristica> lista;
        Caracteristica padreActual = null;

        // Si no hay padreId en la URL, mostramos las raíces
        if (padreId == null) {
            lista = caracteristicaRepository.findByPadreIsNull();
        } else {
            // Si hay padreId, buscamos al padre y luego mostramos a sus hijos
            padreActual = caracteristicaRepository.findById(padreId).orElse(null);
            lista = caracteristicaRepository.findByPadreId(padreId);
        }

        // Enviamos la información a la vista HTML
        model.addAttribute("caracteristicas", lista);
        model.addAttribute("padreActual", padreActual);

        return "admin/caracteristicas";
    }

    // ---- 2. GUARDAR UNA NUEVA CARACTERÍSTICA ----
    @PostMapping("/admin/caracteristicas/guardar")
    public String guardarCaracteristica(@RequestParam String nombre, @RequestParam(required = false) Long padreId) {
        Caracteristica nueva = new Caracteristica();
        nueva.setNombre(nombre);

        // Si venía un padreId desde el formulario, significa que es una subcategoría
        if (padreId != null) {
            Caracteristica padre = caracteristicaRepository.findById(padreId).orElse(null);
            nueva.setPadre(padre);
        }

        caracteristicaRepository.save(nueva); // Guardamos en BD

        // Recargamos la misma página en la que estábamos (Raíz o Subcategoría)
        if (padreId != null) {
            return "redirect:/admin/caracteristicas?padreId=" + padreId;
        }
        return "redirect:/admin/caracteristicas";
    }

    // ---- 1. MOSTRAR LA PANTALLA DE REPORTES ----
    @GetMapping("/admin/reportes")
    public String verReportes() {
        return "admin/reportes";
    }

    // ---- 2. GENERAR Y DESCARGAR EL PDF ----
    @GetMapping("/admin/reportes/puestos-pdf")
    public void descargarReportePdf(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        // 1. Configurar la respuesta web para que el navegador sepa que es un PDF descargable
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Puestos_Mensual.pdf");

        // 2. Traer todos los puestos de la base de datos
        List<Puesto> puestos = puestoRepository.findAll();

        // 3. Crear el documento PDF (usando OpenPDF)
        com.lowagie.text.Document documento = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
        com.lowagie.text.pdf.PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        // 4. Diseñar el Título
        com.lowagie.text.Font fuenteTitulo = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setSize(18);
        com.lowagie.text.Paragraph titulo = new com.lowagie.text.Paragraph("Reporte de Puestos Solicitados", fuenteTitulo);
        titulo.setAlignment(com.lowagie.text.Paragraph.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        // 5. Dibujar la Tabla
        com.lowagie.text.pdf.PdfPTable tabla = new com.lowagie.text.pdf.PdfPTable(4); // 4 Columnas
        tabla.setWidthPercentage(100f);

        // Encabezados
        tabla.addCell("Fecha");
        tabla.addCell("Empresa");
        tabla.addCell("Descripción del Puesto");
        tabla.addCell("Salario (₡)");

        // Llenar la tabla con los datos
        for (Puesto puesto : puestos) {
            // Si el puesto es viejo y no tenía fecha, le ponemos una por defecto para que no explote
            String fecha = (puesto.getFechaCreacion() != null) ? puesto.getFechaCreacion().toString() : "N/A";
            tabla.addCell(fecha);

            tabla.addCell(puesto.getEmpresa().getNombre());
            tabla.addCell(puesto.getDescripcion());
            tabla.addCell(String.valueOf(puesto.getSalario()));
        }

        documento.add(tabla);
        documento.close();
    }

}