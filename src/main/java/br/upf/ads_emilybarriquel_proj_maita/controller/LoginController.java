package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private br.upf.ads_emilybarriquel_proj_maita.facade.FuncionarioFacade ejbFacade;

    private FuncionarioEntity funcionario;

    public LoginController() {
    }

    @PostConstruct
    public void init() {
        prepareAutenticarFuncionario();
    }

    public void prepareAutenticarFuncionario() {
        funcionario = new FuncionarioEntity();
    }

    public String validarLogin() {

        FuncionarioEntity funcionarioDB =
                ejbFacade.buscarPorEmail(
                        funcionario.getEmail(),
                        funcionario.getSenha());

        if (funcionarioDB != null) {
            return "/index.xhtml?faces-redirect=true";
        }

        FacesMessage fm = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Falha no Login!",
                "Email ou senha incorreto!");

        FacesContext.getCurrentInstance().addMessage(null, fm);

        return null;
    }

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }
}