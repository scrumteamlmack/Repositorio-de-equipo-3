package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

public final class FacesUtils implements Serializable {

    private FacesUtils() {
    }

    public static void addInfoMessage(String summary) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, null);
    }

    public static void addWarnMessage(String summary) {
        addMessage(FacesMessage.SEVERITY_WARN, summary, null);
    }

    public static void addErrorMessage(String summary) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, null);
    }

    public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void redirect(String view) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String ctx = externalContext.getRequestContextPath();
        try {
            externalContext.redirect(ctx + view);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible redirigir a " + view, e);
        }
    }
}

