package br.com.fiap.comunicaplus_api_main.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private Long id;
    private String deviceName;
    private String bluetoothAddress;
    private String wifiDirectAddress;
    private String status; 
    private int totalMessagesSent;
    private int totalMessagesReceived;
}
