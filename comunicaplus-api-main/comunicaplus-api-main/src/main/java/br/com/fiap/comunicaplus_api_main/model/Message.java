package br.com.fiap.comunicaplus_api_main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "SEQ_MESSAGE", allocationSize = 1)
    @Column(name = "ID_MESSAGE")
    private Long idMessage;


    @NotNull(message = "O dispositivo de origem é obrigatório")
    @ManyToOne
    private Device sender;

    @NotNull(message = "O dispositivo de destino é obrigatório")
    @ManyToOne
    private Device recipient;

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    @Column(length = 1000)
    private String content;

    @NotNull(message = "A data/hora do envio é obrigatória")
    private LocalDateTime timestamp;

    private boolean delivered;

    private boolean forwarded;

    @NotNull(message = "O tipo da mensagem é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;
}
