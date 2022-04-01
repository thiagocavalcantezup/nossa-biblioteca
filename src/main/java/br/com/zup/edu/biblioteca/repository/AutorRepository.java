package br.com.zup.edu.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zup.edu.biblioteca.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}
