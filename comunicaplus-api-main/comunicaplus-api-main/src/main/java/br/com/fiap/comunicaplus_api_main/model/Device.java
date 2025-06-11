package br.com.fiap.comunicaplus_api_main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
    @SequenceGenerator(name = "device_seq", sequenceName = "SEQ_DEVICE_ID", allocationSize = 1)
    @Column(name = "ID_DEVICE")
    private Long id;

    @NotBlank(message = "O nome do dispositivo é obrigatório")
    private String deviceName;

    @NotBlank(message = "Endereço Bluetooth é obrigatório")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$", message = "Endereço Bluetooth inválido")
    private String bluetoothAddress;

    @NotBlank(message = "Endereço Wi-Fi Direct é obrigatório")
    private String wifiDirectAddress;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status do dispositivo é obrigatório")
    private DeviceStatus status;

    @Builder.Default
    private int totalMessagesSent = 0;

    @Builder.Default
    private int totalMessagesReceived = 0;
}
