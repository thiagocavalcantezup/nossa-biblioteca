package br.com.zup.edu.biblioteca.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

@Entity
@Table(name = "exemplares")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;

    @ManyToOne(optional = false)
    Livro livro;

    LocalDate criadoEm = LocalDate.now();

    /**
     * @deprecated Construtor de uso exclusivo do Hibernate
     */
    @Deprecated
    public Exemplar() {}

    public Exemplar(@ISBN(type = Type.ANY) String isbn, Livro livro) {
        this.isbn = isbn;
        this.livro = livro;
    }

    public Long getId() {
        return id;
    }

}
