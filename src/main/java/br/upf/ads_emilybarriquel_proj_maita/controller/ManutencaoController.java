package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.ManutencaoEntity;
import br.upf.ads_emilybarriquel_proj_maita.entity.ApartamentoEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.ManutencaoFacade;
import br.upf.ads_emilybarriquel_proj_maita.facade.ApartamentoFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named(value = "manutencaoController")
@SessionScoped
public class ManutencaoController implements Serializable {

    @EJB
    private ManutencaoFacade ejbFacade;

    @EJB
    private ApartamentoFacade apartamentoFacade;

    private ManutencaoEntity manutencao = new ManutencaoEntity();
    private ManutencaoEntity selected;

    // Apartamento selecionado para filtrar manutenções
    private ApartamentoEntity apartamentoFiltro;

    public ManutencaoEntity getManutencao() {
        return manutencao;
    }

    public void setManutencao(ManutencaoEntity manutencao) {
        this.manutencao = manutencao;
    }

    public ManutencaoEntity getSelected() {
        return selected;
    }

    public void setSelected(ManutencaoEntity selected) {
        this.selected = selected;
    }

    public ApartamentoEntity getApartamentoFiltro() {
        return apartamentoFiltro;
    }

    public void setApartamentoFiltro(ApartamentoEntity apartamentoFiltro) {
        this.apartamentoFiltro = apartamentoFiltro;
    }

    /**
     * Lista todas as manutenções (sem filtro).
     */
    public List<ManutencaoEntity> getManutencaoList() {
        return ejbFacade.buscarTodos();
    }

    /**
     * Lista manutenções filtradas pelo apartamento selecionado.
     */
    public List<ManutencaoEntity> getManutencaoListByApartamento() {
        if (apartamentoFiltro != null && apartamentoFiltro.getId() != null) {
            return ejbFacade.buscarPorApartamento(apartamentoFiltro);
        }
        return ejbFacade.buscarTodos();
    }

    /**
     * Retorna lista de apartamentos para o selectOneMenu.
     */
    public List<ApartamentoEntity> getApartamentoList() {
        return apartamentoFacade.buscarTodos();
    }

    public ManutencaoEntity prepareAdicionar() {
        manutencao = new ManutencaoEntity();
        return manutencao;
    }

    /**
     * Abre um chamado de manutenção: define status ABERTO e data de abertura.
     */
    public void abrirChamado() {
        manutencao.setStatus(ManutencaoEntity.STATUS_ABERTO);
        manutencao.setDataAbertura(new Date());
        manutencao.setDataFechamento(null);
        persist(PersistAction.CREATE, "Chamado aberto com sucesso!");
    }

    /**
     * Fecha o chamado selecionado: define status FECHADO e data de fechamento.
     */
    public void fecharChamado() {
        if (selected != null) {
            selected.setStatus(ManutencaoEntity.STATUS_FECHADO);
            selected.setDataFechamento(new Date());
            persist(PersistAction.UPDATE, "Chamado fechado com sucesso!");
        } else {
            addErrorMessage("Nenhuma manutenção selecionada!");
        }
    }

    /**
     * Reabre um chamado fechado.
     */
    public void reabrirChamado() {
        if (selected != null) {
            selected.setStatus(ManutencaoEntity.STATUS_ABERTO);
            selected.setDataFechamento(null);
            selected.setObservacaoFechamento(null);
            persist(PersistAction.UPDATE, "Chamado reaberto com sucesso!");
        } else {
            addErrorMessage("Nenhuma manutenção selecionada!");
        }
    }

    public void editarManutencao() {
        persist(PersistAction.UPDATE, "Manutenção alterada com sucesso!");
    }

    public void deletarManutencao() {
        persist(PersistAction.DELETE, "Manutenção excluída com sucesso!");
    }

    public void filtrarPorApartamento() {
        // A lista é atualizada via getter
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
                        ejbFacade.create(manutencao);
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
