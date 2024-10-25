package it.mdg.inspireme.dto;

import java.util.List;

import lombok.Data;

@Data
public class SuggestionFilterDto {
	private List<Integer> categorie;
	private List<String> tag;
    private List<SelectItem> origini;
}
