package br.upf.ads_emilybarriquel_proj_maita.facade;

import br.upf.ads_emilybarriquel_proj_maita.entity.ManutencaoEntity;
import br.upf.ads_emilybarriquel_proj_maita.entity.ApartamentoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ManutencaoFacade extends AbstractFacade<ManutencaoEntity> {

    @PersistenceContext(unitName = "MaitaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManutencaoFacade() {
        super(ManutencaoEntity.class);
    }

    public List<ManutencaoEntity> buscarTodos() {
        List<ManutencaoEntity> lista = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery(
                "SELECT m FROM ManutencaoEntity m ORDER BY m.dataAbertura DESC");
            if (!query.getResultList().isEmpty()) {
                lista = (List<ManutencaoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar manutenções: " + e);
        }
        return lista;
    }

    /**
     * Busca manutenções de um apartamento específico.
     */
    public List<ManutencaoEntity> buscarPorApartamento(ApartamentoEntity apartamento) {
        List<ManutencaoEntity> lista = new ArrayList<>();
        try {
            if (apartamento == null || apartamento.getId() == null) return lista;
            Query query = getEntityManager().createQuery(
                "SELECT m FROM ManutencaoEntity m WHERE m.apartamento = :apartamento ORDER BY m.dataAbertura DESC");
            query.setParameter("apartamento", apartamento);
            if (!query.getResultList().isEmpty()) {
                lista = (List<ManutencaoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar manutenções por apartamento: " + e);
        }
        return lista;
    }

    /**
     * Busca manutenções abertas.
     */
    public List<ManutencaoEntity> buscarAbertas() {
        List<ManutencaoEntity> lista = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery(
                "SELECT m FROM ManutencaoEntity m WHERE m.status = 'ABERTO' ORDER BY m.dataAbertura DESC");
            if (!query.getResultList().isEmpty()) {
                lista = (List<ManutencaoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar manutenções abertas: " + e);
        }
        return lista;
    }
}
