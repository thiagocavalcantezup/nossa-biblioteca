package br.com.zup.edu.biblioteca.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.biblioteca.model.Autor;
import br.com.zup.edu.biblioteca.model.AutorDTO;
import br.com.zup.edu.biblioteca.repository.AutorRepository;

@RestController
@RequestMapping(AutorController.BASE_URI)
public class AutorController {

    public final static String BASE_URI = "/autores";

    private final AutorRepository autorRepository;

    public AutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AutorDTO autorDTO,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Autor autor = autorRepository.save(autorDTO.paraAutor());

        URI location = uriComponentsBuilder.path(BASE_URI + "/{id}")
                                           .buildAndExpand(autor.getId())
                                           .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody @Valid AutorDTO autorDTO) {
        Autor autor = autorRepository.findById(id)
                                     .orElseThrow(
                                         () -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Não existe um autor com o id informado."
                                         )
                                     );

        autor.setNome(autorDTO.getNome());
        autor.setEmail(autorDTO.getEmail());
        autor.setDescricao(autorDTO.getDescricao());
        autor.setCpf(autorDTO.getCpf());
        autorRepository.save(autor);

        return ResponseEntity.noContent().build();
    }

}
