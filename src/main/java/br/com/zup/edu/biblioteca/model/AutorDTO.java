package br.com.zup.edu.biblioteca.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

public class AutorDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 2500)
    private String descricao;

    @NotBlank
    @CPF
    private String cpf;

    public AutorDTO() {}

    public AutorDTO(@NotBlank String nome, @NotBlank @Email String email,
                    @NotBlank @Size(max = 2500) String descricao, @NotBlank @CPF String cpf) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
        this.cpf = cpf;
    }

    public Autor paraAutor() {
        return new Autor(nome, email, descricao, cpf);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCpf() {
        return cpf;
    }

}
