package br.upf.ads_emilybarriquel_proj_maita.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "apartamento")
public class ApartamentoEntity implements Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "andar_id", nullable = false)
    private AndarEntity andar;

    @OneToMany(mappedBy = "apartamento")
    private List<ManutencaoEntity> manutencoes;

    public ApartamentoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public AndarEntity getAndar() {
        return andar;
    }

    public void setAndar(AndarEntity andar) {
        this.andar = andar;
    }

    public List<ManutencaoEntity> getManutencoes() {
        return manutencoes;
    }

    public void setManutencoes(List<ManutencaoEntity> manutencoes) {
        this.manutencoes = manutencoes;
    }
}