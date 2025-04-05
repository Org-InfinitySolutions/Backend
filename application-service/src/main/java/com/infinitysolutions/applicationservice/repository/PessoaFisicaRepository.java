package com.infinitysolutions.applicationservice.repository;


import com.infinitysolutions.applicationservice.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsById(UUID id);
    boolean existsByRgContaining(String rg);
}
