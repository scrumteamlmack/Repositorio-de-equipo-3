package beans;

import dao.AprendizDAO;
import dao.ProgramaDAO;
import dao.FichaDAO;
import dao.UsuarioDAO;
import modelo.Aprendiz;
import modelo.Programa;
import modelo.Ficha;
import modelo.Usuario;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "aprendizBean")
@ViewScoped
public class AprendizBean implements Serializable {

    private final AprendizDAO aprendizDAO = new AprendizDAO();
    private final ProgramaDAO programaDAO = new ProgramaDAO();
    private final FichaDAO fichaDAO = new FichaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Aprendiz aprendiz = new Aprendiz();
    private Integer idUsuario;
    
    private boolean inicializado = false;

    public AprendizBean() {
<<<<<<< HEAD
        System.out.println("AprendizBean: Constructor llamado");
=======
        System.out.println("🔍 AprendizBean: Constructor llamado");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
    }

    private List<Programa> programas;
    private List<Ficha> fichas;

    private void cargarDatos() {
        try {
<<<<<<< HEAD
            System.out.println("AprendizBean.cargarDatos: Iniciando carga de datos");
            
            programas = programaDAO.listar();
            System.out.println("  Programas cargados: " + (programas != null ? programas.size() : 0));
=======
            System.out.println("🔍 AprendizBean.cargarDatos: Iniciando carga de datos");
            
            programas = programaDAO.listar();
            System.out.println("   - Programas cargados: " + (programas != null ? programas.size() : 0));
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            if (programas != null && !programas.isEmpty()) {
                for (Programa p : programas) {
                    System.out.println("      - Programa: " + p.getNombrePrograma() + " (ID: " + p.getIdProgramas() + ")");
                }
            } else {
<<<<<<< HEAD
                System.err.println(" No se encontraron programas en la base de datos");
            }
            
            fichas = fichaDAO.listar();
            System.out.println(" Fichas cargadas: " + (fichas != null ? fichas.size() : 0));
            if (fichas != null && !fichas.isEmpty()) {
                for (Ficha f : fichas) {
                    System.out.println("Ficha ID: " + f.getIdFicha() + ", Num: " + f.getNumFicha());
                }
            } else {
                System.err.println(" No se encontraron fichas en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("AprendizBean.cargarDatos: Error al cargar datos: " + e.getMessage());
=======
                System.err.println("   ⚠️ No se encontraron programas en la base de datos");
            }
            
            fichas = fichaDAO.listar();
            System.out.println("   - Fichas cargadas: " + (fichas != null ? fichas.size() : 0));
            if (fichas != null && !fichas.isEmpty()) {
                for (Ficha f : fichas) {
                    System.out.println("      - Ficha ID: " + f.getIdFicha() + ", Num: " + f.getNumFicha());
                }
            } else {
                System.err.println("   ⚠️ No se encontraron fichas en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("❌ AprendizBean.cargarDatos: Error al cargar datos: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
<<<<<<< HEAD
        System.out.println("AprendizBean.init: @PostConstruct ejecutado");
        System.out.println("idUsuario recibido: " + idUsuario);
        
        
        cargarDatos();
        inicializado = true;
        

=======
        System.out.println("🔍 AprendizBean.init: @PostConstruct ejecutado");
        System.out.println("   - idUsuario recibido: " + idUsuario);
        
        // Cargar programas y fichas siempre
        cargarDatos();
        inicializado = true;
        
        // Si no hay idUsuario, intentar obtenerlo de la URL manualmente
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (idUsuario == null || idUsuario == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println("  idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("AprendizBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
=======
                        System.out.println("   - idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ AprendizBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            }
        }
        
        if (idUsuario != null && idUsuario > 0) {
            Aprendiz existente = aprendizDAO.buscarPorUsuario(idUsuario);
            if (existente != null) {
<<<<<<< HEAD
                System.out.println("Aprendiz existente encontrado");
                aprendiz = existente;
            } else {
                System.out.println(" Creando nuevo aprendiz para usuario ID: " + idUsuario);
=======
                System.out.println("   - Aprendiz existente encontrado");
                aprendiz = existente;
            } else {
                System.out.println("   - Creando nuevo aprendiz para usuario ID: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                aprendiz = new Aprendiz();
                aprendiz.setIdUsuario(idUsuario);
            }
        } else {
<<<<<<< HEAD
            System.err.println("AprendizBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
=======
            System.err.println("⚠️ AprendizBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        }
    }

    public String guardar() {
<<<<<<< HEAD
        System.out.println("AprendizBean.guardar: Iniciando guardado");
        System.out.println(" idUsuario en bean: " + idUsuario);
        System.out.println(" aprendiz.getIdUsuario(): " + aprendiz.getIdUsuario());
        System.out.println(" aprendiz.getProgramaId(): " + aprendiz.getProgramaId());
        System.out.println(" aprendiz.getFichaId(): " + aprendiz.getFichaId());
       
=======
        System.out.println("🔍 AprendizBean.guardar: Iniciando guardado");
        System.out.println("   - idUsuario en bean: " + idUsuario);
        System.out.println("   - aprendiz.getIdUsuario(): " + aprendiz.getIdUsuario());
        System.out.println("   - aprendiz.getProgramaId(): " + aprendiz.getProgramaId());
        System.out.println("   - aprendiz.getFichaId(): " + aprendiz.getFichaId());
        
        // Intentar obtener idUsuario de la URL si no está establecido
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if ((aprendiz.getIdUsuario() == 0) && (idUsuario == null || idUsuario == 0)) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println(" idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("AprendizBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }

=======
                        System.out.println("   - idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ AprendizBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        // Establecer idUsuario en aprendiz si está disponible
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (aprendiz.getIdUsuario() == 0 && idUsuario != null && idUsuario > 0) {
            System.out.println("   - Estableciendo idUsuario desde variable: " + idUsuario);
            aprendiz.setIdUsuario(idUsuario);
        }
        
        if (aprendiz.getIdUsuario() == 0) {
<<<<<<< HEAD
            System.err.println("AprendizBean.guardar: idUsuario es 0 después de todos los intentos");
=======
            System.err.println("❌ AprendizBean.guardar: idUsuario es 0 después de todos los intentos");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Error: No se pudo identificar el usuario. Por favor, intente nuevamente.");
            return null;
        }
        
        if (aprendiz.getProgramaId() == 0) {
<<<<<<< HEAD
            System.err.println("AprendizBean.guardar: programaId es 0");
=======
            System.err.println("❌ AprendizBean.guardar: programaId es 0");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe seleccionar un programa.");
            return null;
        }
        
        if (aprendiz.getFichaId() == 0) {
<<<<<<< HEAD
            System.err.println("AprendizBean.guardar: fichaId es 0");
=======
            System.err.println("❌ AprendizBean.guardar: fichaId es 0");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe seleccionar una ficha.");
            return null;
        }

        boolean guardado;
        Aprendiz existente = aprendizDAO.buscarPorUsuario(aprendiz.getIdUsuario());
        
        if (existente == null) {
<<<<<<< HEAD
            System.out.println(" Creando nuevo registro de aprendiz");
            guardado = aprendizDAO.guardar(aprendiz);
        } else {
            System.out.println(" Actualizando registro existente");
=======
            System.out.println("   - Creando nuevo registro de aprendiz");
            guardado = aprendizDAO.guardar(aprendiz);
        } else {
            System.out.println("   - Actualizando registro existente");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            guardado = aprendizDAO.actualizar(aprendiz);
        }

        if (guardado) {
<<<<<<< HEAD
            System.out.println("AprendizBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Aprendiz registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("AprendizBean.guardar: No se pudo guardar");
=======
            System.out.println("✅ AprendizBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Aprendiz registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ AprendizBean.guardar: No se pudo guardar");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("No fue posible guardar los datos del aprendiz. Verifique los logs del servidor.");
            return null;
        }
    }

<<<<<<< HEAD
=======
    // GETTERS Y SETTERS
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf

    public Aprendiz getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(Aprendiz aprendiz) {
        this.aprendiz = aprendiz;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Programa> getProgramas() {
<<<<<<< HEAD
        System.out.println("AprendizBean.getProgramas: Getter llamado");
        System.out.println("inicializado: " + inicializado);
        System.out.println("programas es null: " + (programas == null));
        
        if (!inicializado) {
            System.out.println("Bean no inicializado, ejecutando init()...");
=======
        System.out.println("🔍 AprendizBean.getProgramas: Getter llamado");
        System.out.println("   - inicializado: " + inicializado);
        System.out.println("   - programas es null: " + (programas == null));
        
        if (!inicializado) {
            System.out.println("   ⚠️ Bean no inicializado, ejecutando init()...");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            init();
        }
        
        if (programas == null || programas.isEmpty()) {
<<<<<<< HEAD
            System.out.println("Programas es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("Retornando " + (programas != null ? programas.size() : 0) + " programas");
=======
            System.out.println("   - Programas es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("   - Retornando " + (programas != null ? programas.size() : 0) + " programas");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        return programas != null ? programas : new java.util.ArrayList<>();
    }

    public List<Ficha> getFichas() {
<<<<<<< HEAD
        System.out.println("AprendizBean.getFichas: Getter llamado");
=======
        System.out.println("🔍 AprendizBean.getFichas: Getter llamado");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (fichas == null || fichas.isEmpty()) {
            System.out.println("   - Fichas es null o vacío, cargando datos...");
            cargarDatos();
        }
<<<<<<< HEAD
        System.out.println("Retornando " + (fichas != null ? fichas.size() : 0) + " fichas");
=======
        System.out.println("   - Retornando " + (fichas != null ? fichas.size() : 0) + " fichas");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        return fichas != null ? fichas : new java.util.ArrayList<>();
    }

    public int contarAprendicesPorFicha(int idFicha) {
        List<Aprendiz> aprendices = aprendizDAO.buscarPorFicha(idFicha);
        return aprendices != null ? aprendices.size() : 0;
    }

    public List<Aprendiz> getAprendicesPorFicha(int idFicha) {
        return aprendizDAO.buscarPorFicha(idFicha);
    }

    public String getNombreAprendiz(int idUsuario) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        if (usuario != null) {
            return usuario.getPNombre() + " " + usuario.getPApellido();
        }
        return "Aprendiz " + idUsuario;
    }

    public String getNombrePrograma(int idPrograma) {
        Programa programa = programaDAO.buscarPorId(idPrograma);
        if (programa != null) {
            return programa.getNombrePrograma();
        }
        return "Programa " + idPrograma;
    }
}
