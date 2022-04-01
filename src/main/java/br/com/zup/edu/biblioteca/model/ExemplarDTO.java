package br.com.zup.edu.biblioteca.model;

public class ExemplarDTO {

    public ExemplarDTO() {}

    public Exemplar paraExemplar(Livro livro) {
        return new Exemplar(livro);
    }

}
