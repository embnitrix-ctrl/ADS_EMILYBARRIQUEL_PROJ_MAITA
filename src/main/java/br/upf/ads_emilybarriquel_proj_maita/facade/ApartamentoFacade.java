package br.upf.ads_emilybarriquel_proj_maita.facade;

import br.upf.ads_emilybarriquel_proj_maita.entity.ApartamentoEntity;
import br.upf.ads_emilybarriquel_proj_maita.entity.AndarEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ApartamentoFacade extends AbstractFacade<ApartamentoEntity> {

    @PersistenceContext(unitName = "MaitaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApartamentoFacade() {
        super(ApartamentoEntity.class);
    }

    public List<ApartamentoEntity> buscarTodos() {
        List<ApartamentoEntity> lista = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery(
                "SELECT a FROM ApartamentoEntity a ORDER BY a.andar.numero, a.numero");
            if (!query.getResultList().isEmpty()) {
                lista = (List<ApartamentoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar apartamentos: " + e);
        }
        return lista;
    }

    /**
     * Busca apartamentos de um andar específico.
     */
    public List<ApartamentoEntity> buscarPorAndar(AndarEntity andar) {
        List<ApartamentoEntity> lista = new ArrayList<>();
        try {
            if (andar == null || andar.getId() == null) return lista;
            Query query = getEntityManager().createQuery(
                "SELECT a FROM ApartamentoEntity a WHERE a.andar = :andar ORDER BY a.numero");
            query.setParameter("andar", andar);
            if (!query.getResultList().isEmpty()) {
                lista = (List<ApartamentoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar apartamentos por andar: " + e);
        }
        return lista;
    }
}
