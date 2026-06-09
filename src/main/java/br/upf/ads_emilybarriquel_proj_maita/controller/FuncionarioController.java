package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.FuncionarioFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "funcionarioController")
@SessionScoped
public class FuncionarioController implements Serializable {

    // Aqui está a injeção correta apontando para o seu Facade real
    @EJB
    private FuncionarioFacade ejbFacade;

    private FuncionarioEntity funcionario = new FuncionarioEntity();
    private List<FuncionarioEntity> funcionarioList = new ArrayList<>();
    private FuncionarioEntity selected;

    public FuncionarioEntity getSelected() {
        return selected;
    }

    public void setSelected(FuncionarioEntity selected) {
        this.selected = selected;
    }

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }

    // Chamando o método buscarTodos() que você programou no Facade
    public List<FuncionarioEntity> getFuncionarioList() {
        return ejbFacade.buscarTodos();
    }

    public void setFuncionarioList(List<FuncionarioEntity> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public FuncionarioEntity prepareAdicionar() {
        funcionario = new FuncionarioEntity();
        return funcionario;
    }

    public void adicionarFuncionario() {
        persist(PersistAction.CREATE, "Registro incluído com sucesso!");
    }

    public void editarFuncionario() {
        persist(PersistAction.UPDATE, "Registro alterado com sucesso!");
    }

    public void deletarFuncionario() {
        persist(PersistAction.DELETE, "Registro excluído com sucesso!");
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
        CREATE,
        DELETE,
        UPDATE
    }

    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (persistAction != null) {
                switch (persistAction) {
                    case CREATE:
                        // Se o seu AbstractFacade exigir "createReturn", troque "create" por "createReturn"
                        ejbFacade.create(funcionario);
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