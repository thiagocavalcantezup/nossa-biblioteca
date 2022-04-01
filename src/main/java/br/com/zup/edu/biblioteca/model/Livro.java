package br.com.zup.edu.biblioteca.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 4000)
    @Lob
    private String descricao;

    @Column(nullable = false)
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;

    @Enumerated(EnumType.STRING)
    private TipoCirculacao circulacao;

    @Column(nullable = false)
    @Past
    private LocalDate dataPublicacao;

    /**
     * @deprecated Construtor de uso exclusivo do Hibernate
     */
    @Deprecated
    public Livro() {}

    public Livro(String titulo, String descricao, @ISBN(type = Type.ANY) String isbn,
                 LocalDate dataPublicacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
    }

    public Long getId() {
        return id;
    }

}
