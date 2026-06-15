package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.FuncionarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private FuncionarioFacade ejbFacade;

    private FuncionarioEntity funcionario;
    private FuncionarioEntity funcionarioLogado;

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
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        FuncionarioEntity funcionarioDB =
                ejbFacade.buscarPorEmail(
                        funcionario.getEmail(),
                        funcionario.getSenha());

        if (funcionarioDB != null) {

            funcionarioLogado = funcionarioDB;

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getSession(true);

            session.setAttribute("funcionarioLogado", funcionarioDB);

            return "/admin/index.xhtml?faces-redirect=true";
        }

        FacesMessage fm = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Falha no Login!",
                "Email ou senha incorretos!");

        FacesContext.getCurrentInstance().addMessage(null, fm);

        return null;
    }

    public String logout() {

        funcionarioLogado = null;

        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "/login.xhtml?faces-redirect=true";
    }

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }

    public FuncionarioEntity getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public void setFuncionarioLogado(FuncionarioEntity funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }
}