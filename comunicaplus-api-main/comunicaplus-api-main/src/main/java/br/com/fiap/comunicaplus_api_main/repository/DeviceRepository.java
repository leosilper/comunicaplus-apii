package br.com.fiap.comunicaplus_api_main.repository;

import br.com.fiap.comunicaplus_api_main.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsByBluetoothAddress(String bluetoothAddress);
}
