package it.mdg.inspireme.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.mdg.inspireme.dao.OriginiDao;
import it.mdg.inspireme.dto.RiferimentoDto;
import it.mdg.inspireme.dto.SelectItem;
import it.mdg.inspireme.services.RiferimentoService;

@RestController
public class MainController {

	@Autowired
    private RiferimentoService riferimentoService;
	
	@Autowired
	private OriginiDao originiDao;

    @GetMapping("/riferimenti/all")
    public Page<RiferimentoDto> getRiferimentiPaginati(Pageable pageable) {
        return riferimentoService.getRiferimentiPaginati(pageable);
    }
	
    @PostMapping("/random")
    public List<RiferimentoDto> getRandomRiferimentiByCategorie(@RequestBody List<Integer> categorieIds, @RequestParam int n) {
    	return riferimentoService.getRandomRiferimentiByCategorie(categorieIds, n);
    }
    
    @GetMapping("/options/origini")
    public List<SelectItem> getOriginiOptions() {
        return originiDao.findAll().stream().map(o -> new SelectItem().setLabel(o.getDescrizione()).setValue(o.getId())).collect(Collectors.toList());
    }
	
}