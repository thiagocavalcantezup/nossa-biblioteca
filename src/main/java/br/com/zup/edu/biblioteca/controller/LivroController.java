package br.com.zup.edu.biblioteca.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.model.LivroDTO;
import br.com.zup.edu.biblioteca.repository.LivroRepository;

@RestController
@RequestMapping(LivroController.BASE_URI)
public class LivroController {

    public final static String BASE_URI = "/livros";

    private final LivroRepository livroRepository;

    public LivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid LivroDTO livroDTO,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Livro livro = livroRepository.save(livroDTO.paraLivro());

        URI location = uriComponentsBuilder.path(BASE_URI + "/{id}")
                                           .buildAndExpand(livro.getId())
                                           .toUri();

        return ResponseEntity.created(location).build();
    }

}
