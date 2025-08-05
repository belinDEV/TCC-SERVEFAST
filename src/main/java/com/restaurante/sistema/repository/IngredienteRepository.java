package com.restaurante.sistema.repository;

import com.restaurante.sistema.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    /**
     * Conta quantos ingredientes estão com a quantidade em estoque abaixo de um limite.
     * @param limite O valor limite para considerar o estoque baixo.
     * @return A contagem de ingredientes com estoque baixo.
     */
    // CORREÇÃO AQUI: Trocamos "FROM Produto p" por "FROM Ingrediente p"
    @Query("SELECT COUNT(p) FROM Ingrediente p WHERE p.quantidadeEmEstoque <= :limite")
    long contarEstoqueBaixo(@Param("limite") int limite);

}
