package it.mdg.inspireme.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "riferimenti_tag")
@IdClass(RiferimentoTagId.class)
public class RiferimentoTag implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private Integer idRiferimento;
    @Id
    private Integer idTag;

}