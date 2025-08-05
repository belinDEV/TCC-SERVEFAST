package com.restaurante.sistema.repository;

import com.restaurante.sistema.model.FichaTecnicaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaTecnicaItemRepository extends JpaRepository<FichaTecnicaItem, Long> {
}
    