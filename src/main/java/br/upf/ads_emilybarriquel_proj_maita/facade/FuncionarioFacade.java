package br.upf.ads_emilybarriquel_proj_maita.facade;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FuncionarioFacade extends AbstractFacade<FuncionarioEntity>{

    @PersistenceContext(unitName = "MaitaPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
     public FuncionarioFacade() {
        super(FuncionarioEntity.class);
     }
      private List<FuncionarioEntity> entityList;

    public List<FuncionarioEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {       
            Query query = getEntityManager().createQuery("SELECT f FROM FuncionarioEntity f ORDER BY f.nome");
            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                entityList = (List<FuncionarioEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    /**
     * Buscar uma funcionario por email
     * @param email
     * @param senha
     * @return 
     */
    public FuncionarioEntity buscarPorEmail(String email, String senha) {
        FuncionarioEntity funcionario = new FuncionarioEntity();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager()
                    .createQuery("SELECT f FROM FuncionarioEntity f WHERE f.email = :email AND f.senha = :senha");
            query.setParameter("email", email);
            query.setParameter("senha", senha);

            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                funcionario = (FuncionarioEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return funcionario;
    }
}

    
   