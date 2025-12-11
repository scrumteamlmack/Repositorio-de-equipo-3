# GUÍA COMPLETA DEL PROYECTO L-MACK

## ÍNDICE
1. [Arquitectura General](#arquitectura-general)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Paquetes y Archivos](#paquetes-y-archivos)
4. [Flujo de Datos](#flujo-de-datos)
5. [Explicación Detallada de Archivos Clave](#explicación-detallada-de-archivos-clave)
6. [Funcionalidades por Módulo](#funcionalidades-por-módulo)

---

## ARQUITECTURA GENERAL

### Patrón de Diseño: MVC (Model-View-Controller)

El proyecto sigue el patrón **MVC** adaptado para aplicaciones JSF:

- **Model (Modelo)**: Clases en `src/java/modelo/` que representan entidades de la base de datos
- **View (Vista)**: Archivos XHTML en `web/pages/` que definen la interfaz de usuario
- **Controller (Controlador)**: Managed Beans en `src/java/beans/` que manejan la lógica de negocio

### Tecnologías Utilizadas

- **JavaServer Faces (JSF)**: Framework para aplicaciones web Java
- **PrimeFaces**: Biblioteca de componentes UI para JSF
- **MySQL**: Base de datos relacional
- **JDBC**: API para conectarse a la base de datos
- **GlassFish**: Servidor de aplicaciones Java EE

### Flujo de Petición

```
Usuario → XHTML (Vista) → Managed Bean (Controlador) → DAO (Acceso a Datos) → Base de Datos
         ←                ←                          ←                      ←
```

---

## ESTRUCTURA DEL PROYECTO

```
Prueba5/
├── src/java/
│   ├── beans/          # Managed Beans (Controladores)
│   ├── dao/            # Data Access Objects (Acceso a Datos)
│   ├── modelo/         # Entidades/Modelos (POJOs)
│   ├── util/           # Utilidades (Helpers)
│   └── filter/         # Filtros de Servlet
├── web/
│   ├── pages/          # Páginas XHTML (Vistas)
│   ├── resources/      # CSS, JS, imágenes
│   └── WEB-INF/        # Configuración web
└── nbproject/          # Configuración NetBeans
```

---

## PAQUETES Y ARCHIVOS

### 1. PAQUETE `modelo` (Entidades)

**Propósito**: Representar las tablas de la base de datos como objetos Java (POJOs - Plain Old Java Objects).

#### Archivos Principales:

- **`Usuario.java`**: Representa la tabla `usuario`
  - Campos: `idUsuario`, `pNombre`, `sNombre`, `pApellido`, `sApellido`, `tipoDocumento`, `numDocumento`, `correo`, `contrasena`
  - Métodos: Getters/Setters estándar, `getNombre()` (retorna nombre completo)

- **`Recurso.java`**: Representa la tabla `recurso`
  - Campos: `idRecurso`, `nombre`, `numero`, `descripcion`, `estado`, `idTipoRecurso`, `idAmbiente`

- **`Ambiente.java`**: Representa la tabla `ambiente`
  - Campos: `idAmbiente`, `numero`, `nombre`, `capacidad`, `estado`

- **`Asistencia.java`**: Representa la tabla `registro_asistencia`
  - Campos: `idAsistencia`, `fecha`, `estado`, `idAprendiz`, `idInstructor`, `idJornada`

- **`Minuta.java`**: Representa la tabla `registro_minuta`
  - Campos: `idMinuta`, `fechaRecibo`, `fechaEntrega`, `novedad`, `descripcion`, `estado`, `idAmbiente`, `idGuarda`, `idResponsable`
  - Campos auxiliares: `ambienteNombre`, `guardaNombre`, `responsableNombre` (para mostrar en tablas)

- **`Incidente.java`**: Representa la tabla `registro_incidente`
  - Campos: `idIncidente`, `descripcion`, `fecha`, `hora`, `idAmbiente`, `idTipoIncidente`, `idReportador`

- **`TrasladoRecurso.java`**: Representa la tabla `traslado_recurso`
  - Campos: `idTraslado`, `fechaTraslado`, `observacion`, `idRecurso`, `idAmbienteOrigen`, `idAmbienteDestino`
  - Campos auxiliares: `recursoNombre`, `ambienteOrigenNombre`, `ambienteDestinoNombre`

- **Otros modelos**: `Aprendiz`, `Instructor`, `Coordinador`, `GuardaSeguridad`, `Ficha`, `Programa`, `Jornada`, `Modalidad`, `Coordinacion`, `TipoRecurso`, `TipoIncidente`, `Rol`, `UserRol`

**Cómo funciona**: Cada modelo es una clase Java simple con:
- Propiedades privadas (campos)
- Constructor vacío (requerido por JSF)
- Getters y Setters (requeridos por JSF para binding)
- Implementa `Serializable` (requerido para sesiones)

---

### 2. PAQUETE `dao` (Data Access Object)

**Propósito**: Abstraer las operaciones de base de datos (CRUD: Create, Read, Update, Delete).

#### Archivo Clave: `ConnBD.java`

```java
public class ConnBD {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";
    
    public static Connection conectar() {
        // Carga el driver MySQL
        Class.forName("com.mysql.jdbc.Driver");
        // Establece conexión con la base de datos
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

**Explicación línea por línea**:
- `URL`: Cadena de conexión JDBC que especifica la base de datos MySQL
- `USER` y `PASS`: Credenciales de acceso
- `conectar()`: Método estático que retorna una `Connection` a la base de datos
- `Class.forName()`: Carga dinámicamente la clase del driver MySQL
- `DriverManager.getConnection()`: Crea la conexión usando la URL y credenciales

#### Ejemplo: `UsuarioDAO.java`

**Métodos principales**:

1. **`listar()`**: Obtiene todos los usuarios
   ```java
   public List<Usuario> listar() {
       List<Usuario> usuarios = new ArrayList<>();
       // Usa try-with-resources para cerrar automáticamente la conexión
       try (Connection con = ConnBD.conectar();
            PreparedStatement ps = con.prepareStatement(BASE_SELECT);
            ResultSet rs = ps.executeQuery()) {
           // Itera sobre cada fila del ResultSet
           while (rs.next()) {
               // Convierte cada fila en un objeto Usuario
               usuarios.add(mapRow(rs));
           }
       }
       return usuarios;
   }
   ```

2. **`guardar(Usuario usuario)`**: Inserta un nuevo usuario
   ```java
   public int guardar(Usuario usuario) {
       String sql = "INSERT INTO usuario (...) VALUES (?,?,...)";
       try (Connection con = ConnBD.conectar();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           // Establece los valores de los parámetros (?) en orden
           ps.setString(1, usuario.getPNombre());
           ps.setString(2, usuario.getSNombre());
           // ... más parámetros
           ps.executeUpdate(); // Ejecuta el INSERT
           // Obtiene el ID generado automáticamente
           ResultSet keys = ps.getGeneratedKeys();
           if (keys.next()) {
               return keys.getInt(1); // Retorna el ID
           }
       }
   }
   ```

3. **`actualizar(Usuario usuario)`**: Actualiza un usuario existente
   ```java
   public boolean actualizar(Usuario usuario) {
       String sql = "UPDATE usuario SET ... WHERE id_usuario=?";
       // Similar a guardar, pero usa UPDATE
       ps.executeUpdate(); // Retorna número de filas afectadas
       return ps.executeUpdate() > 0; // true si se actualizó al menos una fila
   }
   ```

4. **`buscarPorId(int id)`**: Busca un usuario por su ID
   ```java
   public Usuario buscarPorId(int id) {
       String sql = BASE_SELECT + " WHERE id_usuario=?";
       ps.setInt(1, id); // Establece el parámetro
       ResultSet rs = ps.executeQuery();
       if (rs.next()) {
           return mapRow(rs); // Convierte la fila en Usuario
       }
       return null; // No encontrado
   }
   ```

5. **`mapRow(ResultSet rs)`**: Método helper que convierte una fila del ResultSet en un objeto Usuario
   ```java
   private Usuario mapRow(ResultSet rs) throws SQLException {
       Usuario u = new Usuario();
       u.setIdUsuario(rs.getInt("id_usuario"));
       u.setPNombre(rs.getString("p_nombre"));
       // ... más campos
       return u;
   }
   ```

**Patrón DAO**: Cada entidad tiene su DAO correspondiente:
- `RecursoDAO.java` → Operaciones con `recurso`
- `AmbienteDAO.java` → Operaciones con `ambiente`
- `AsistenciaDAO.java` → Operaciones con `registro_asistencia`
- `MinutaDAO.java` → Operaciones con `registro_minuta`
- `IncidenteDAO.java` → Operaciones con `registro_incidente`
- Y así sucesivamente...

---

### 3. PAQUETE `beans` (Managed Beans)

**Propósito**: Actúan como controladores que conectan las vistas (XHTML) con los DAOs y modelos.

#### Características de los Beans:

1. **Anotaciones JSF**:
   - `@ManagedBean(name = "nombreBean")`: Registra el bean en JSF
   - `@ViewScoped`: El bean vive durante la vista (se destruye al cambiar de página)
   - `@SessionScoped`: El bean vive durante la sesión (ejemplo: `LoginBean`)

2. **Ciclo de Vida**:
   - `@PostConstruct`: Método que se ejecuta después de crear el bean
   - Se usa para inicializar datos (cargar listas, etc.)

#### Ejemplo: `LoginBean.java`

**Propósito**: Maneja la autenticación de usuarios.

```java
@ManagedBean(name = "loginBean")
@SessionScoped // Vive durante toda la sesión del usuario
public class LoginBean implements Serializable {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario credenciales = new Usuario(); // Datos del formulario
    private Usuario usuarioAutenticado; // Usuario logueado
    private List<Integer> roles; // Roles del usuario
    
    public String autenticar() {
        // 1. Busca el usuario por documento
        Usuario usuarioBD = usuarioDAO.buscarPorDocumento(credenciales.getDoc());
        
        // 2. Verifica que exista
        if (usuarioBD == null) {
            FacesUtils.addErrorMessage("Usuario no encontrado");
            return null; // No redirige, muestra error
        }
        
        // 3. Verifica la contraseña (usa hash SHA-256)
        if (!PasswordUtil.matches(credenciales.getPass(), usuarioBD.getContrasena())) {
            FacesUtils.addErrorMessage("Contraseña incorrecta");
            return null;
        }
        
        // 4. Login exitoso
        usuarioAutenticado = usuarioBD;
        roles = usuarioDAO.obtenerRolesIdsPorUsuarioId(usuarioBD.getIdUsuario());
        guardarEnSesion(); // Guarda en sesión HTTP
        
        // 5. Redirige según el rol
        return redireccionSegunRol();
    }
    
    private String redireccionSegunRol() {
        if (hasRol(1)) return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        if (hasRol(2)) return "/pages/instructor/index.xhtml?faces-redirect=true";
        if (hasRol(3)) return "/pages/aprendiz/index.xhtml?faces-redirect=true";
        if (hasRol(4)) return "/pages/guarda/index.xhtml?faces-redirect=true";
        return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
    }
}
```

**Explicación**:
- `credenciales`: Objeto que se llena desde el formulario de login
- `usuarioAutenticado`: Usuario que pasó la autenticación
- `autenticar()`: Método llamado desde el formulario XHTML
- Retorna `String` con la ruta de redirección (JSF navigation)
- `?faces-redirect=true`: Fuerza una redirección HTTP (nueva petición)

#### Ejemplo: `RecursoBean.java`

**Propósito**: Maneja CRUD de recursos.

```java
@ManagedBean(name = "recursoBean")
@ViewScoped
public class RecursoBean implements Serializable {
    private RecursoDAO recursoDAO = new RecursoDAO();
    private Recurso recurso = new Recurso(); // Recurso actual (para formulario)
    private List<Recurso> recursos; // Lista de todos los recursos
    private List<Recurso> recursosFiltrados; // Lista filtrada (para tabla)
    private List<TipoRecurso> tipos; // Para dropdown
    private List<Ambiente> ambientes; // Para dropdown
    private Integer idRecursoEditar; // ID del recurso a editar
    
    @PostConstruct
    public void init() {
        // Se ejecuta automáticamente al crear el bean
        recursos = recursoDAO.listar();
        tipos = tipoRecursoDAO.listar();
        ambientes = ambienteDAO.listar();
        cargarRecursoSiEsNecesario(); // Si hay ID en URL, carga el recurso
    }
    
    public String guardar() {
        if (recurso.getIdRecurso() == 0) {
            // Nuevo recurso
            recursoDAO.guardar(recurso);
            FacesUtils.addInfoMessage("Recurso guardado");
        } else {
            // Actualizar recurso existente
            recursoDAO.actualizar(recurso);
            FacesUtils.addInfoMessage("Recurso actualizado");
        }
        recursos = recursoDAO.listar(); // Recarga la lista
        return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
    }
    
    public String editar(int idRecurso) {
        // Redirige a la página de edición con el ID
        return "/pages/admin/editarRecurso.xhtml?id=" + idRecurso + "&faces-redirect=true";
    }
}
```

**Patrón común en Beans**:
1. **Propiedades**: Objeto actual + Lista + Lista filtrada + Listas para dropdowns
2. **@PostConstruct init()**: Carga datos iniciales
3. **guardar()**: Crea o actualiza según el ID
4. **editar()**: Redirige a página de edición con ID en URL
5. **eliminar()**: Elimina y recarga lista

---

### 4. PAQUETE `util` (Utilidades)

**Propósito**: Clases helper reutilizables.

#### `FacesUtils.java`

```java
public final class FacesUtils {
    // Muestra mensajes en la interfaz
    public static void addInfoMessage(String summary) {
        FacesContext.getCurrentInstance()
            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));
    }
    
    // Redirige a otra página
    public static void redirect(String view) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String ctx = ec.getRequestContextPath();
        ec.redirect(ctx + view);
    }
}
```

**Uso**: `FacesUtils.addInfoMessage("Éxito")` muestra un mensaje verde en la página.

#### `PasswordUtil.java`

```java
public final class PasswordUtil {
    // Genera hash SHA-256 de una contraseña
    public static String hash(String plain) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(plain.getBytes(StandardCharsets.UTF_8));
        // Convierte bytes a hexadecimal
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    // Compara contraseña plana con hash
    public static boolean matches(String plain, String hashed) {
        return hash(plain).equals(hashed);
    }
}
```

**Uso**: 
- Al guardar: `usuario.setContrasena(PasswordUtil.hash(contraseñaPlana))`
- Al verificar: `PasswordUtil.matches(contraseñaIngresada, hashAlmacenado)`

---

### 5. PAQUETE `filter` (Filtros)

**Propósito**: Interceptar peticiones HTTP antes de llegar a las páginas.

#### `SessionFilter.java`

```java
@WebFilter(filterName = "SessionFilter", urlPatterns = {"/faces/pages/*"})
public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false); // No crea sesión si no existe
        
        // Verifica si hay usuario autenticado
        boolean isAuthenticated = false;
        if (session != null) {
            Object userId = session.getAttribute("userId");
            if (userId != null) {
                isAuthenticated = true;
            }
        }
        
        // Si no está autenticado, redirige a aviso-logout
        if (!isAuthenticated) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect(contextPath + "/faces/aviso-logout.xhtml");
            return; // No continúa la cadena
        }
        
        // Si está autenticado, continúa a la página solicitada
        chain.doFilter(request, response);
    }
}
```

**Explicación**:
- `@WebFilter`: Anotación que registra el filtro
- `urlPatterns`: URLs que intercepta (`/faces/pages/*`)
- `doFilter()`: Se ejecuta antes de cada petición
- `chain.doFilter()`: Continúa la cadena de filtros (permite acceso)
- Si no llama `chain.doFilter()`, bloquea la petición

---

## FLUJO DE DATOS

### Ejemplo Completo: Registrar un Recurso

1. **Usuario hace clic en "Nuevo Recurso"** en `listarRecursos.xhtml`
   ```xhtml
   <p:commandButton value="Nuevo Recurso" 
                    action="/pages/admin/formRecurso.xhtml?faces-redirect=true"/>
   ```

2. **Navegación a `formRecurso.xhtml`**
   - JSF crea una instancia de `RecursoBean` (si no existe)
   - Ejecuta `@PostConstruct init()`
   - Carga listas: `tipos`, `ambientes`

3. **Usuario llena el formulario**
   ```xhtml
   <p:inputText value="#{recursoBean.recurso.nombre}" />
   <p:selectOneMenu value="#{recursoBean.recurso.idTipoRecurso}">
       <f:selectItems value="#{recursoBean.tipos}" var="tipo" 
                      itemLabel="#{tipo.nombre}" itemValue="#{tipo.idTipoRecurso}"/>
   </p:selectOneMenu>
   ```
   - JSF actualiza `recursoBean.recurso` automáticamente (binding)

4. **Usuario hace clic en "Guardar"**
   ```xhtml
   <p:commandButton value="Guardar" action="#{recursoBean.guardar}"/>
   ```

5. **Ejecución de `recursoBean.guardar()`**
   ```java
   public String guardar() {
       if (recurso.getIdRecurso() == 0) {
           recursoDAO.guardar(recurso); // INSERT en BD
       } else {
           recursoDAO.actualizar(recurso); // UPDATE en BD
       }
       return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
   }
   ```

6. **`recursoDAO.guardar()` ejecuta SQL**
   ```java
   String sql = "INSERT INTO recurso (...) VALUES (?,?,...)";
   PreparedStatement ps = con.prepareStatement(sql);
   ps.setString(1, recurso.getNombre());
   // ... más parámetros
   ps.executeUpdate();
   ```

7. **Redirección a `listarRecursos.xhtml`**
   - JSF navega a la nueva página
   - Se crea nuevo `RecursoBean` (o se reutiliza)
   - `@PostConstruct init()` carga la lista actualizada

---

## EXPLICACIÓN DETALLADA DE ARCHIVOS CLAVE

### 1. `web/WEB-INF/web.xml`

**Propósito**: Configuración de la aplicación web.

```xml
<servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>/faces/*</url-pattern>
</servlet-mapping>
```

**Explicación**:
- Registra el servlet de JSF
- Todas las URLs que empiecen con `/faces/` son manejadas por JSF
- `load-on-startup`: Carga el servlet al iniciar el servidor

```xml
<filter>
    <filter-name>SessionFilter</filter-name>
    <filter-class>filter.SessionFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>SessionFilter</filter-name>
    <url-pattern>/faces/pages/*</url-pattern>
</filter-mapping>
```

**Explicación**:
- Registra el filtro de sesión
- Intercepta todas las URLs `/faces/pages/*`

---

### 2. `web/pages/layout/template.xhtml`

**Propósito**: Plantilla base para todas las páginas.

```xhtml
<ui:composition template="/pages/layout/template.xhtml">
    <ui:define name="content">
        <!-- Contenido específico de la página -->
    </ui:define>
</ui:composition>
```

**Estructura**:
- **Sidebar**: Menú de navegación (izquierda)
- **Header**: Barra superior con usuario y logout
- **Content**: Área donde se inserta el contenido de cada página

**Componentes PrimeFaces usados**:
- `p:menu`: Menú de navegación
- `p:commandLink`: Enlaces que ejecutan acciones
- `p:outputLabel`: Etiquetas de texto

---

### 3. `web/login.xhtml`

**Propósito**: Página de inicio de sesión.

```xhtml
<h:form id="loginForm">
    <p:inputText value="#{loginBean.credenciales.doc}" 
                 required="true"/>
    <p:password value="#{loginBean.credenciales.pass}" 
                required="true"/>
    <p:commandButton value="Iniciar Sesión" 
                     action="#{loginBean.autenticar}"/>
</h:form>
```

**Explicación**:
- `h:form`: Formulario JSF (envuelve los campos)
- `p:inputText`: Campo de texto (documento)
- `p:password`: Campo de contraseña (oculta caracteres)
- `value="#{loginBean.credenciales.doc}"`: Binding bidireccional
  - Al cargar: muestra el valor de `credenciales.doc`
  - Al enviar: actualiza `credenciales.doc` con lo ingresado
- `action="#{loginBean.autenticar}"`: Ejecuta el método al hacer clic
- `required="true"`: Validación JSF (no permite enviar vacío)

---

### 4. `web/pages/admin/listarRecursos.xhtml`

**Propósito**: Lista todos los recursos en una tabla.

```xhtml
<p:dataTable value="#{recursoBean.recursos}" 
             var="rec" 
             filteredValue="#{recursoBean.recursosFiltrados}"
             paginator="true" 
             rows="10">
    <p:column headerText="ID" filterBy="#{rec.idRecurso}">
        <h:outputText value="#{rec.idRecurso}"/>
    </p:column>
    <p:column headerText="Nombre" filterBy="#{rec.nombre}" filterMatchMode="contains">
        <h:outputText value="#{rec.nombre}"/>
    </p:column>
    <p:column>
        <p:commandButton value="Editar" 
                         action="#{recursoBean.editar(rec.idRecurso)}"/>
    </p:column>
</p:dataTable>
```

**Explicación**:
- `p:dataTable`: Tabla de PrimeFaces
- `value="#{recursoBean.recursos}"`: Lista de datos
- `var="rec"`: Variable que representa cada fila
- `filteredValue`: Lista donde PrimeFaces guarda los resultados filtrados
- `paginator="true"`: Habilita paginación
- `rows="10"`: 10 filas por página
- `filterBy`: Habilita filtro en esa columna
- `filterMatchMode="contains"`: Tipo de filtro (contiene texto)
- `action="#{recursoBean.editar(rec.idRecurso)}"`: Pasa el ID como parámetro

**Exportación PDF/Excel**:
```xhtml
<p:dataExporter type="pdf" target="tablaRecursos" fileName="recursos"/>
<p:dataExporter type="xls" target="tablaRecursos" fileName="recursos"/>
```

**Explicación**:
- `p:dataExporter`: Componente de PrimeFaces para exportar
- `type`: Formato (pdf o xls)
- `target`: ID de la tabla a exportar
- `fileName`: Nombre del archivo generado

---

### 5. `web/pages/admin/formRecurso.xhtml`

**Propósito**: Formulario para crear/editar recursos.

```xhtml
<f:viewParam name="id" value="#{recursoBean.idRecursoEditar}"/>
<f:viewAction action="#{recursoBean.cargarRecursoParaEditar}"/>

<h:form>
    <p:inputText value="#{recursoBean.recurso.nombre}" 
                 required="true"
                 requiredMessage="El nombre es requerido"/>
    <p:selectOneMenu value="#{recursoBean.recurso.idTipoRecurso}">
        <f:selectItem itemLabel="Seleccione..." itemValue="0"/>
        <f:selectItems value="#{recursoBean.tipos}" 
                       var="tipo"
                       itemLabel="#{tipo.nombre}" 
                       itemValue="#{tipo.idTipoRecurso}"/>
    </p:selectOneMenu>
    <p:commandButton value="Guardar" action="#{recursoBean.guardar}"/>
</h:form>
```

**Explicación**:
- `f:viewParam`: Captura parámetro de la URL (`?id=5`)
- `f:viewAction`: Ejecuta método después de procesar parámetros
- `p:selectOneMenu`: Dropdown/select
- `f:selectItems`: Itera sobre lista y crea opciones
- `itemLabel`: Texto visible
- `itemValue`: Valor seleccionado (se guarda en `recurso.idTipoRecurso`)

---

## FUNCIONALIDADES POR MÓDULO

### MÓDULO ADMINISTRADOR

**Beans**: `UsuarioBean`, `RecursoBean`, `AmbienteBean`, `ProgramaBean`, `FichaBean`

**Funcionalidades**:
1. **Gestión de Usuarios** (`UsuarioBean`)
   - Crear usuarios (Aprendiz, Instructor, Coordinador, Guarda)
   - Asignar roles
   - Editar y eliminar usuarios
   - Listar con filtros

2. **Gestión de Recursos** (`RecursoBean`)
   - CRUD completo
   - Asignar a ambientes
   - Filtrar y exportar

3. **Gestión de Ambientes** (`AmbienteBean`)
   - CRUD completo
   - Filtrar y exportar

4. **Gestión de Programas y Fichas** (`ProgramaBean`, `FichaBean`)
   - Crear programas de formación
   - Crear fichas (grupos de aprendices)

---

### MÓDULO INSTRUCTOR

**Beans**: `AsistenciaBean`, `IncidenteBean`, `MinutaBean`, `TrasladoRecursoBean`

**Funcionalidades**:
1. **Registro de Asistencias** (`AsistenciaBean`)
   - Registrar asistencia/inasistencia de aprendices
   - Filtrar por fecha, aprendiz, jornada
   - Exportar reportes

2. **Registro de Incidentes** (`IncidenteBean`)
   - Reportar incidentes en ambientes
   - Asociar tipo de incidente
   - Filtrar y exportar

3. **Consulta de Minutas** (`MinutaBean`)
   - Ver minutas registradas por guardas
   - Filtrar por ambiente, fecha, estado
   - Exportar

4. **Consulta de Traslados** (`TrasladoRecursoBean`)
   - Ver traslados de recursos entre ambientes
   - Filtrar y exportar

---

### MÓDULO GUARDA DE SEGURIDAD

**Beans**: `MinutaBean`, `TrasladoRecursoBean`, `IncidenteBean`

**Funcionalidades**:
1. **Registro de Minutas** (`MinutaBean`)
   - Registrar minutas de entrega/recibo
   - Asociar instructor responsable
   - Filtrar y exportar

2. **Registro de Traslados** (`TrasladoRecursoBean`)
   - Registrar traslado de recursos
   - Especificar ambiente origen y destino
   - Filtrar y exportar

3. **Consulta de Incidentes** (`IncidenteBean`)
   - Ver incidentes reportados
   - Filtrar y exportar

---

### MÓDULO APRENDIZ

**Beans**: `AsistenciaAprendizBean`

**Funcionalidades**:
1. **Consulta de Asistencias** (`AsistenciaAprendizBean`)
   - Ver sus propias asistencias
   - Filtrar por fecha
   - Exportar

---

## CONCEPTOS CLAVE

### 1. Expression Language (EL)

**Sintaxis**: `#{bean.propiedad}` o `#{bean.metodo()}`

**Ejemplos**:
- `#{recursoBean.recursos}`: Accede a la propiedad `recursos` del bean
- `#{rec.nombre}`: Accede a `nombre` del objeto `rec` (variable de iteración)
- `#{recursoBean.guardar()}`: Ejecuta el método `guardar()`

### 2. Binding Bidireccional

```xhtml
<p:inputText value="#{recursoBean.recurso.nombre}"/>
```

- **Al cargar**: Muestra el valor de `recurso.nombre`
- **Al enviar**: Actualiza `recurso.nombre` con lo ingresado

### 3. Navegación JSF

**Retorno de String**:
```java
public String guardar() {
    return "/pages/admin/listarRecursos.xhtml?faces-redirect=true";
}
```

- `faces-redirect=true`: Fuerza redirección HTTP (nueva petición)
- Sin `faces-redirect`: Forward interno (misma petición)

**Con parámetros**:
```java
public String editar(int id) {
    return "/pages/admin/editarRecurso.xhtml?id=" + id + "&faces-redirect=true";
}
```

### 4. Scope de Beans

- **@ViewScoped**: Vive durante la vista (se destruye al cambiar de página)
- **@SessionScoped**: Vive durante la sesión (ejemplo: `LoginBean`)
- **@RequestScoped**: Vive durante una petición HTTP

### 5. Filtros y Exportación

**Filtros en tablas**:
- `filterBy`: Propiedad por la que filtrar
- `filterMatchMode`: Tipo de filtro (`contains`, `equals`, `startsWith`, etc.)
- `filteredValue`: Lista donde se guardan los resultados filtrados

**Exportación**:
- Requiere librerías: `itext-2.1.7.jar` (PDF) y `poi-3.16.jar` (Excel)
- `exportValue`: Valor específico para exportar (útil cuando hay HTML en la celda)

---

## RESUMEN

Este proyecto es una **aplicación web Java EE** que gestiona:
- Usuarios (Aprendices, Instructores, Coordinadores, Guardas)
- Recursos (equipos, materiales)
- Ambientes (aulas, laboratorios)
- Asistencias
- Incidentes
- Minutas
- Traslados

**Arquitectura**:
- **Vista (XHTML)**: Interfaz de usuario con PrimeFaces
- **Controlador (Beans)**: Lógica de negocio
- **Modelo (POJOs)**: Entidades de datos
- **DAO**: Acceso a base de datos

**Flujo típico**:
1. Usuario interactúa con XHTML
2. XHTML llama método del Bean
3. Bean usa DAO para acceder a BD
4. DAO retorna datos al Bean
5. Bean actualiza propiedades
6. XHTML muestra datos actualizados

---

**FIN DE LA GUÍA**

