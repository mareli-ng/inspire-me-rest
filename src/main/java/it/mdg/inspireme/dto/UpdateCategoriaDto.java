package it.mdg.inspireme.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateCategoriaDto {
	private List<Integer> idRiferimenti;
	private Integer idOld;
	private List<Integer> idNew;
}
