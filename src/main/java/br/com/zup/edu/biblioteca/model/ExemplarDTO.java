package br.com.zup.edu.biblioteca.model;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

public class ExemplarDTO {

    @NotBlank
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;

    /**
     * @deprecated Construtor de uso exclusivo do Hibernate
     */
    @Deprecated
    public ExemplarDTO() {}

    public ExemplarDTO(@ISBN(type = Type.ANY) String isbn) {
        this.isbn = isbn;
    }

    public Exemplar paraExemplar(Livro livro) {
        return new Exemplar(isbn, livro);
    }

    public String getIsbn() {
        return isbn;
    }

}
