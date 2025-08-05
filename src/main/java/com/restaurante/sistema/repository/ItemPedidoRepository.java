package com.restaurante.sistema.repository;

import com.restaurante.sistema.model.ItemPedido;
import com.restaurante.sistema.model.dto.ItemMaisVendidoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // CONSULTA ATUALIZADA PARA ACEITAR DATAS
    @Query("SELECT new com.restaurante.sistema.model.dto.ItemMaisVendidoDTO(ip.itemCardapio.nome, SUM(ip.quantidade)) " +
            "FROM ItemPedido ip JOIN ip.pedido p " +
            "WHERE p.status = com.restaurante.sistema.model.StatusPedido.FECHADO " +
            "AND (cast(:dataInicio as timestamp) IS NULL OR p.dataHoraFechamento >= :dataInicio) " +
            "AND (cast(:dataFim as timestamp) IS NULL OR p.dataHoraFechamento <= :dataFim) " +
            "GROUP BY ip.itemCardapio.nome " +
            "ORDER BY SUM(ip.quantidade) DESC")
    List<ItemMaisVendidoDTO> findItensMaisVendidosPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}
