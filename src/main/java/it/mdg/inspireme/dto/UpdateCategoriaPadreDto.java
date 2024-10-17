package it.mdg.inspireme.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateCategoriaPadreDto {
	private List<Integer> idCategorie;
	private Integer idCategoriaPadre;
}
