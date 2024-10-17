package it.mdg.inspireme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.mdg.inspireme.dto.CategoriaDto;
import it.mdg.inspireme.dto.UpdateCategoriaPadreDto;
import it.mdg.inspireme.entities.Categoria;
import it.mdg.inspireme.services.CategoriaService;

@RestController
public class CategorieController {

	@Autowired
	private CategoriaService categoriaService;

	@PostMapping("/categoria/new")
	public CategoriaDto addCategoria(@RequestBody Categoria categoria) {
		return categoriaService.addCategoria(categoria);
	}

	@PutMapping("/categoria/{id}")
	public CategoriaDto updateCategoria(@PathVariable Integer id,
			@RequestBody Categoria categoriaDetails) {
		return categoriaService.updateCategoria(id, categoriaDetails);
	}
	
	@DeleteMapping("/categorie/delete")
    public void deleteCategorie(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
        	categoriaService.deleteCategoria(id);
        }
    }

	@DeleteMapping("/categoria/{id}")
	public void deleteCategoria(@PathVariable Integer id) {
		categoriaService.deleteCategoria(id);
	}
	
	@PostMapping("/update/categoriapadre")
	public void updateCategoriaPadre(@RequestBody UpdateCategoriaPadreDto dto) {
		categoriaService.updateCategoriaPadre(dto);
	}

	@GetMapping("/categorie/albero")
	public List<CategoriaDto> getAlberoCategorie() {
		List<CategoriaDto> categorie = categoriaService.getAlberoCategorie();
		if (categorie.isEmpty()) {
			System.out.println("Nessuna categoria trovata.");
		}
		return categorie;
	}
	
	@GetMapping("/categorie/root")
	public List<CategoriaDto> getCategorieRoot() {
		return categoriaService.getCategorieRoot();
	}
	
	@GetMapping("/categoria/{id}/children")
	public List<CategoriaDto> getSottocategorie(@PathVariable Integer id) {
	    return categoriaService.getSottocategorie(id);
	}

}
