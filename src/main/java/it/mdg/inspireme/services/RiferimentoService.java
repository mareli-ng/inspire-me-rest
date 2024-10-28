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
import it.mdg.inspireme.dao.RiferimentiTagDao;
import it.mdg.inspireme.dao.TagDao;
import it.mdg.inspireme.dto.RiferimentoDto;
import it.mdg.inspireme.dto.SuggestionFilterDto;
import it.mdg.inspireme.entities.Categoria;
import it.mdg.inspireme.entities.Origine;
import it.mdg.inspireme.entities.Riferimento;
import it.mdg.inspireme.entities.RiferimentoCategoria;
import it.mdg.inspireme.entities.RiferimentoTag;
import it.mdg.inspireme.entities.Tag;

@Service
public class RiferimentoService {

	@Autowired
	private RiferimentiDao riferimentiDao;
	@Autowired
	private CategorieDao categorieDao;
	@Autowired
	private OriginiDao originiDao;
	@Autowired
	private TagDao tagDao;
	@Autowired
	private RiferimentiTagDao riferimentiTagDao;

	@Autowired
	private RiferimentiCategorieDao riferimentiCategorieDao;

	@Transactional
	public RiferimentoDto addRiferimento(RiferimentoDto riferimentoDto) {
		Riferimento riferimento = new Riferimento();
		riferimento.setAnteprima(riferimentoDto.getAnteprima());
		riferimento.setTitolo(riferimentoDto.getTitolo());
		riferimento.setDescrizione(riferimentoDto.getDescrizione());
		riferimento.setUrl(riferimentoDto.getUrl());
		
		//origine
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

	//tag
		if (riferimentoDto.getTagList() != null) {
			Integer idRiferimento = riferimento.getId();
			riferimentoDto.getTagList().stream().forEach(t -> {
				Optional<Tag> opt = tagDao.findByDescrizione(t);
				Integer idTag = null;
				if (opt.isPresent()) {
					idTag = opt.get().getId();
				} else {
					// Crea una nuova origine e salva nel database
					Tag nuovoTag = new Tag();
					nuovoTag.setDescrizione(t);
					nuovoTag = tagDao.save(nuovoTag);
					idTag = nuovoTag.getId();
				}
				RiferimentoTag rt = new RiferimentoTag();
				rt.setIdRiferimento(idRiferimento);
				rt.setIdTag(idTag);
				riferimentiTagDao.save(rt);
			});
		}

		// categorie
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
		riferimentiCategorieDao.deleteByIdRiferimento(riferimento.getId()); // Elimina le associazioni esistenti
		saveRiferimentoCategorie(riferimento.getId(), riferimentoDto.getCategorieIds()); // Salva le nuove associazioni

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
		List<RiferimentoCategoria> riferimentoCategorie = categorieIds.stream().map(idCategoria -> {
			RiferimentoCategoria riferimentoCategoria = new RiferimentoCategoria();
			riferimentoCategoria.setIdRiferimento(idRiferimento);
			riferimentoCategoria.setIdCategoria(idCategoria);
			return riferimentoCategoria;
		}).collect(Collectors.toList());

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
		if (riferimento.getIdOrigine() != null) {
			dto.setOrigine(originiDao.findById(riferimento.getIdOrigine()).orElseThrow(IllegalArgumentException::new)
					.getDescrizione());
		}
		dto.setTagList(tagDao.findByIdRiferimento(riferimento.getId()).stream().map(t -> t.getDescrizione()).collect(Collectors.toList()));
		return dto;
	}

	public List<RiferimentoDto> getRandomRiferimenti(SuggestionFilterDto dto, int n) {
		List<Integer> categoryIds = null;
		List<Integer> tagIds = null;
		List<Integer> originiIds = null;
		
		if (checkIfHasElements(dto.getCategorie())) {
			categoryIds = new ArrayList<>();
			categoryIds.addAll(dto.getCategorie());
			for (Integer categoryId : dto.getCategorie()) {
				categoryIds.addAll(findAllChildCategoryIds(categoryId));
			}
		}
		
		if (checkIfHasElements(dto.getTag())) {
			tagIds = this.tagDao.findByDescrizioneList(dto.getTag().stream().map(t -> t.toLowerCase().trim()).collect(Collectors.toList())).stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		
		if (checkIfHasElements(dto.getOrigini())) {
			originiIds = dto.getOrigini().stream().map(o -> o.getValue()).collect(Collectors.toList());
		}
		
		Pageable pageable = PageRequest.of(0, n);
		return riferimentiDao.findRandomWithFilters(categoryIds, originiIds, tagIds, pageable).stream().map(r -> convertToDto(r)).collect(Collectors.toList());
//		return riferimentiDao.findRandom(allCategoryIds, tagIds, originiIds, pageable).stream().map(r -> convertToDto(r))
//				.collect(Collectors.toList());
	}
	
	private boolean checkIfHasElements(List<?> ids) {
		return ids != null && ids.size() > 0;
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
		for (Integer id : idNew) {
			RiferimentoCategoria rc = new RiferimentoCategoria();
			rc.setIdCategoria(id);
			rc.setIdRiferimento(idRiferimento);
			riferimentiCategorieDao.save(rc);
		}
	}
}