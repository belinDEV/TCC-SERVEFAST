package com.restaurante.sistema.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private BigDecimal precoVenda;
    private boolean disponivel = true;

    // =================================================================
    //      CORREÇÃO AQUI: Adicionado fetch = FetchType.EAGER
    // =================================================================
    @OneToMany(mappedBy = "itemCardapio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FichaTecnicaItem> fichaTecnica = new ArrayList<>();

    /**
     * Converte a lista da ficha técnica para uma string JSON.
     * @return A lista da receita como uma string JSON.
     */
    public String getFichaTecnicaAsJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<FichaTecnicaItemDTO> dtos = new ArrayList<>();
            if (this.fichaTecnica != null) {
                for (FichaTecnicaItem item : this.fichaTecnica) {
                    dtos.add(new FichaTecnicaItemDTO(item));
                }
            }
            return mapper.writeValueAsString(dtos);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    // Classes internas (DTOs) para ajudar na conversão para JSON
    @Data
    private static class FichaTecnicaItemDTO {
        private double quantidade;
        private IngredienteDTO ingrediente;

        public FichaTecnicaItemDTO(FichaTecnicaItem item) {
            this.quantidade = item.getQuantidade();
            if (item.getIngrediente() != null) {
                this.ingrediente = new IngredienteDTO(item.getIngrediente());
            }
        }
    }

    @Data
    private static class IngredienteDTO {
        private String nome;
        private UnidadeMedidaDTO unidadeMedida;

        public IngredienteDTO(Ingrediente ingrediente) {
            this.nome = ingrediente.getNome();
            if (ingrediente.getUnidadeMedida() != null) {
                this.unidadeMedida = new UnidadeMedidaDTO(ingrediente.getUnidadeMedida());
            }
        }
    }

    @Data
    private static class UnidadeMedidaDTO {
        private String abreviacao;

        public UnidadeMedidaDTO(UnidadeMedida unidade) {
            this.abreviacao = unidade.getAbreviacao();
        }
    }
}
