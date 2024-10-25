package it.mdg.inspireme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.mdg.inspireme.services.TagService;

@RestController
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping("/tag")
	public List<String> getTagList() {
		return tagService.getTagList();
	}
	

}
