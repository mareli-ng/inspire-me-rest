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

import it.mdg.inspireme.dto.RiferimentoDto;
import it.mdg.inspireme.dto.UpdateCategoriaDto;
import it.mdg.inspireme.services.RiferimentoService;

@RestController
public class RiferimentiController {

	@Autowired
	private RiferimentoService riferimentoService;

	@PostMapping("/riferimenti/add")
	public RiferimentoDto addRiferimento(@RequestBody RiferimentoDto riferimentoDto) {
		return riferimentoService.addRiferimento(riferimentoDto);
	}

	@PutMapping("/riferimenti/update")
	public RiferimentoDto updateRiferimento(@RequestBody RiferimentoDto riferimentoDto) {
		return riferimentoService.updateRiferimento(riferimentoDto);
	}
	
	@PutMapping("/riferimenti/update/categoria")
    public void updateRiferimentiCategoria(@RequestBody UpdateCategoriaDto request) {
        for (Integer id : request.getIdRiferimenti()) {
            riferimentoService.updateCategoria(id, request.getIdOld(), request.getIdNew());
        }
    }
	
	@DeleteMapping("/riferimenti/delete")
    public void deleteRiferimenti(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            riferimentoService.deleteRiferimento(id);
        }
    }

	@DeleteMapping("/riferimenti/delete/{id}")
	public void deleteRiferimento(@PathVariable Integer id) {
		riferimentoService.deleteRiferimento(id);
	}
	
	@GetMapping("/categoria/{id}/riferimenti")
	public List<RiferimentoDto> getRiferimentiByIdCategoria(@PathVariable Integer id) {
	    return riferimentoService.getRiferimentiByIdCategoria(id);
	}
}
