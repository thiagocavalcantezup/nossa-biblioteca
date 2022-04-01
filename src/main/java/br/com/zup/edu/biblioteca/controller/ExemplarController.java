package br.com.zup.edu.biblioteca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(LivroController.BASE_URI + "/{livroIsbn}" + ExemplarController.BASE_URI)
public class ExemplarController {

    public final static String BASE_URI = "/exemplares";

}
