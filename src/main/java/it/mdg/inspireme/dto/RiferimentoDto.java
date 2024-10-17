package it.mdg.inspireme.dto;

import java.util.List;

import lombok.Data;

@Data
public class RiferimentoDto {
	private Integer id;
	private String anteprima;
	private String titolo;
	private String descrizione;
	private String url;
	private String origine;
	private List<Integer> categorieIds;
}
