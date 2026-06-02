package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {


    public LoginController() {
    }

    private FuncionarioEntity funcionario;

    
    public void prepareAutenticarFuncionario() {
        funcionario = new FuncionarioEntity();
    }

    /**
     * Método utilizado para inicializar métodos ao instanciar a classe...
     */
    @PostConstruct
    public void init() {
        prepareAutenticarFuncionario();
    }

    public String validarLogin() {
        if (funcionario.getEmail().equals("EMILY@gmail.com")
                && funcionario.getSenha().equals("123456")) {
           
            return "/index.xhtml?faces-redirect=true";
        } else {
            FacesMessage fm = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Falha no Login!",
                    "Email ou senha incorreto!");
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return null;
        }
    }

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }

}

    