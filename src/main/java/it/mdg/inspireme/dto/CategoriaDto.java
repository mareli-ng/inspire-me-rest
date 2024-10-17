package it.mdg.inspireme.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaDto {
    private Integer id;
    private String nome;
    private Integer idCategoriaPadre;
    private List<CategoriaDto> sottoCategorie;
}
