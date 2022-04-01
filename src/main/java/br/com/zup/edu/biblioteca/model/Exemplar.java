package br.com.zup.edu.biblioteca.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "exemplares")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Livro livro;

    private LocalDate criadoEm = LocalDate.now();

    /**
     * @deprecated Construtor de uso exclusivo do Hibernate
     */
    @Deprecated
    public Exemplar() {}

    public Exemplar(Livro livro) {
        this.livro = livro;
    }

    public Long getId() {
        return id;
    }

}
