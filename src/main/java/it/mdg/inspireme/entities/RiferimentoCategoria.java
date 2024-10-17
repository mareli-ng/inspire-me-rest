package it.mdg.inspireme.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "riferimenti_categorie")
@IdClass(RiferimentoCategoriaId.class)
public class RiferimentoCategoria implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private Integer idRiferimento;
    @Id
    private Integer idCategoria;

}