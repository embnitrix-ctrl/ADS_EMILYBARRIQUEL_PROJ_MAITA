package br.upf.ads_emilybarriquel_proj_maita.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "andar")
public class AndarEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @OneToMany(mappedBy = "andar", fetch = FetchType.LAZY)
    private List<ApartamentoEntity> apartamentos;

    public AndarEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public List<ApartamentoEntity> getApartamentos() {
        return apartamentos;
    }

    public void setApartamentos(List<ApartamentoEntity> apartamentos) {
        this.apartamentos = apartamentos;
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
        final AndarEntity other = (AndarEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return nome != null ? numero + "º Andar - " + nome : "";
    }
}
