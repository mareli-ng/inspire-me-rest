package it.mdg.inspireme.entities;

import java.io.Serializable;

import lombok.Data;
@Data
public class RiferimentoTagId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private Integer idRiferimento;
    private Integer idTag;

    public RiferimentoTagId() {}

    public RiferimentoTagId(Integer idRiferimento, Integer idTag) {
        this.idRiferimento = idRiferimento;
        this.idTag = idTag;
    }

}
