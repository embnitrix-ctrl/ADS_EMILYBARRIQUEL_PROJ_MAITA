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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "manutencao")
public class ManutencaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_ABERTO   = "ABERTO";
    public static final String STATUS_FECHADO  = "FECHADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "tipo", length = 100)
    private String tipo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_abertura")
    private Date dataAbertura;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_fechamento")
    private Date dataFechamento;

    @Column(name = "observacao_fechamento", length = 500)
    private String observacaoFechamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartamento_id", nullable = false)
    private ApartamentoEntity apartamento;

    public ManutencaoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getObservacaoFechamento() {
        return observacaoFechamento;
    }

    public void setObservacaoFechamento(String observacaoFechamento) {
        this.observacaoFechamento = observacaoFechamento;
    }

    public ApartamentoEntity getApartamento() {
        return apartamento;
    }

    public void setApartamento(ApartamentoEntity apartamento) {
        this.apartamento = apartamento;
    }

    public boolean isAberto() {
        return STATUS_ABERTO.equals(this.status);
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
        final ManutencaoEntity other = (ManutencaoEntity) obj;
        return Objects.equals(this.id, other.id);
    }
}
