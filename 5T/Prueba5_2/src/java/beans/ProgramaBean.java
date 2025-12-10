package beans;

import dao.ProgramaDAO;
import dao.JornadaDAO;
import dao.ModalidadDAO;
import dao.CoordinacionDAO;
import modelo.Programa;
import modelo.Jornada;
import modelo.Modalidad;
import modelo.Coordinacion;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "programaBean")
@ViewScoped
public class ProgramaBean implements Serializable {

    private final ProgramaDAO programaDAO = new ProgramaDAO();
    private final JornadaDAO jornadaDAO = new JornadaDAO();
    private final ModalidadDAO modalidadDAO = new ModalidadDAO();
    private final CoordinacionDAO coordinacionDAO = new CoordinacionDAO();

    private Programa programa = new Programa();
    private Integer idProgramaEditar;
    
    private boolean inicializado = false;

    private List<Programa> programas;
    private List<Jornada> jornadas;
    private List<Modalidad> modalidades;
    private List<Coordinacion> coordinaciones;

    public ProgramaBean() {
        System.out.println("🔍 ProgramaBean: Constructor llamado");
    }

    private void cargarDatos() {
        try {
            System.out.println("🔍 ProgramaBean.cargarDatos: Iniciando carga de datos");
            
            programas = programaDAO.listar();
            System.out.println("   - Programas cargados: " + (programas != null ? programas.size() : 0));
            
            jornadas = jornadaDAO.listar();
            System.out.println("   - Jornadas cargadas: " + (jornadas != null ? jornadas.size() : 0));
            if (jornadas != null && !jornadas.isEmpty()) {
                for (Jornada j : jornadas) {
                    System.out.println("      - Jornada: " + j.getNombreJornada() + " (ID: " + j.getIdJornada() + ")");
                }
            } else {
                System.err.println("   ⚠️ No se encontraron jornadas en la base de datos");
            }
            
            modalidades = modalidadDAO.listar();
            System.out.println("   - Modalidades cargadas: " + (modalidades != null ? modalidades.size() : 0));
            if (modalidades != null && !modalidades.isEmpty()) {
                for (Modalidad m : modalidades) {
                    System.out.println("      - Modalidad: " + m.getNombreModalidad() + " (ID: " + m.getIdModalidad() + ")");
                }
            } else {
                System.err.println("   ⚠️ No se encontraron modalidades en la base de datos");
            }
            
            coordinaciones = coordinacionDAO.listar();
            System.out.println("   - Coordinaciones cargadas: " + (coordinaciones != null ? coordinaciones.size() : 0));
            if (coordinaciones != null && !coordinaciones.isEmpty()) {
                for (Coordinacion c : coordinaciones) {
                    System.out.println("      - Coordinación: " + c.getNombreCoordinacion() + " (ID: " + c.getIdCoordinacion() + ")");
                }
            } else {
                System.err.println("   ⚠️ No se encontraron coordinaciones en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("❌ ProgramaBean.cargarDatos: Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("🔍 ProgramaBean.init: @PostConstruct ejecutado");
        System.out.println("   - idProgramaEditar recibido: " + idProgramaEditar);
        
        // Si no hay idProgramaEditar, intentar obtenerlo de la URL manualmente
        if (idProgramaEditar == null || idProgramaEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("idProgramaEditar");
                    if (idParam != null && !idParam.isEmpty()) {
                        idProgramaEditar = Integer.parseInt(idParam);
                        System.out.println("   - idProgramaEditar obtenido de parámetro URL: " + idProgramaEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ ProgramaBean.init: Error al obtener idProgramaEditar de URL: " + e.getMessage());
            }
        }
        
        // Cargar datos siempre
        cargarDatos();
        inicializado = true;
        
        // Si hay idProgramaEditar, cargar el programa para editar
        if (idProgramaEditar != null && idProgramaEditar > 0) {
            System.out.println("   - Cargando programa para editar con ID: " + idProgramaEditar);
            Programa existente = programaDAO.buscarPorId(idProgramaEditar);
            if (existente != null) {
                System.out.println("   - Programa existente encontrado");
                System.out.println("      - Nombre: " + existente.getNombrePrograma());
                System.out.println("      - Nivel: " + existente.getNivelFormacion());
                System.out.println("      - Duración: " + existente.getDuracion());
                System.out.println("      - Jornada: " + existente.getJornadaId());
                System.out.println("      - Modalidad: " + existente.getModalidadId());
                System.out.println("      - Coordinación: " + existente.getCoordinacionId());
                
                programa = existente;
            } else {
                System.err.println("   ❌ No se encontró el programa con ID: " + idProgramaEditar);
                programa = new Programa();
            }
        } else {
            System.out.println("   - Creando nuevo programa");
            programa = new Programa();
        }
    }

    public String guardar() {
        System.out.println("🔍 ProgramaBean.guardar: Iniciando guardado");
        System.out.println("   - programa.getIdProgramas(): " + programa.getIdProgramas());
        System.out.println("   - programa.getNombrePrograma(): " + programa.getNombrePrograma());
        System.out.println("   - programa.getJornadaId(): " + programa.getJornadaId());
        System.out.println("   - programa.getModalidadId(): " + programa.getModalidadId());
        System.out.println("   - programa.getCoordinacionId(): " + programa.getCoordinacionId());
        
        // Validaciones
        if (programa.getNombrePrograma() == null || programa.getNombrePrograma().trim().isEmpty()) {
            System.err.println("❌ ProgramaBean.guardar: nombre vacío");
            FacesUtils.addErrorMessage("Debe ingresar un nombre para el programa.");
            return null;
        }
        
        if (programa.getNivelFormacion() == null || programa.getNivelFormacion().trim().isEmpty()) {
            System.err.println("❌ ProgramaBean.guardar: nivel de formación vacío");
            FacesUtils.addErrorMessage("Debe ingresar un nivel de formación.");
            return null;
        }
        
        if (programa.getDuracion() == null || programa.getDuracion().trim().isEmpty()) {
            System.err.println("❌ ProgramaBean.guardar: duración vacía");
            FacesUtils.addErrorMessage("Debe ingresar una duración.");
            return null;
        }
        
        if (programa.getJornadaId() == 0) {
            System.err.println("❌ ProgramaBean.guardar: jornada no seleccionada");
            FacesUtils.addErrorMessage("Debe seleccionar una jornada.");
            return null;
        }
        
        if (programa.getModalidadId() == 0) {
            System.err.println("❌ ProgramaBean.guardar: modalidad no seleccionada");
            FacesUtils.addErrorMessage("Debe seleccionar una modalidad.");
            return null;
        }
        
        if (programa.getCoordinacionId() == 0) {
            System.err.println("❌ ProgramaBean.guardar: coordinación no seleccionada");
            FacesUtils.addErrorMessage("Debe seleccionar una coordinación.");
            return null;
        }

        boolean guardado;
        
        if (programa.getIdProgramas() == 0) {
            System.out.println("   - Creando nuevo registro de programa");
            int idGenerado = programaDAO.guardar(programa);
            guardado = idGenerado > 0;
        } else {
            System.out.println("   - Actualizando registro existente");
            guardado = programaDAO.actualizar(programa);
        }

        if (guardado) {
            System.out.println("✅ ProgramaBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Programa registrado correctamente.");
            return "/pages/admin/listarProgramas.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ ProgramaBean.guardar: No se pudo guardar");
            FacesUtils.addErrorMessage("No fue posible guardar el programa. Verifique los logs del servidor.");
            return null;
        }
    }

    public String editar(int id) {
        System.out.println("🔍 ProgramaBean.editar: Editando programa ID: " + id);
        return "/pages/admin/formPrograma.xhtml?idProgramaEditar=" + id + "&faces-redirect=true";
    }

    public void eliminar(int id) {
        System.out.println("🔍 ProgramaBean.eliminar: Eliminando programa ID: " + id);
        boolean eliminado = programaDAO.eliminar(id);
        if (eliminado) {
            System.out.println("✅ ProgramaBean.eliminar: Eliminado exitosamente");
            FacesUtils.addInfoMessage("Programa eliminado correctamente.");
            // Recargar la lista
            cargarDatos();
        } else {
            System.err.println("❌ ProgramaBean.eliminar: No se pudo eliminar");
            FacesUtils.addErrorMessage("No fue posible eliminar el programa.");
        }
    }

    public void prepararNuevo() {
        System.out.println("🔍 ProgramaBean.prepararNuevo: Preparando nuevo programa");
        programa = new Programa();
    }

    // Métodos auxiliares para obtener nombres
    public String getNombreJornada(int idJornada) {
        for (Jornada j : getJornadas()) {
            if (j.getIdJornada() == idJornada) {
                return j.getNombreJornada();
            }
        }
        return "Jornada " + idJornada;
    }

    public String getNombreModalidad(int idModalidad) {
        for (Modalidad m : getModalidades()) {
            if (m.getIdModalidad() == idModalidad) {
                return m.getNombreModalidad();
            }
        }
        return "Modalidad " + idModalidad;
    }

    public String getNombreCoordinacion(int idCoordinacion) {
        for (Coordinacion c : getCoordinaciones()) {
            if (c.getIdCoordinacion() == idCoordinacion) {
                return c.getNombreCoordinacion();
            }
        }
        return "Coordinación " + idCoordinacion;
    }

    // GETTERS Y SETTERS

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Integer getIdProgramaEditar() {
        return idProgramaEditar;
    }

    public void setIdProgramaEditar(Integer idProgramaEditar) {
        this.idProgramaEditar = idProgramaEditar;
    }

    public List<Programa> getProgramas() {
        System.out.println("🔍 ProgramaBean.getProgramas: Getter llamado");
        System.out.println("   - inicializado: " + inicializado);
        System.out.println("   - programas es null: " + (programas == null));
        
        if (!inicializado) {
            System.out.println("   ⚠️ Bean no inicializado, ejecutando init()...");
            init();
        }
        
        // Siempre recargar desde el DAO para asegurar que tenemos todos los datos actualizados
        System.out.println("   - Recargando programas desde el DAO...");
        programas = programaDAO.listar();
        System.out.println("   - Programas cargados: " + (programas != null ? programas.size() : 0));
        
        System.out.println("   - Retornando " + (programas != null ? programas.size() : 0) + " programas");
        return programas != null ? programas : new java.util.ArrayList<>();
    }

    public List<Jornada> getJornadas() {
        System.out.println("🔍 ProgramaBean.getJornadas: Getter llamado");
        if (!inicializado) {
            System.out.println("   ⚠️ Bean no inicializado, ejecutando init()...");
            init();
        }
        if (jornadas == null || jornadas.isEmpty()) {
            System.out.println("   - Jornadas es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("   - Retornando " + (jornadas != null ? jornadas.size() : 0) + " jornadas");
        return jornadas != null ? jornadas : new java.util.ArrayList<>();
    }

    public List<Modalidad> getModalidades() {
        System.out.println("🔍 ProgramaBean.getModalidades: Getter llamado");
        if (!inicializado) {
            System.out.println("   ⚠️ Bean no inicializado, ejecutando init()...");
            init();
        }
        if (modalidades == null || modalidades.isEmpty()) {
            System.out.println("   - Modalidades es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("   - Retornando " + (modalidades != null ? modalidades.size() : 0) + " modalidades");
        return modalidades != null ? modalidades : new java.util.ArrayList<>();
    }

    public List<Coordinacion> getCoordinaciones() {
        System.out.println("🔍 ProgramaBean.getCoordinaciones: Getter llamado");
        if (!inicializado) {
            System.out.println("   ⚠️ Bean no inicializado, ejecutando init()...");
            init();
        }
        if (coordinaciones == null || coordinaciones.isEmpty()) {
            System.out.println("   - Coordinaciones es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("   - Retornando " + (coordinaciones != null ? coordinaciones.size() : 0) + " coordinaciones");
        return coordinaciones != null ? coordinaciones : new java.util.ArrayList<>();
    }

    public boolean isModoEdicion() {
        return idProgramaEditar != null && idProgramaEditar > 0;
    }
}

