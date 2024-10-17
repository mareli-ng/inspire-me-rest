package it.mdg.inspireme.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
@Entity
@Table(name = "riferimenti")
public class Riferimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nullable
    private String anteprima;
    private String titolo;
    private String descrizione;
    private String url;
    private Integer idOrigine;
}