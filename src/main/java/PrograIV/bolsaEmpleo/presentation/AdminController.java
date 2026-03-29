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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PuestoRepository puestoRepository;


    @GetMapping("/dashboard")
    public String verDashboard() {
        return "admin/dashboard";
    }


    @GetMapping("/empresas-pendientes")
    public String verEmpresasPendientes(Model model) {
        List<Empresa> pendientes = empresaRepository.findByEstado(0);
        model.addAttribute("empresas", pendientes);
        return "admin/empresas-pendientes";
    }

    @PostMapping("/aprobar-empresa")
    public String aprobarEmpresa(@RequestParam String email) {
        Empresa empresa = empresaRepository.findById(email).orElse(null);
        if (empresa != null) {
            empresa.setEstado(1);
            empresaRepository.save(empresa);
        }
        return "redirect:/admin/empresas-pendientes";
    }


    @GetMapping("/oferentes-pendientes")
    public String verOferentesPendientes(Model model) {
        List<Oferente> pendientes = oferenteRepository.findByEstado(0);
        model.addAttribute("oferentes", pendientes);
        return "admin/oferentes-pendientes";
    }

    @PostMapping("/aprobar-oferente")
    public String aprobarOferente(@RequestParam String email) {
        Oferente oferente = oferenteRepository.findById(email).orElse(null);
        if (oferente != null) {
            oferente.setEstado(1);
            oferenteRepository.save(oferente);
        }
        return "redirect:/admin/oferentes-pendientes";
    }


    @GetMapping("/caracteristicas")
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

    @PostMapping("/caracteristicas/guardar")
    public String guardarCaracteristica(@RequestParam String nombre,
                                        @RequestParam(required = false) Long padreId) {
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


    @GetMapping("/reportes")
    public String verReportes(Model model) {
        // Enviamos los años disponibles para el selector del formulario
        List<Integer> aniosDisponibles = puestoRepository.findAll()
                .stream()
                .filter(p -> p.getFechaCreacion() != null)
                .map(p -> p.getFechaCreacion().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("anios", aniosDisponibles);
        return "admin/reportes";
    }


    @GetMapping("/reportes/puestos-pdf")
    public void descargarReportePdf(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio,
            jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {

        response.setContentType("application/pdf");
        String nombreArchivo = (mes != null && anio != null)
                ? "Reporte_Puestos_" + anio + "_Mes" + mes + ".pdf"
                : "Reporte_Puestos_Completo.pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);


        List<Puesto> todos = puestoRepository.findAll();
        List<Puesto> puestos = todos.stream()
                .filter(p -> p.getFechaCreacion() != null)
                .filter(p -> mes == null || p.getFechaCreacion().getMonthValue() == mes)
                .filter(p -> anio == null || p.getFechaCreacion().getYear() == anio)
                .sorted(java.util.Comparator.comparing(Puesto::getFechaCreacion))
                .collect(Collectors.toList());


        com.lowagie.text.Document documento =
                new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
        com.lowagie.text.pdf.PdfWriter.getInstance(documento, response.getOutputStream());
        documento.open();


        com.lowagie.text.Font fuenteTitulo =
                com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setSize(18);

        com.lowagie.text.Font fuenteSubtitulo =
                com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fuenteSubtitulo.setSize(13);
        fuenteSubtitulo.setColor(java.awt.Color.decode("#003366"));

        com.lowagie.text.Font fuenteNormal =
                com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA);
        fuenteNormal.setSize(10);


        String tituloPdf = (mes != null && anio != null)
                ? "Reporte de Puestos – " + obtenerNombreMes(mes) + " " + anio
                : "Reporte de Puestos – Todos los Registros";

        com.lowagie.text.Paragraph titulo =
                new com.lowagie.text.Paragraph(tituloPdf, fuenteTitulo);
        titulo.setAlignment(com.lowagie.text.Paragraph.ALIGN_CENTER);
        titulo.setSpacingAfter(6);
        documento.add(titulo);

        com.lowagie.text.Paragraph generado =
                new com.lowagie.text.Paragraph(
                        "Generado el: " + java.time.LocalDate.now(), fuenteNormal);
        generado.setAlignment(com.lowagie.text.Paragraph.ALIGN_CENTER);
        generado.setSpacingAfter(20);
        documento.add(generado);

        if (puestos.isEmpty()) {
            documento.add(new com.lowagie.text.Paragraph(
                    "No se encontraron puestos para el período seleccionado.", fuenteNormal));
        } else {

            String mesAnioActual = "";

            com.lowagie.text.pdf.PdfPTable tabla = null;

            for (Puesto puesto : puestos) {
                String mesAnio = obtenerNombreMes(puesto.getFechaCreacion().getMonthValue())
                        + " " + puesto.getFechaCreacion().getYear();


                if (!mesAnio.equals(mesAnioActual)) {

                    if (tabla != null) {
                        documento.add(tabla);
                        documento.add(new com.lowagie.text.Paragraph(" "));
                    }


                    com.lowagie.text.Paragraph encabezadoMes =
                            new com.lowagie.text.Paragraph(mesAnio, fuenteSubtitulo);
                    encabezadoMes.setSpacingBefore(10);
                    encabezadoMes.setSpacingAfter(4);
                    documento.add(encabezadoMes);


                    tabla = new com.lowagie.text.pdf.PdfPTable(4);
                    tabla.setWidthPercentage(100f);
                    tabla.setWidths(new float[]{2f, 3f, 4f, 2f});


                    agregarCeldaEncabezado(tabla, "Fecha");
                    agregarCeldaEncabezado(tabla, "Empresa");
                    agregarCeldaEncabezado(tabla, "Descripción");
                    agregarCeldaEncabezado(tabla, "Salario (₡)");

                    mesAnioActual = mesAnio;
                }

                tabla.addCell(puesto.getFechaCreacion().toString());
                tabla.addCell(puesto.getEmpresa().getNombre());
                tabla.addCell(puesto.getDescripcion());
                tabla.addCell(String.valueOf(puesto.getSalario()));
            }

            if (tabla != null) {
                documento.add(tabla);
            }
        }

        documento.close();
    }



    private void agregarCeldaEncabezado(com.lowagie.text.pdf.PdfPTable tabla, String texto) {
        com.lowagie.text.pdf.PdfPCell celda = new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Phrase(texto,
                        com.lowagie.text.FontFactory.getFont(
                                com.lowagie.text.FontFactory.HELVETICA_BOLD, 10,
                                java.awt.Color.WHITE)));
        celda.setBackgroundColor(java.awt.Color.decode("#003366"));
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private String obtenerNombreMes(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes >= 1 && mes <= 12) ? meses[mes - 1] : "Mes " + mes;
    }
}
