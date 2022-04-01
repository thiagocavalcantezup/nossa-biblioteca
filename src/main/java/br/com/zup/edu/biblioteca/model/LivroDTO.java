package br.com.zup.edu.biblioteca.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

public class LivroDTO {

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @NotBlank
    @Size(max = 4000)
    private String descricao;

    @NotBlank
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;

    @NotNull
    @Past
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPublicacao;

    public LivroDTO() {}

    public LivroDTO(@NotBlank @Size(max = 200) String titulo,
                    @NotBlank @Size(max = 4000) String descricao,
                    @NotBlank @ISBN(type = Type.ANY) String isbn,
                    @NotNull @Past LocalDate dataPublicacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
    }

    public Livro paraLivro() {
        String novoIsbn = isbn.replaceAll("[^0-9X]", "");

        return new Livro(titulo, descricao, novoIsbn, dataPublicacao);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

}
