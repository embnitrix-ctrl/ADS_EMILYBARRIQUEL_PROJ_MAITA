package br.upf.ads_emilybarriquel_proj_maita.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "apartamento")
public class ApartamentoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "andar_id", nullable = false)
    private AndarEntity andar;

    @OneToMany(mappedBy = "apartamento", fetch = FetchType.LAZY)
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ApartamentoEntity other = (ApartamentoEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Apto " + numero;
    }
}
