package br.com.zup.edu.biblioteca.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.biblioteca.model.Exemplar;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.repository.ExemplarRepository;
import br.com.zup.edu.biblioteca.repository.LivroRepository;

@RestController
@RequestMapping(LivroController.BASE_URI + "/{livroIsbn}" + ExemplarController.BASE_URI)
public class ExemplarController {

    public final static String BASE_URI = "/exemplares";

    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;

    public ExemplarController(LivroRepository livroRepository,
                              ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable String livroIsbn,
                                       UriComponentsBuilder uriComponentsBuilder) {
        String isbn = livroIsbn.replaceAll("[^0-9X]", "");
        Livro livro = livroRepository.findByIsbn(isbn)
                                     .orElseThrow(
                                         () -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Não existe um livro com o ISBN fornecido."
                                         )
                                     );
        Exemplar exemplar = exemplarRepository.save(new Exemplar(livro));

        URI location = uriComponentsBuilder.path(
            LivroController.BASE_URI + "/{livroIsbn}" + BASE_URI + "/{id}"
        ).buildAndExpand(isbn, exemplar.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
