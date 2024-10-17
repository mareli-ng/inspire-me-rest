package it.mdg.inspireme.entities;

import java.io.Serializable;

import lombok.Data;
@Data
public class RiferimentoCategoriaId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private Integer idRiferimento;
    private Integer idCategoria;

    public RiferimentoCategoriaId() {}

    public RiferimentoCategoriaId(Integer idRiferimento, Integer idCategoria) {
        this.idRiferimento = idRiferimento;
        this.idCategoria = idCategoria;
    }

}
