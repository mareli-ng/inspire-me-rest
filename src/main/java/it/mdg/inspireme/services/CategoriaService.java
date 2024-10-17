package it.mdg.inspireme.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mdg.inspireme.dao.CategorieDao;
import it.mdg.inspireme.dto.CategoriaDto;
import it.mdg.inspireme.dto.UpdateCategoriaPadreDto;
import it.mdg.inspireme.entities.Categoria;

@Service
public class CategoriaService {

    @Autowired
    private CategorieDao categorieDao;

    public CategoriaDto addCategoria(Categoria categoria) {
        Categoria nuovaCategoria = categorieDao.save(categoria);
        return convertToDtoWithChildren(nuovaCategoria);
    }
    
    // Aggiorna una categoria esistente
    public CategoriaDto updateCategoria(Integer id, Categoria categoriaDetails) {
        Categoria categoria = categorieDao.findById(id).orElseThrow(() -> new RuntimeException("Categoria non trovata"));
        categoria.setNome(categoriaDetails.getNome());
        categoria.setIdCategoriaPadre(categoriaDetails.getIdCategoriaPadre());
        categorieDao.save(categoria);
        return convertToDtoWithChildren(categoria); // Restituisce il DTO aggiornato con le sottocategorie
    }

    // Cancella una categoria
    public void deleteCategoria(Integer id) {
        Categoria categoria = categorieDao.findById(id).orElseThrow(() -> new RuntimeException("Categoria non trovata"));
        categorieDao.delete(categoria);
    }
    
 // Metodo per costruire l'albero delle categorie
    public List<CategoriaDto> getAlberoCategorie() {
        List<Categoria> categorieRadice = categorieDao.findByIdCategoriaPadre(null);
        return categorieRadice.stream().map(this::convertToDtoWithChildren).collect(Collectors.toList());
    }

    // Metodo ricorsivo per costruire il DTO con le sottocategorie
    private CategoriaDto convertToDtoWithChildren(Categoria categoria) {
    	CategoriaDto categoriaDto = convertToDto(categoria);
    	
    	// Recupera le sottocategorie
    	List<Categoria> sottoCategorie = categorieDao.findByIdCategoriaPadre(categoria.getId());
    	List<CategoriaDto> sottoCategorieDTO = sottoCategorie.stream()
    			.map(this::convertToDtoWithChildren)
    			.collect(Collectors.toList());
    	
    	// Assegna le sottocategorie
    	categoriaDto.setSottoCategorie(sottoCategorieDTO);
    	
    	return categoriaDto;
    }
    
    private CategoriaDto convertToDto(Categoria categoria) {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setId(categoria.getId());
        categoriaDto.setNome(categoria.getNome());
        categoriaDto.setIdCategoriaPadre(categoria.getIdCategoriaPadre());
        return categoriaDto;
    }
    
	public List<CategoriaDto> getCategorieRoot() {
		return categorieDao.findByIdCategoriaPadre(null).stream().map(c -> convertToDto(c)).collect(Collectors.toList());
	}

	public List<CategoriaDto> getSottocategorie(Integer id) {
		return categorieDao.findByIdCategoriaPadre(id).stream().map(c -> convertToDto(c)).collect(Collectors.toList());
	}

	public void updateCategoriaPadre(UpdateCategoriaPadreDto dto) {
		List<Categoria> categorie = categorieDao.findByIdList(dto.getIdCategorie());
		for (Categoria c: categorie) {
			c.setIdCategoriaPadre(dto.getIdCategoriaPadre());
			categorieDao.save(c);
		}
	}
}