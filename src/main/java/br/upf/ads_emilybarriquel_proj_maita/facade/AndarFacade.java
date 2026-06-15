package br.upf.ads_emilybarriquel_proj_maita.facade;

import br.upf.ads_emilybarriquel_proj_maita.entity.AndarEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AndarFacade extends AbstractFacade<AndarEntity> {

    @PersistenceContext(unitName = "MaitaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AndarFacade() {
        super(AndarEntity.class);
    }

    public List<AndarEntity> buscarTodos() {
        List<AndarEntity> lista = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery(
                "SELECT a FROM AndarEntity a ORDER BY a.numero");
            if (!query.getResultList().isEmpty()) {
                lista = (List<AndarEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar andares: " + e);
        }
        return lista;
    }
}
