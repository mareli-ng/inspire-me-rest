package it.mdg.inspireme.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mdg.inspireme.dao.TagDao;

@Service
public class TagService {

	@Autowired
	private TagDao tagDao;

	public List<String> getTagList() {
		return tagDao.findAll().stream().map(t -> t.getDescrizione()).collect(Collectors.toList());
	}
}