package br.com.zup.edu.biblioteca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
