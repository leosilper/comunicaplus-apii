package br.com.fiap.comunicaplus_api_main.controller;

import java.net.URI;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.comunicaplus_api_main.dto.MessageDTO;
import br.com.fiap.comunicaplus_api_main.model.Device;
import br.com.fiap.comunicaplus_api_main.model.Message;
import br.com.fiap.comunicaplus_api_main.model.MessageType;
import br.com.fiap.comunicaplus_api_main.repository.DeviceRepository;
import br.com.fiap.comunicaplus_api_main.repository.MessageRepository;
import br.com.fiap.comunicaplus_api_main.service.MessageService;
import br.com.fiap.comunicaplus_api_main.specification.MessageSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final DeviceRepository deviceRepository;
    private final MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<MessageDTO> create(@RequestBody @Valid Message message) {
        // Buscar o sender e recipient pelo ID para garantir que estão completos
        Device sender = message.getSender() != null && message.getSender().getId() != null
                ? deviceRepository.findById(message.getSender().getId()).orElse(null)
                : null;

        Device recipient = message.getRecipient() != null && message.getRecipient().getId() != null
                ? deviceRepository.findById(message.getRecipient().getId()).orElse(null)
                : null;

        message.setSender(sender);
        message.setRecipient(recipient);

        Message saved = messageService.save(message);

        return ResponseEntity.created(URI.create("/api/messages/" + saved.getIdMessage()))
                             .body(toDTO(saved));
    }

    @GetMapping
    @Cacheable("messages")
    public Page<MessageDTO> list(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) MessageType messageType,
            Pageable pageable
    ) {
        Device device = null;
        if (deviceId != null) {
            device = deviceRepository.findById(deviceId).orElse(null);
        }

        Specification<Message> spec = Specification.where(
                MessageSpecification.hasContent(content)
                        .and(MessageSpecification.hasDevice(device))
                        .and(MessageSpecification.hasMessageType(messageType))
        );

        return messageRepository.findAll(spec, pageable).map(this::toDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> get(@PathVariable Long id) {
        return messageService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> update(@PathVariable Long id, @RequestBody @Valid Message updatedMessage) {
        return messageService.findById(id)
                .map(existingMessage -> {
                    existingMessage.setContent(updatedMessage.getContent());
                    existingMessage.setSender(updatedMessage.getSender());
                    existingMessage.setRecipient(updatedMessage.getRecipient());
                    existingMessage.setTimestamp(updatedMessage.getTimestamp());
                    existingMessage.setDelivered(updatedMessage.isDelivered());
                    existingMessage.setForwarded(updatedMessage.isForwarded());
                    existingMessage.setMessageType(updatedMessage.getMessageType());
                    Message saved = messageService.save(existingMessage);
                    return ResponseEntity.ok(toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private MessageDTO toDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setIdMessage(message.getIdMessage());
        dto.setContent(message.getContent());
        dto.setSenderDeviceName(message.getSender() != null ? message.getSender().getDeviceName() : null);
        dto.setRecipientDeviceName(message.getRecipient() != null ? message.getRecipient().getDeviceName() : null);
        dto.setTimestamp(message.getTimestamp());
        dto.setDelivered(message.isDelivered());
        dto.setForwarded(message.isForwarded());
        dto.setMessageType(message.getMessageType());
        return dto;
    }
}
