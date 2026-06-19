package beans;

import dao.AmbienteDAO;
import dao.RecursoDAO;
import dao.TipoRecursoDAO;
import modelo.Ambiente;
import modelo.Recurso;
import modelo.TipoRecurso;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "recursoBean")
@ViewScoped
public class RecursoBean implements Serializable {

    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final TipoRecursoDAO tipoRecursoDAO = new TipoRecursoDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    private Recurso recurso = new Recurso();
    private List<Recurso> recursos;
    private List<Recurso> recursosFiltrados;
    private List<TipoRecurso> tipos;
    private List<Ambiente> ambientes;
    private Integer idRecursoEditar;

    @PostConstruct
    public void init() {
        recursos = recursoDAO.listar();
        tipos = tipoRecursoDAO.listar();
        ambientes = ambienteDAO.listar();
        cargarRecursoSiEsNecesario();
    }
    
    private void cargarRecursoSiEsNecesario() {
        try {
            javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
            if (facesContext != null) {
                String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println("  ID encontrado en URL en @PostConstruct: " + id);
=======
                        System.out.println("   - ID encontrado en URL en @PostConstruct: " + id);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                        if (id > 0 && (recurso == null || recurso.getIdRecurso() == 0)) {
                            Recurso encontrado = recursoDAO.buscarPorId(id);
                            if (encontrado != null) {
                                idRecursoEditar = id;
                                recurso = encontrado;
<<<<<<< HEAD
                                System.out.println(" Recurso cargado en @PostConstruct: " + recurso.getNombre());
=======
                                System.out.println("   ✅ Recurso cargado en @PostConstruct: " + recurso.getNombre());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (Exception e) {
<<<<<<< HEAD
            System.err.println("RecursoBean.cargarRecursoSiEsNecesario: Error: " + e.getMessage());
=======
            System.err.println("⚠️ RecursoBean.cargarRecursoSiEsNecesario: Error: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        }
    }

    public void prepararNuevo() {
        recurso = new Recurso();
    }

    public String guardar() {
        if (recurso.getIdRecurso() == 0) {
            recursoDAO.guardar(recurso);
            FacesUtils.addInfoMessage("Recurso registrado correctamente.");
            recursos = recursoDAO.listar();
            prepararNuevo();
            return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
        } else {
            recursoDAO.actualizar(recurso);
            FacesUtils.addInfoMessage("Recurso actualizado correctamente.");
            recursos = recursoDAO.listar();
            prepararNuevo();
            return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
        }
    }

    public String editar(int idRecurso) {
        return "/pages/admin/editarRecurso.xhtml?id=" + idRecurso + "&faces-redirect=true";
    }
    
    public void editar(Recurso seleccionado) {
        recurso = seleccionado;
    }
    
    public String cargarRecursoParaEditar() {
<<<<<<< HEAD
        System.out.println("RecursoBean.cargarRecursoParaEditar: INICIO");
        System.out.println(" idRecursoEditar desde viewParam: " + idRecursoEditar);
        System.out.println(" recurso actual: " + (recurso != null ? "ID=" + recurso.getIdRecurso() : "null"));
        
        if (recurso != null && recurso.getIdRecurso() > 0 && idRecursoEditar != null && recurso.getIdRecurso() == idRecursoEditar) {
            System.out.println(" Recurso ya está cargado, no es necesario recargar");
=======
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔍 RecursoBean.cargarRecursoParaEditar: INICIO");
        System.out.println("   - idRecursoEditar desde viewParam: " + idRecursoEditar);
        System.out.println("   - recurso actual: " + (recurso != null ? "ID=" + recurso.getIdRecurso() : "null"));
        
        if (recurso != null && recurso.getIdRecurso() > 0 && idRecursoEditar != null && recurso.getIdRecurso() == idRecursoEditar) {
            System.out.println("   ✅ Recurso ya está cargado, no es necesario recargar");
            System.out.println("═══════════════════════════════════════════════════════");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            return null;
        }
        
        if (idRecursoEditar == null || idRecursoEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
<<<<<<< HEAD
                    System.out.println(" Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idRecursoEditar = Integer.parseInt(idParam);
                        System.out.println(" idRecursoEditar parseado desde URL: " + idRecursoEditar);
                    } else {
                        System.err.println(" No se encontró parámetro 'id' en la URL");
                    }
                } else {
                    System.err.println(" FacesContext es null");
                }
            } catch (Exception e) {
                System.err.println("RecursoBean.cargarRecursoParaEditar: Error al obtener idRecursoEditar de URL: " + e.getMessage());
=======
                    System.out.println("   - Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idRecursoEditar = Integer.parseInt(idParam);
                        System.out.println("   ✅ idRecursoEditar parseado desde URL: " + idRecursoEditar);
                    } else {
                        System.err.println("   ❌ No se encontró parámetro 'id' en la URL");
                    }
                } else {
                    System.err.println("   ❌ FacesContext es null");
                }
            } catch (Exception e) {
                System.err.println("⚠️ RecursoBean.cargarRecursoParaEditar: Error al obtener idRecursoEditar de URL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                e.printStackTrace();
            }
        }
        
<<<<<<< HEAD
        System.out.println(" idRecursoEditar FINAL: " + idRecursoEditar);
        
        if (idRecursoEditar != null && idRecursoEditar > 0) {
            System.out.println("  Llamando a recursoDAO.buscarPorId(" + idRecursoEditar + ")");
            Recurso encontrado = recursoDAO.buscarPorId(idRecursoEditar);
            
            if (encontrado != null) {
                System.out.println("Recurso encontrado en BD:");
                System.out.println("ID: " + encontrado.getIdRecurso());
                System.out.println("Nombre: " + encontrado.getNombre());
                System.out.println("Serial: " + encontrado.getSerial());
                System.out.println("Número: " + encontrado.getNumero());
                System.out.println("Tipo: " + encontrado.getIdTipoRecurso());
                System.out.println("Ambiente: " + encontrado.getIdAmbiente());
                System.out.println("Estado: " + encontrado.getEstado());
                System.out.println("Observación: " + encontrado.getObservacion());
=======
        System.out.println("   - idRecursoEditar FINAL: " + idRecursoEditar);
        
        if (idRecursoEditar != null && idRecursoEditar > 0) {
            System.out.println("   - Llamando a recursoDAO.buscarPorId(" + idRecursoEditar + ")");
            Recurso encontrado = recursoDAO.buscarPorId(idRecursoEditar);
            
            if (encontrado != null) {
                System.out.println("   ✅ Recurso encontrado en BD:");
                System.out.println("      * ID: " + encontrado.getIdRecurso());
                System.out.println("      * Nombre: " + encontrado.getNombre());
                System.out.println("      * Serial: " + encontrado.getSerial());
                System.out.println("      * Número: " + encontrado.getNumero());
                System.out.println("      * Tipo: " + encontrado.getIdTipoRecurso());
                System.out.println("      * Ambiente: " + encontrado.getIdAmbiente());
                System.out.println("      * Estado: " + encontrado.getEstado());
                System.out.println("      * Observación: " + encontrado.getObservacion());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                
                if (recurso == null) {
                    recurso = new Recurso();
                }
                recurso.setIdRecurso(encontrado.getIdRecurso());
                recurso.setNombre(encontrado.getNombre());
                recurso.setSerial(encontrado.getSerial());
                recurso.setNumero(encontrado.getNumero());
                recurso.setIdTipoRecurso(encontrado.getIdTipoRecurso());
                recurso.setIdAmbiente(encontrado.getIdAmbiente());
                recurso.setEstado(encontrado.getEstado());
                recurso.setObservacion(encontrado.getObservacion() != null ? encontrado.getObservacion() : "");
                
<<<<<<< HEAD
                System.out.println(" Datos asignados al bean recurso:");
                System.out.println(" recurso.getIdRecurso(): " + recurso.getIdRecurso());
                System.out.println(" recurso.getNombre(): " + recurso.getNombre());
                System.out.println(" recurso.getSerial(): " + recurso.getSerial());
                System.out.println(" recurso.getNumero(): " + recurso.getNumero());
                System.out.println(" recurso.getIdTipoRecurso(): " + recurso.getIdTipoRecurso());
                System.out.println(" recurso.getIdAmbiente(): " + recurso.getIdAmbiente());
                System.out.println(" recurso.getEstado(): " + recurso.getEstado());
            } else {
                System.err.println(" Recurso NO encontrado en BD con ID: " + idRecursoEditar);
                FacesUtils.addErrorMessage("Recurso no encontrado con ID: " + idRecursoEditar);
            }
        } else {
            System.err.println(" idRecursoEditar es null o 0 - No se puede cargar recurso");
            System.err.println(" Esto puede indicar que el viewParam no funcionó correctamente");
=======
                System.out.println("   ✅ Datos asignados al bean recurso:");
                System.out.println("      * recurso.getIdRecurso(): " + recurso.getIdRecurso());
                System.out.println("      * recurso.getNombre(): " + recurso.getNombre());
                System.out.println("      * recurso.getSerial(): " + recurso.getSerial());
                System.out.println("      * recurso.getNumero(): " + recurso.getNumero());
                System.out.println("      * recurso.getIdTipoRecurso(): " + recurso.getIdTipoRecurso());
                System.out.println("      * recurso.getIdAmbiente(): " + recurso.getIdAmbiente());
                System.out.println("      * recurso.getEstado(): " + recurso.getEstado());
                System.out.println("═══════════════════════════════════════════════════════");
            } else {
                System.err.println("   ❌ Recurso NO encontrado en BD con ID: " + idRecursoEditar);
                FacesUtils.addErrorMessage("Recurso no encontrado con ID: " + idRecursoEditar);
            }
        } else {
            System.err.println("   ❌ idRecursoEditar es null o 0 - No se puede cargar recurso");
            System.err.println("   - Esto puede indicar que el viewParam no funcionó correctamente");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("No se proporcionó ID de recurso válido para editar.");
        }
        
        return null;
    }
    
    public String actualizarRecurso() {
<<<<<<< HEAD
        System.out.println("RecursoBean.actualizarRecurso: Actualizando recurso ID: " + recurso.getIdRecurso());
=======
        System.out.println("🔍 RecursoBean.actualizarRecurso: Actualizando recurso ID: " + recurso.getIdRecurso());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        
        if (recurso.getIdRecurso() == 0) {
            FacesUtils.addErrorMessage("Error: No se puede actualizar un recurso sin ID.");
            return null;
        }
        
        boolean actualizado = recursoDAO.actualizar(recurso);
        
        if (actualizado) {
            FacesUtils.addInfoMessage("Recurso actualizado correctamente.");
            recursos = recursoDAO.listar();
            prepararNuevo();
            return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
        } else {
            FacesUtils.addErrorMessage("Error al actualizar el recurso.");
            return null;
        }
    }

    public void eliminar(int idRecurso) {
        recursoDAO.eliminar(idRecurso);
        recursos = recursoDAO.listar();
        FacesUtils.addInfoMessage("Recurso eliminado.");
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public List<Recurso> getRecursos() {
        if (recursos == null) {
            recursos = recursoDAO.listar();
        }
        if (recursosFiltrados != null && !recursosFiltrados.isEmpty()) {
            return recursosFiltrados;
        }
        return recursos;
    }

    public List<Recurso> getRecursosFiltrados() {
        return recursosFiltrados;
    }

    public void setRecursosFiltrados(List<Recurso> recursosFiltrados) {
        this.recursosFiltrados = recursosFiltrados;
    }

    public List<TipoRecurso> getTipos() {
        if (tipos == null) {
            tipos = tipoRecursoDAO.listar();
        }
        return tipos;
    }

    public List<Ambiente> getAmbientes() {
        if (ambientes == null) {
            ambientes = ambienteDAO.listar();
        }
        return ambientes;
    }

    public Integer getIdRecursoEditar() {
        return idRecursoEditar;
    }

    public void setIdRecursoEditar(Integer idRecursoEditar) {
        this.idRecursoEditar = idRecursoEditar;
    }
}

