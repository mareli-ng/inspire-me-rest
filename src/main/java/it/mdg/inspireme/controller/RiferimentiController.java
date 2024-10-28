package it.mdg.inspireme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.mdg.inspireme.dto.RiferimentoDto;
import it.mdg.inspireme.dto.UpdateCategoriaDto;
import it.mdg.inspireme.services.FirebaseStorageService;
import it.mdg.inspireme.services.RiferimentoService;

@RestController
public class RiferimentiController {

	@Autowired
	private RiferimentoService riferimentoService;
	@Autowired
	private FirebaseStorageService firebaseStorageService;
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping(value = "/riferimenti/add")
	public ResponseEntity<RiferimentoDto> addRiferimento(
			@RequestParam("riferimento") String dto,
	        @RequestParam(value = "file", required = false) MultipartFile file) {
		 System.out.println("Received RiferimentoDto: " + dto);
	    try {
	    	RiferimentoDto riferimentoDto = objectMapper.readValue(dto, RiferimentoDto.class);
	    	if (riferimentoDto.getAnteprima() == null) {
		        if (file != null && !file.isEmpty()) {
		            String imageUrl = firebaseStorageService.uploadFile(file, file.getOriginalFilename());
		            riferimentoDto.setAnteprima(imageUrl);
		        } else {
		            riferimentoDto.setAnteprima("default-image-url"); // Imposta un URL immagine di default se necessario
		        }
	    	}

	        RiferimentoDto savedRiferimento = riferimentoService.addRiferimento(riferimentoDto);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedRiferimento);

	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	@PutMapping("/riferimenti/update")
	public ResponseEntity<RiferimentoDto> updateRiferimento(@RequestPart("riferimento") RiferimentoDto riferimentoDto,
			@RequestParam("file") MultipartFile file) {
		try {
//TODO gestire update file, origine e tag
			String imageUrl = firebaseStorageService.uploadFile(file, file.getOriginalFilename());
			riferimentoDto.setAnteprima(imageUrl);
			RiferimentoDto savedRiferimento = riferimentoService.updateRiferimento(riferimentoDto);

			return ResponseEntity.status(HttpStatus.CREATED).body(savedRiferimento);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

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
