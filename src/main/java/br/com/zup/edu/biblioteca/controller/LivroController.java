package br.com.zup.edu.biblioteca.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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

    @Transactional
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid LivroDTO livroDTO,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Livro livro = livroRepository.save(livroDTO.paraLivro());

        URI location = uriComponentsBuilder.path(BASE_URI + "/{id}")
                                           .buildAndExpand(livro.getId())
                                           .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable String isbn) {
        String livroIsbn = isbn.replaceAll("[^0-9X]", "");
        Livro livro = livroRepository.findByIsbn(livroIsbn)
                                     .orElseThrow(
                                         () -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Não existe um livro com o ISBN informado."
                                         )
                                     );

        if (livro.isCirculacaoLivre()) {
            livroRepository.delete(livro);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Impossível excluir um livro que não seja de circulação livre."
            );
        }
    }

}
