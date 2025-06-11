package br.com.fiap.comunicaplus_api_main.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.comunicaplus_api_main.dto.DeviceSummaryDTO;
import br.com.fiap.comunicaplus_api_main.model.Device;
import br.com.fiap.comunicaplus_api_main.repository.DeviceRepository;
import br.com.fiap.comunicaplus_api_main.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final MessageRepository messageRepository;

    // POST - Cadastrar novo dispositivo
    @PostMapping
    public ResponseEntity<Device> create(@RequestBody @Valid Device device) {
        Device saved = deviceRepository.save(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET - Buscar todos os dispositivos (resumo)
    @GetMapping("/summary")
    public List<DeviceSummaryDTO> listSummary() {
        return deviceRepository.findAll().stream().map(device -> {
            DeviceSummaryDTO dto = new DeviceSummaryDTO();
            dto.setId(device.getId()); // Adiciona o ID ao DTO
            dto.setDeviceName(device.getDeviceName());
            dto.setBluetoothAddress(device.getBluetoothAddress());
            dto.setWifiDirectAddress(device.getWifiDirectAddress());
            dto.setStatus(device.getStatus() != null ? device.getStatus().name() : null);

            var messages = messageRepository.findAll().stream()
                    .filter(m -> m.getSender() != null && m.getSender().getId().equals(device.getId()))
                    .collect(Collectors.toList());

            dto.setMessageCount(messages.size());
            dto.setMessageContents(messages.stream()
                    .map(m -> m.getContent())
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

    // GET - Buscar dispositivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable Long id) {
        Optional<Device> optional = deviceRepository.findById(id);
        return optional.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // PUT - Atualizar dispositivo por ID
    @PutMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable Long id, @RequestBody @Valid Device updatedDevice) {
        return deviceRepository.findById(id).map(existingDevice -> {
            existingDevice.setDeviceName(updatedDevice.getDeviceName());
            existingDevice.setBluetoothAddress(updatedDevice.getBluetoothAddress());
            existingDevice.setWifiDirectAddress(updatedDevice.getWifiDirectAddress());
            existingDevice.setStatus(updatedDevice.getStatus());
            Device saved = deviceRepository.save(existingDevice);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover dispositivo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!deviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        deviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
