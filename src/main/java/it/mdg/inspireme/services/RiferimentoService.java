package it.mdg.inspireme.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.mdg.inspireme.dao.CategorieDao;
import it.mdg.inspireme.dao.OriginiDao;
import it.mdg.inspireme.dao.RiferimentiCategorieDao;
import it.mdg.inspireme.dao.RiferimentiDao;
import it.mdg.inspireme.dto.RiferimentoDto;
import it.mdg.inspireme.entities.Categoria;
import it.mdg.inspireme.entities.Origine;
import it.mdg.inspireme.entities.Riferimento;
import it.mdg.inspireme.entities.RiferimentoCategoria;

@Service
public class RiferimentoService {

	@Autowired
	private RiferimentiDao riferimentiDao;
	@Autowired
	private CategorieDao categorieDao;
	@Autowired
    private OriginiDao originiDao;

    @Autowired
    private RiferimentiCategorieDao riferimentiCategorieDao;

    @Transactional
    public RiferimentoDto addRiferimento(RiferimentoDto riferimentoDto) {
        Riferimento riferimento = new Riferimento();
        riferimento.setAnteprima(riferimentoDto.getAnteprima());
        riferimento.setTitolo(riferimentoDto.getTitolo());
        riferimento.setDescrizione(riferimentoDto.getDescrizione());
        riferimento.setUrl(riferimentoDto.getUrl());
        if (riferimentoDto.getOrigine() != null) {
        	Optional<Origine> opt = originiDao.findByDescrizione(riferimentoDto.getOrigine());
        	if (opt.isPresent()) {
        		riferimento.setIdOrigine(opt.get().getId());
        	} else {
                // Crea una nuova origine e salva nel database
                Origine nuovaOrigine = new Origine();
                nuovaOrigine.setDescrizione(riferimentoDto.getOrigine());
                nuovaOrigine = originiDao.save(nuovaOrigine);
                riferimento.setIdOrigine(nuovaOrigine.getId());
            }
        }

        // Salva il riferimento
        riferimento = riferimentiDao.save(riferimento);

        // Salva le associazioni tra il riferimento e le categorie
        saveRiferimentoCategorie(riferimento.getId(), riferimentoDto.getCategorieIds());

        riferimentoDto.setId(riferimento.getId());
        return riferimentoDto;
    }

    @Transactional
    public RiferimentoDto updateRiferimento(RiferimentoDto riferimentoDto) {
        // Recupera il riferimento esistente
        Riferimento riferimento = riferimentiDao.findById(riferimentoDto.getId())
                .orElseThrow(() -> new RuntimeException("Riferimento non trovato"));

        // Aggiorna i campi del riferimento
        riferimento.setAnteprima(riferimentoDto.getAnteprima());
        riferimento.setTitolo(riferimentoDto.getTitolo());
        riferimento.setDescrizione(riferimentoDto.getDescrizione());
        riferimento.setUrl(riferimentoDto.getUrl());
        if (riferimentoDto.getOrigine() != null) {
        	Optional<Origine> opt = originiDao.findByDescrizione(riferimentoDto.getOrigine().toLowerCase().trim());
        	if (opt.isPresent()) {
        		riferimento.setIdOrigine(opt.get().getId());
        	}
        }

        // Aggiorna il riferimento
        riferimentiDao.save(riferimento);

        // Aggiorna le associazioni con le categorie
        riferimentiCategorieDao.deleteByIdRiferimento(riferimento.getId());  // Elimina le associazioni esistenti
        saveRiferimentoCategorie(riferimento.getId(), riferimentoDto.getCategorieIds());  // Salva le nuove associazioni

        return riferimentoDto;
    }

    @Transactional
    public void deleteRiferimento(Integer id) {
        // Elimina le associazioni con le categorie
        riferimentiCategorieDao.deleteByIdRiferimento(id);

        // Elimina il riferimento
        riferimentiDao.deleteById(id);
    }

    // Metodo per salvare le associazioni tra un riferimento e le categorie
    private void saveRiferimentoCategorie(Integer idRiferimento, List<Integer> categorieIds) {
        List<RiferimentoCategoria> riferimentoCategorie = categorieIds.stream()
            .map(idCategoria -> {
                RiferimentoCategoria riferimentoCategoria = new RiferimentoCategoria();
                riferimentoCategoria.setIdRiferimento(idRiferimento);
                riferimentoCategoria.setIdCategoria(idCategoria);
                return riferimentoCategoria;
            })
            .collect(Collectors.toList());

        riferimentiCategorieDao.saveAll(riferimentoCategorie);
    }
    
    public Page<RiferimentoDto> getRiferimentiPaginati(Pageable pageable) {
        Page<Riferimento> riferimentiPage = riferimentiDao.findAll(pageable);
        return riferimentiPage.map(this::convertToDto);
    }

    private RiferimentoDto convertToDto(Riferimento riferimento) {
        RiferimentoDto dto = new RiferimentoDto();
        dto.setId(riferimento.getId());
        dto.setAnteprima(riferimento.getAnteprima());
        dto.setTitolo(riferimento.getTitolo());
        dto.setDescrizione(riferimento.getDescrizione());
        dto.setUrl(riferimento.getUrl());
        dto.setOrigine(originiDao.findById(riferimento.getIdOrigine()).orElseThrow(IllegalArgumentException::new).getDescrizione());
        return dto;
    }
    
    public List<RiferimentoDto> getRandomRiferimentiByCategorie(List<Integer> categorieIds, int n) {
        List<Integer> allCategoryIds = new ArrayList<>(categorieIds);
        for (Integer categoryId : categorieIds) {
            allCategoryIds.addAll(findAllChildCategoryIds(categoryId));
        }
        Pageable pageable = PageRequest.of(0, n);
        return riferimentiDao.findRandomByCategorie(allCategoryIds, pageable).stream().map(r -> convertToDto(r)).collect(Collectors.toList());
    }

    private List<Integer> findAllChildCategoryIds(Integer categoryId) {
        List<Integer> childCategoryIds = new ArrayList<>();
    	List<Categoria> children = categorieDao.findByIdCategoriaPadre(categoryId);
    	for (Categoria child : children) {
    		childCategoryIds.add(child.getId());
    		childCategoryIds.addAll(findAllChildCategoryIds(child.getId())); // Ricorsione per le sottocategorie
    	}
        return childCategoryIds;
    }

	public List<RiferimentoDto> getRiferimentiByIdCategoria(Integer id) {
		return riferimentiDao.findByIdCategoria(id).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public void updateCategoria(Integer idRiferimento, Integer idOld, List<Integer> idNew) {
		 riferimentiCategorieDao.deleteByIdCategoria(idOld);
		for (Integer id: idNew) {
			RiferimentoCategoria rc = new RiferimentoCategoria();
			rc.setIdCategoria(id);
			rc.setIdRiferimento(idRiferimento);
			riferimentiCategorieDao.save(rc);
		}
	}
}