package beans;

import dao.TrasladoRecursoDAO;
import dao.RecursoDAO;
import dao.AmbienteDAO;
import modelo.TrasladoRecurso;
import modelo.Recurso;
import modelo.Ambiente;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Managed Bean para gestionar traslados de recursos entre ambientes
 */
@ManagedBean(name = "trasladoRecursoBean")
@ViewScoped
public class TrasladoRecursoBean implements Serializable {

    private final TrasladoRecursoDAO trasladoDAO = new TrasladoRecursoDAO();
    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    private TrasladoRecurso traslado = new TrasladoRecurso();
    private List<TrasladoRecurso> traslados;
    private List<Recurso> recursos;
    private List<Ambiente> ambientes;
    
    private Date fechaDate;
    private Integer idTrasladoEditar;

    @PostConstruct
    public void init() {
        System.out.println("🔍 TrasladoRecursoBean.init: Inicializando bean");
        cargarDatos();
        prepararNuevo();
    }

    private void cargarDatos() {
        traslados = trasladoDAO.listar();
        recursos = recursoDAO.listar();
        ambientes = ambienteDAO.listar();
        System.out.println("   - Traslados cargados: " + (traslados != null ? traslados.size() : 0));
        System.out.println("   - Recursos cargados: " + (recursos != null ? recursos.size() : 0));
        System.out.println("   - Ambientes cargados: " + (ambientes != null ? ambientes.size() : 0));
    }

    public void prepararNuevo() {
        System.out.println("🔍 TrasladoRecursoBean.prepararNuevo: Preparando nuevo traslado");
        traslado = new TrasladoRecurso();
        traslado.setFechaTraslado(new Date());
        fechaDate = new Date();
        idTrasladoEditar = null;
    }

    /**
     * Actualiza el ambiente de origen cuando se selecciona un recurso
     */
    public void onRecursoChange() {
        System.out.println("🔍 TrasladoRecursoBean.onRecursoChange: Recurso seleccionado: " + traslado.getRecursoId());
        if (traslado.getRecursoId() > 0) {
            int ambienteActual = trasladoDAO.obtenerAmbienteActualRecurso(traslado.getRecursoId());
            traslado.setAmbienteOrigenId(ambienteActual);
            System.out.println("   - Ambiente origen establecido: " + ambienteActual);
        }
    }

    public String guardar() {
        System.out.println("🔍 TrasladoRecursoBean.guardar: Iniciando guardado");
        System.out.println("   - Recurso ID: " + traslado.getRecursoId());
        System.out.println("   - Ambiente Origen: " + traslado.getAmbienteOrigenId());
        System.out.println("   - Ambiente Destino: " + traslado.getAmbienteDestinoId());

        // Validaciones
        if (traslado.getRecursoId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un recurso.");
            return null;
        }

        if (traslado.getAmbienteDestinoId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un ambiente de destino.");
            return null;
        }

        if (traslado.getAmbienteOrigenId() == traslado.getAmbienteDestinoId()) {
            FacesUtils.addErrorMessage("El ambiente de destino debe ser diferente al de origen.");
            return null;
        }

        // Establecer fecha
        if (fechaDate != null) {
            traslado.setFechaTraslado(new Date());
        }

        int idGenerado = trasladoDAO.guardar(traslado);
        if (idGenerado > 0) {
            System.out.println("✅ TrasladoRecursoBean.guardar: Traslado guardado con ID: " + idGenerado);
            FacesUtils.addInfoMessage("Traslado registrado correctamente.");
            cargarDatos();
            prepararNuevo();
            return null;
        } else {
            System.err.println("❌ TrasladoRecursoBean.guardar: Error al guardar");
            FacesUtils.addErrorMessage("No fue posible registrar el traslado.");
            return null;
        }
    }

    public void editar(int id) {
        System.out.println("🔍 TrasladoRecursoBean.editar: ID: " + id);
        TrasladoRecurso encontrado = trasladoDAO.buscarPorId(id);
        if (encontrado != null) {
            traslado = encontrado;
            idTrasladoEditar = id;
        }
    }

    public void eliminar(int id) {
        System.out.println("🔍 TrasladoRecursoBean.eliminar: ID: " + id);
        if (trasladoDAO.eliminar(id)) {
            FacesUtils.addInfoMessage("Traslado eliminado correctamente.");
            cargarDatos();
        } else {
            FacesUtils.addErrorMessage("No fue posible eliminar el traslado.");
        }
    }

    // Métodos auxiliares para formatear
    public String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "-";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fecha);
    }

    public String getNombreAmbiente(int idAmbiente) {
        for (Ambiente a : getAmbientes()) {
            if (a.getIdAmbiente() == idAmbiente) {
                return "Ambiente " + a.getNumero() + " - " + a.getTipo();
            }
        }
        return "Ambiente " + idAmbiente;
    }

    public String getNombreRecurso(int idRecurso) {
        for (Recurso r : getRecursos()) {
            if (r.getIdRecurso() == idRecurso) {
                return r.getNombre() + " (Serial: " + r.getSerial() + ")";
            }
        }
        return "Recurso " + idRecurso;
    }

    // Getters y Setters
    public TrasladoRecurso getTraslado() {
        return traslado;
    }

    public void setTraslado(TrasladoRecurso traslado) {
        this.traslado = traslado;
    }

    public List<TrasladoRecurso> getTraslados() {
        return traslados;
    }

    public List<Recurso> getRecursos() {
        if (recursos == null) {
            recursos = recursoDAO.listar();
        }
        return recursos;
    }

    public List<Ambiente> getAmbientes() {
        if (ambientes == null) {
            ambientes = ambienteDAO.listar();
        }
        return ambientes;
    }

    public Date getFechaDate() {
        return fechaDate;
    }

    public void setFechaDate(Date fechaDate) {
        this.fechaDate = fechaDate;
    }

    public Integer getIdTrasladoEditar() {
        return idTrasladoEditar;
    }

    public void setIdTrasladoEditar(Integer idTrasladoEditar) {
        this.idTrasladoEditar = idTrasladoEditar;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}

