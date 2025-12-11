package beans;

import dao.AmbienteDAO;
import modelo.Ambiente;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "ambienteBean")
@ViewScoped
public class AmbienteBean implements Serializable {

    private final AmbienteDAO ambienteDAO = new AmbienteDAO();
    private Ambiente ambiente = new Ambiente();
    private List<Ambiente> ambientes;
    private List<Ambiente> ambientesFiltrados;
    private Integer idAmbienteEditar;

    @PostConstruct
    public void init() {
        ambientes = ambienteDAO.listar();

        cargarAmbienteSiEsNecesario();
    }
    
    private void cargarAmbienteSiEsNecesario() {
       
        try {
            javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
            if (facesContext != null) {
                String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        System.out.println("   - ID encontrado en URL en @PostConstruct: " + id);
                        
                        if (id > 0 && (ambiente == null || ambiente.getIdAmbiente() == 0)) {
                            Ambiente encontrado = ambienteDAO.buscarPorId(id);
                            if (encontrado != null) {
                                idAmbienteEditar = id;
                                ambiente = encontrado;
                                System.out.println(" Ambiente cargado en @PostConstruct: " + ambiente.getNumero());
                            }
                        }
                    } catch (NumberFormatException e) {
              
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ AmbienteBean.cargarAmbienteSiEsNecesario: Error: " + e.getMessage());
        }
    }

    public void prepararNuevo() {
        ambiente = new Ambiente();
    }

    public String guardar() {
        if (ambiente.getIdAmbiente() == 0) {
            int id = ambienteDAO.guardar(ambiente);
            if (id > 0) {
                FacesUtils.addInfoMessage("Ambiente registrado correctamente.");
                ambientes = ambienteDAO.listar();
                prepararNuevo();
                return "/pages/admin/listarAmbientes.xhtml?faces-redirect=true";
            } else {
                FacesUtils.addErrorMessage("No fue posible guardar el ambiente.");
                return null;
            }
        } else {
            ambienteDAO.actualizar(ambiente);
            FacesUtils.addInfoMessage("Ambiente actualizado correctamente.");
            ambientes = ambienteDAO.listar();
            prepararNuevo();
            return "/pages/admin/listarAmbientes.xhtml?faces-redirect=true";
        }
    }

    public String editar(int idAmbiente) {
        return "/pages/admin/editarAmbiente.xhtml?id=" + idAmbiente + "&faces-redirect=true";
    }
    
    public String cargarAmbienteParaEditar() {

        System.out.println("🔍 AmbienteBean.cargarAmbienteParaEditar: INICIO");
        System.out.println("   - idAmbienteEditar desde viewParam: " + idAmbienteEditar);
        System.out.println("   - ambiente actual: " + (ambiente != null ? "ID=" + ambiente.getIdAmbiente() : "null"));
        
    
        if (ambiente != null && ambiente.getIdAmbiente() > 0 && idAmbienteEditar != null && ambiente.getIdAmbiente() == idAmbienteEditar) {
            System.out.println("   ✅ Ambiente ya está cargado, no es necesario recargar");
            return null;
        }

        if (idAmbienteEditar == null || idAmbienteEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    System.out.println("   - Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idAmbienteEditar = Integer.parseInt(idParam);
                        System.out.println(" idAmbienteEditar parseado desde URL: " + idAmbienteEditar);
                    } else {
                        System.err.println(" No se encontró parámetro 'id' en la URL");
                    }
                } else {
                    System.err.println("FacesContext es null");
                }
            } catch (Exception e) {
                System.err.println("AmbienteBean.cargarAmbienteParaEditar: Error al obtener idAmbienteEditar de URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println(" idAmbienteEditar FINAL: " + idAmbienteEditar);

        if (idAmbienteEditar != null && idAmbienteEditar > 0) {
            System.out.println("   - Llamando a ambienteDAO.buscarPorId(" + idAmbienteEditar + ")");
            Ambiente encontrado = ambienteDAO.buscarPorId(idAmbienteEditar);
            
            if (encontrado != null) {
                System.out.println(" Ambiente encontrado en BD:");
                System.out.println("      * ID: " + encontrado.getIdAmbiente());
                System.out.println("      * Número: " + encontrado.getNumero());
                System.out.println("      * Capacidad: " + encontrado.getCapacidad());
                System.out.println("      * Tipo: " + encontrado.getTipo());
                System.out.println("      * Estado: " + encontrado.getEstado());
                

                if (ambiente == null) {
                    ambiente = new Ambiente();
                }
                ambiente.setIdAmbiente(encontrado.getIdAmbiente());
                ambiente.setNumero(encontrado.getNumero());
                ambiente.setCapacidad(encontrado.getCapacidad());
                ambiente.setTipo(encontrado.getTipo());
                ambiente.setEstado(encontrado.getEstado());
                
                System.out.println(" Datos asignados al bean ambiente:");
                System.out.println(" ambiente.getIdAmbiente(): " + ambiente.getIdAmbiente());
                System.out.println(" ambiente.getNumero(): " + ambiente.getNumero());
                System.out.println(" ambiente.getCapacidad(): " + ambiente.getCapacidad());
                System.out.println(" ambiente.getTipo(): " + ambiente.getTipo());
                System.out.println(" ambiente.getEstado(): " + ambiente.getEstado());
            } else {
                System.err.println(" Ambiente NO encontrado en BD con ID: " + idAmbienteEditar);
                FacesUtils.addErrorMessage("Ambiente no encontrado con ID: " + idAmbienteEditar);
            }
        } else {
            System.err.println(" idAmbienteEditar es null o 0 - No se puede cargar ambiente");
            System.err.println(" Esto puede indicar que el viewParam no funcionó correctamente");
            FacesUtils.addErrorMessage("No se proporcionó ID de ambiente válido para editar.");
        }
        
        return null;
    }
    
    public String actualizarAmbiente() {
        System.out.println("AmbienteBean.actualizarAmbiente: Actualizando ambiente ID: " + ambiente.getIdAmbiente());
        
        if (ambiente.getIdAmbiente() == 0) {
            FacesUtils.addErrorMessage("Error: No se puede actualizar un ambiente sin ID.");
            return null;
        }
        
        boolean actualizado = ambienteDAO.actualizar(ambiente);
        
        if (actualizado) {
            FacesUtils.addInfoMessage("Ambiente actualizado correctamente.");
            ambientes = ambienteDAO.listar();
            prepararNuevo();
            return "/pages/admin/listarAmbientes.xhtml?faces-redirect=true";
        } else {
            FacesUtils.addErrorMessage("Error al actualizar el ambiente.");
            return null;
        }
    }

    public void eliminar(int idAmbiente) {
        ambienteDAO.eliminar(idAmbiente);
        ambientes = ambienteDAO.listar();
        FacesUtils.addInfoMessage("Ambiente eliminado.");
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public List<Ambiente> getAmbientes() {
        if (ambientes == null) {
            ambientes = ambienteDAO.listar();
        }
        if (ambientesFiltrados != null && !ambientesFiltrados.isEmpty()) {
            return ambientesFiltrados;
        }
        return ambientes;
    }

    public List<Ambiente> getAmbientesFiltrados() {
        return ambientesFiltrados;
    }

    public void setAmbientesFiltrados(List<Ambiente> ambientesFiltrados) {
        this.ambientesFiltrados = ambientesFiltrados;
    }

    public Integer getIdAmbienteEditar() {
        return idAmbienteEditar;
    }

    public void setIdAmbienteEditar(Integer idAmbienteEditar) {
        this.idAmbienteEditar = idAmbienteEditar;
    }
}

