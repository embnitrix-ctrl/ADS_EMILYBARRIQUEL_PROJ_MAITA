package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.ApartamentoEntity;
import br.upf.ads_emilybarriquel_proj_maita.entity.AndarEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.ApartamentoFacade;
import br.upf.ads_emilybarriquel_proj_maita.facade.AndarFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named(value = "apartamentoController")
@SessionScoped
public class ApartamentoController implements Serializable {

    @EJB
    private ApartamentoFacade ejbFacade;

    @EJB
    private AndarFacade andarFacade;

    private ApartamentoEntity apartamento = new ApartamentoEntity();
    private ApartamentoEntity selected;

    // Andar selecionado para filtrar a listagem
    private AndarEntity andarFiltro;

    public ApartamentoEntity getApartamento() {
        return apartamento;
    }

    public void setApartamento(ApartamentoEntity apartamento) {
        this.apartamento = apartamento;
    }

    public ApartamentoEntity getSelected() {
        return selected;
    }

    public void setSelected(ApartamentoEntity selected) {
        this.selected = selected;
    }

    public AndarEntity getAndarFiltro() {
        return andarFiltro;
    }

    public void setAndarFiltro(AndarEntity andarFiltro) {
        this.andarFiltro = andarFiltro;
    }

    /**
     * Lista todos os apartamentos (sem filtro).
     */
    public List<ApartamentoEntity> getApartamentoList() {
        return ejbFacade.buscarTodos();
    }

    /**
     * Lista apartamentos filtrados pelo andar selecionado.
     */
    public List<ApartamentoEntity> getApartamentoListByAndar() {
        if (andarFiltro != null && andarFiltro.getId() != null) {
            return ejbFacade.buscarPorAndar(andarFiltro);
        }
        return ejbFacade.buscarTodos();
    }

    /**
     * Retorna todos os andares disponíveis para o selectOneMenu.
     */
    public List<AndarEntity> getAndarList() {
        return andarFacade.buscarTodos();
    }

    public ApartamentoEntity prepareAdicionar() {
        apartamento = new ApartamentoEntity();
        return apartamento;
    }

    public void adicionarApartamento() {
        persist(PersistAction.CREATE, "Apartamento cadastrado com sucesso!");
    }

    public void editarApartamento() {
        persist(PersistAction.UPDATE, "Apartamento alterado com sucesso!");
    }

    public void deletarApartamento() {
        persist(PersistAction.DELETE, "Apartamento excluído com sucesso!");
    }

    /**
     * Filtra a tabela pelo andar escolhido no selectOneMenu.
     */
    public void filtrarPorAndar() {
        // A lista será atualizada via getter getApartamentoListByAndar()
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
                        ejbFacade.create(apartamento);
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
