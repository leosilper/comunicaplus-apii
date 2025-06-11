package br.com.fiap.comunicaplus_api_main.dto;

import java.util.List;

import lombok.Data;

@Data
public class DeviceSummaryDTO {
    private Long id; 
    private String deviceName;
    private int messageCount;
    private List<String> messageContents;
    private String bluetoothAddress;
    private String wifiDirectAddress;
    private String status;
}
