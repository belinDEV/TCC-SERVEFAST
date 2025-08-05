package com.restaurante.sistema.repository;

import com.restaurante.sistema.model.Pedido;
import com.restaurante.sistema.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca um pedido em uma mesa específica que não esteja com o status FECHADO.
     * Útil para encontrar um pedido em andamento em uma mesa.
     * @param mesaId O ID da mesa.
     * @param status O status a ser excluído da busca (geralmente FECHADO).
     * @return Um Optional contendo o Pedido, se encontrado.
     */

    Optional<Pedido> findByMesaIdAndStatusNot(Long mesaId, StatusPedido status);

    // NOVO MÉTODO: Encontra todos os pedidos com um determinado status
    List<Pedido> findAllByStatus(StatusPedido status);

    /**
     * Soma o valorTotal de todos os pedidos que estão com um status específico
     * e foram fechados dentro de um intervalo de datas.
     * @param status O status do pedido (geralmente FECHADO).
     * @param inicio A data e hora de início do período.
     * @param fim A data e hora de fim do período.
     * @return A soma total como BigDecimal.
     */
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.status = :status AND p.dataHoraFechamento BETWEEN :inicio AND :fim")
    BigDecimal sumValorTotalByStatusAndDataHoraFechamentoBetween(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}
