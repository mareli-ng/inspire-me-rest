package it.mdg.inspireme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.SceltaUtente;

@Repository
public interface ScelteUtentiDao extends JpaRepository<SceltaUtente, Integer> {

}
