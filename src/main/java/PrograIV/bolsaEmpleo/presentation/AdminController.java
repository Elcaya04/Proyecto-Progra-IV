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


    @GetMapping("/admin/dashboard")
    public String verDashboard() {
        return "admin/dashboard"; }



    @GetMapping("/admin/empresas-pendientes")
    public String verEmpresasPendientes(Model model) {

        List<Empresa> pendientes = empresaRepository.findByEstado(0);
        model.addAttribute("empresas", pendientes);
        return "admin/empresas-pendientes";
    }


    @PostMapping("/admin/aprobar-empresa")
    public String aprobarEmpresa(@RequestParam String email) {

        Empresa empresa = empresaRepository.findById(email).orElse(null);

        if (empresa != null) {
            empresa.setEstado(1);
            empresaRepository.save(empresa);
        }


        return "redirect:/admin/empresas-pendientes";
    }


    @GetMapping("/admin/oferentes-pendientes")
    public String verOferentesPendientes(Model model) {

        List<Oferente> pendientes = oferenteRepository.findByEstado(0);
        model.addAttribute("oferentes", pendientes);
        return "admin/oferentes-pendientes";
    }


    @PostMapping("/admin/aprobar-oferente")
    public String aprobarOferente(@RequestParam String email) {

        Oferente oferente = oferenteRepository.findById(email).orElse(null);

        if (oferente != null) {
            oferente.setEstado(1);
            oferenteRepository.save(oferente);
        }


        return "redirect:/admin/oferentes-pendientes";
    }


    @GetMapping("/admin/caracteristicas")
    public String verCaracteristicas(@RequestParam(required = false) Long padreId, Model model) {
        List<Caracteristica> lista;
        Caracteristica padreActual = null;


        if (padreId == null) {
            lista = caracteristicaRepository.findByPadreIsNull();
        } else {

            padreActual = caracteristicaRepository.findById(padreId).orElse(null);
            lista = caracteristicaRepository.findByPadreId(padreId);
        }


        model.addAttribute("caracteristicas", lista);
        model.addAttribute("padreActual", padreActual);

        return "admin/caracteristicas";
    }


    @PostMapping("/admin/caracteristicas/guardar")
    public String guardarCaracteristica(@RequestParam String nombre, @RequestParam(required = false) Long padreId) {
        Caracteristica nueva = new Caracteristica();
        nueva.setNombre(nombre);


        if (padreId != null) {
            Caracteristica padre = caracteristicaRepository.findById(padreId).orElse(null);
            nueva.setPadre(padre);
        }

        caracteristicaRepository.save(nueva);


        if (padreId != null) {
            return "redirect:/admin/caracteristicas?padreId=" + padreId;
        }
        return "redirect:/admin/caracteristicas";
    }


    @GetMapping("/admin/reportes")
    public String verReportes() {
        return "admin/reportes";
    }


    @GetMapping("/admin/reportes/puestos-pdf")
    public void descargarReportePdf(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Puestos_Mensual.pdf");


        List<Puesto> puestos = puestoRepository.findAll();


        com.lowagie.text.Document documento = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
        com.lowagie.text.pdf.PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();


        com.lowagie.text.Font fuenteTitulo = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setSize(18);
        com.lowagie.text.Paragraph titulo = new com.lowagie.text.Paragraph("Reporte de Puestos Solicitados", fuenteTitulo);
        titulo.setAlignment(com.lowagie.text.Paragraph.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);


        com.lowagie.text.pdf.PdfPTable tabla = new com.lowagie.text.pdf.PdfPTable(4); // 4 Columnas
        tabla.setWidthPercentage(100f);


        tabla.addCell("Fecha");
        tabla.addCell("Empresa");
        tabla.addCell("Descripción del Puesto");
        tabla.addCell("Salario (₡)");


        for (Puesto puesto : puestos) {

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