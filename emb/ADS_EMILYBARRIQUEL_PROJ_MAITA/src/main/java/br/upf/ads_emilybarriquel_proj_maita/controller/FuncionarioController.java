package br.upf.ads_emilybarriquel_proj_maita.controller;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
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

    public FuncionarioController() {
    }
    
    FuncionarioEntity funcionario = new FuncionarioEntity();

    public FuncionarioEntity getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioEntity funcionario) {
        this.funcionario = funcionario;
    }

    public List<FuncionarioEntity> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<FuncionarioEntity> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }
    
    private List<FuncionarioEntity> funcionarioList = new ArrayList<>();
    
     private int gerarId() {
        int id = 1;
        if (!funcionarioList.isEmpty()) {
            id = funcionarioList.size() + 1;
        }
        return id;
    }
    private void exibirMensagem() {
        String msg = "funcionario criado com sucesso: " + funcionario.getNome();
        FacesMessage fm = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(msg, fm);
    }

    public void adicionarFuncionario() {
        funcionario.setId(gerarId());
        funcionarioList.add(funcionario);
        exibirMensagem();
        funcionario = new FuncionarioEntity();
    }
}
