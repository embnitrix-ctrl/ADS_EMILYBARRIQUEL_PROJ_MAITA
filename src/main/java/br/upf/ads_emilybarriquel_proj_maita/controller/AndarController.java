package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.AndarEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.AndarFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named(value = "andarController")
@SessionScoped
public class AndarController implements Serializable {

    @EJB
    private AndarFacade ejbFacade;

    private AndarEntity andar = new AndarEntity();
    private AndarEntity selected;

    public AndarEntity getAndar() {
        return andar;
    }

    public void setAndar(AndarEntity andar) {
        this.andar = andar;
    }

    public AndarEntity getSelected() {
        return selected;
    }

    public void setSelected(AndarEntity selected) {
        this.selected = selected;
    }

    public List<AndarEntity> getAndarList() {
        return ejbFacade.buscarTodos();
    }

    public AndarEntity prepareAdicionar() {
        andar = new AndarEntity();
        return andar;
    }

    public void adicionarAndar() {
        persist(PersistAction.CREATE, "Andar cadastrado com sucesso!");
    }

    public void editarAndar() {
        persist(PersistAction.UPDATE, "Andar alterado com sucesso!");
    }

    public void deletarAndar() {
        persist(PersistAction.DELETE, "Andar excluído com sucesso!");
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static enum PersistAction {
        CREATE, DELETE, UPDATE
    }

    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (persistAction != null) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.create(andar);
                        break;
                    case UPDATE:
                        ejbFacade.edit(selected);
                        selected = null;
                        break;
                    case DELETE:
                        ejbFacade.remove(selected);
                        selected = null;
                        break;
                }
            }
            addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                addErrorMessage(msg);
            } else {
                addErrorMessage(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage());
        }
    }
}
