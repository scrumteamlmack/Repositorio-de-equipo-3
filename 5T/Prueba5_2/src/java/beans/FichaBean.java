package beans;

import dao.FichaDAO;
import dao.InstructorDAO;
import dao.UsuarioDAO;
import modelo.Ficha;
import modelo.Instructor;
import modelo.Usuario;
import util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "fichaBean")
@ViewScoped
public class FichaBean implements Serializable {

    private final FichaDAO fichaDAO = new FichaDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Ficha ficha = new Ficha();
    private List<Ficha> fichas;
    private List<Ficha> fichasInstructor = new ArrayList<>(); // Inicializar como lista vacía
    private List<Instructor> instructores;
    private List<String> instructoresSeleccionados = new ArrayList<>();
    private Integer idFichaEditar;

    public void init() {
        System.out.println("🔍 FichaBean.init: Inicializando bean");
        
        // Solo cargar fichas e instructores si no están cargados
        if (fichas == null) {
            fichas = fichaDAO.listar();
        }
        if (instructores == null) {
            cargarInstructores();
        }
        
        // Cargar fichas del instructor actual (siempre que se inicialice el bean)
        cargarFichasInstructor();
        
        // Intentar obtener idFichaEditar de la URL si no está establecido
        if (idFichaEditar == null || idFichaEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("fichaId");
                    if (idParam == null || idParam.isEmpty()) {
                        idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    }
                    if (idParam != null && !idParam.isEmpty()) {
                        idFichaEditar = Integer.parseInt(idParam);
                        System.out.println("   - idFichaEditar obtenido de URL: " + idFichaEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ FichaBean.init: Error al obtener idFichaEditar de URL: " + e.getMessage());
            }
        }
        
        if (idFichaEditar != null && idFichaEditar > 0) {
            ficha = fichaDAO.buscarPorId(idFichaEditar);
            if (ficha != null) {
                // Cargar instructores asignados a esta ficha
                List<Ficha> fichasMismoNumero = fichaDAO.buscarPorNumero(ficha.getNumFicha());
                if (instructoresSeleccionados == null) {
                    instructoresSeleccionados = new ArrayList<>();
                } else {
                    instructoresSeleccionados.clear();
                }
                for (Ficha f : fichasMismoNumero) {
                    instructoresSeleccionados.add(String.valueOf(f.getInstructorUsuarioId()));
                }
            }
        } else {
            if (ficha == null) {
                ficha = new Ficha();
            }
            if (instructoresSeleccionados == null) {
                instructoresSeleccionados = new ArrayList<>();
            } else {
                instructoresSeleccionados.clear();
            }
        }
        
        System.out.println("✅ FichaBean.init: Bean inicializado correctamente");
    }

    private void cargarInstructores() {
        List<Instructor> instructoresBD = instructorDAO.listar();
        instructores = new ArrayList<>();
        
        for (Instructor inst : instructoresBD) {
            Usuario usuario = usuarioDAO.buscarPorId(inst.getIdUsuario());
            if (usuario != null && "Activo".equals(inst.getEstado())) {
                instructores.add(inst);
            }
        }
    }

    public String guardar() {
        if (ficha.getNumFicha() == 0) {
            FacesUtils.addErrorMessage("El número de ficha es obligatorio");
            return null;
        }

        if (instructoresSeleccionados == null || instructoresSeleccionados.isEmpty()) {
            FacesUtils.addErrorMessage("Debe seleccionar al menos un instructor");
            return null;
        }

        try {
            // Si es edición, eliminar las asignaciones anteriores de esta ficha
            if (idFichaEditar != null && idFichaEditar > 0) {
                Ficha fichaExistente = fichaDAO.buscarPorId(idFichaEditar);
                if (fichaExistente != null) {
                    // Eliminar todas las fichas con el mismo número
                    fichaDAO.eliminarPorNumero(fichaExistente.getNumFicha());
                }
            }

            // Crear una ficha para cada instructor seleccionado
            boolean alMenosUnoGuardado = false;
            for (String instructorIdStr : instructoresSeleccionados) {
                try {
                    int instructorId = Integer.parseInt(instructorIdStr);
                    Ficha nuevaFicha = new Ficha();
                    nuevaFicha.setNumFicha(ficha.getNumFicha());
                    nuevaFicha.setInstructorUsuarioId(instructorId);
                    
                    int idGenerado = fichaDAO.guardar(nuevaFicha);
                    if (idGenerado > 0) {
                        alMenosUnoGuardado = true;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("❌ FichaBean.guardar: Error al convertir instructorId: " + instructorIdStr);
                    e.printStackTrace();
                }
            }

            if (alMenosUnoGuardado) {
                FacesUtils.addInfoMessage("Ficha guardada correctamente");
                return "/pages/admin/listarFichas.xhtml?faces-redirect=true";
            } else {
                FacesUtils.addErrorMessage("No se pudo guardar la ficha");
                return null;
            }

        } catch (Exception e) {
            System.err.println("❌ FichaBean.guardar: Error: " + e.getMessage());
            e.printStackTrace();
            FacesUtils.addErrorMessage("Error al guardar la ficha: " + e.getMessage());
            return null;
        }
    }

    public String eliminar(int idFicha) {
        Ficha fichaEliminar = fichaDAO.buscarPorId(idFicha);
        if (fichaEliminar != null) {
            // Eliminar todas las fichas con el mismo número
            if (fichaDAO.eliminarPorNumero(fichaEliminar.getNumFicha())) {
                FacesUtils.addInfoMessage("Ficha eliminada correctamente");
            } else {
                FacesUtils.addErrorMessage("No se pudo eliminar la ficha");
            }
        }
        fichas = fichaDAO.listar();
        return null;
    }

    public String editar(int idFicha) {
        return "/pages/admin/formFicha.xhtml?id=" + idFicha + "&faces-redirect=true";
    }

    public String verAprendices(int idFicha) {
        return "/pages/instructor/aprendicesFicha.xhtml?fichaId=" + idFicha + "&faces-redirect=true";
    }

    public String prepararNuevo() {
        ficha = new Ficha();
        instructoresSeleccionados.clear();
        return "/pages/admin/formFicha.xhtml?faces-redirect=true";
    }

    // Getters y Setters
    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public List<Ficha> getFichas() {
        if (fichas == null) {
            fichas = fichaDAO.listar();
        }
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public List<Instructor> getInstructores() {
        if (instructores == null) {
            cargarInstructores();
        }
        return instructores;
    }

    public void setInstructores(List<Instructor> instructores) {
        this.instructores = instructores;
    }

    public List<String> getInstructoresSeleccionados() {
        return instructoresSeleccionados;
    }

    public void setInstructoresSeleccionados(List<String> instructoresSeleccionados) {
        this.instructoresSeleccionados = instructoresSeleccionados;
    }

    public Integer getIdFichaEditar() {
        return idFichaEditar;
    }

    public void setIdFichaEditar(Integer idFichaEditar) {
        this.idFichaEditar = idFichaEditar;
    }

    public String getNombreInstructor(int instructorId) {
        Usuario usuario = usuarioDAO.buscarPorId(instructorId);
        if (usuario != null) {
            return usuario.getPNombre() + " " + usuario.getPApellido();
        }
        return "Instructor " + instructorId;
    }

    public List<Ficha> getFichasAgrupadas() {
        List<Ficha> todasFichas = fichaDAO.listar();
        List<Ficha> fichasUnicas = new ArrayList<>();
        List<Integer> numerosProcesados = new ArrayList<>();

        for (Ficha f : todasFichas) {
            if (!numerosProcesados.contains(f.getNumFicha())) {
                fichasUnicas.add(f);
                numerosProcesados.add(f.getNumFicha());
            }
        }

        return fichasUnicas;
    }

    public List<Integer> getInstructoresDeFicha(int numFicha) {
        List<Ficha> fichasMismoNumero = fichaDAO.buscarPorNumero(numFicha);
        List<Integer> instructorIds = new ArrayList<>();
        for (Ficha f : fichasMismoNumero) {
            instructorIds.add(f.getInstructorUsuarioId());
        }
        return instructorIds;
    }

    public List<Ficha> getFichasPorInstructor() {
        System.out.println("🔍 FichaBean.getFichasPorInstructor: Llamado");
        if (fichasInstructor == null) {
            System.out.println("   - fichasInstructor es null, cargando...");
            cargarFichasInstructor();
        }
        System.out.println("   - Retornando " + (fichasInstructor != null ? fichasInstructor.size() : 0) + " fichas");
        return fichasInstructor != null ? fichasInstructor : new ArrayList<>();
    }
    
    public List<Ficha> getFichasInstructor() {
        System.out.println("🔍 FichaBean.getFichasInstructor: Getter llamado");
        System.out.println("   - fichasInstructor actual: " + (fichasInstructor != null ? fichasInstructor.size() + " elementos" : "null"));
        
        // Si es null o está vacía, cargar las fichas
        if (fichasInstructor == null || fichasInstructor.isEmpty()) {
            System.out.println("   - fichasInstructor es null o vacía, cargando...");
            cargarFichasInstructor();
        }
        
        System.out.println("   - Retornando " + (fichasInstructor != null ? fichasInstructor.size() : 0) + " fichas");
        return fichasInstructor != null ? fichasInstructor : new ArrayList<>();
    }

    public void setFichasInstructor(List<Ficha> fichasInstructor) {
        this.fichasInstructor = fichasInstructor;
    }

    private void cargarFichasInstructor() {
        System.out.println("🔍 FichaBean.cargarFichasInstructor: Cargando fichas del instructor");
        fichasInstructor = new ArrayList<>();
        
        try {
            javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
            if (facesContext == null) {
                System.err.println("⚠️ FichaBean.cargarFichasInstructor: FacesContext es null");
                return;
            }
            
            // Intentar obtener userId de múltiples formas
            Object userIdObj = null;
            
            // Método 1: Desde el sessionMap directamente
            userIdObj = facesContext.getExternalContext().getSessionMap().get("userId");
            System.out.println("   - userIdObj de sessionMap: " + userIdObj);
            
            // Método 2: Intentar obtener desde LoginBean usando EL
            if (userIdObj == null) {
                try {
                    javax.el.ELContext elContext = facesContext.getELContext();
                    javax.el.ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();
                    javax.el.ValueExpression ve = factory.createValueExpression(elContext, "#{loginBean.usuarioAutenticado.idUsuario}", Object.class);
                    userIdObj = ve.getValue(elContext);
                    System.out.println("   - userIdObj de LoginBean: " + userIdObj);
                } catch (Exception e) {
                    System.err.println("⚠️ FichaBean.cargarFichasInstructor: Error al obtener de LoginBean: " + e.getMessage());
                }
            }
            
            // Método 3: Listar todas las claves de la sesión para debug
            System.out.println("   - Claves en sessionMap: " + facesContext.getExternalContext().getSessionMap().keySet());
            
            if (userIdObj != null) {
                int instructorId;
                if (userIdObj instanceof Integer) {
                    instructorId = (Integer) userIdObj;
                } else if (userIdObj instanceof String) {
                    instructorId = Integer.parseInt((String) userIdObj);
                } else {
                    System.err.println("❌ FichaBean.cargarFichasInstructor: Tipo inesperado: " + userIdObj.getClass().getName());
                    return;
                }
                
                System.out.println("   - Instructor ID obtenido: " + instructorId);
                fichasInstructor = fichaDAO.buscarPorInstructor(instructorId);
                System.out.println("   - Fichas encontradas en BD: " + (fichasInstructor != null ? fichasInstructor.size() : 0));
                
                if (fichasInstructor != null && !fichasInstructor.isEmpty()) {
                    for (Ficha f : fichasInstructor) {
                        System.out.println("      - Ficha ID: " + f.getIdFicha() + ", Num: " + f.getNumFicha() + ", Instructor: " + f.getInstructorUsuarioId());
                    }
                }
            } else {
                System.err.println("⚠️ FichaBean.cargarFichasInstructor: userIdObj es null - no se pudo obtener de ninguna fuente");
            }
        } catch (Exception e) {
            System.err.println("❌ FichaBean.cargarFichasInstructor: Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

