package br.com.fiap.comunicaplus_api_main.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.comunicaplus_api_main.model.Device;
import br.com.fiap.comunicaplus_api_main.model.DeviceStatus;
import br.com.fiap.comunicaplus_api_main.model.Message;
import br.com.fiap.comunicaplus_api_main.model.MessageType;
import br.com.fiap.comunicaplus_api_main.model.Role;
import br.com.fiap.comunicaplus_api_main.model.User;
import br.com.fiap.comunicaplus_api_main.repository.DeviceRepository;
import br.com.fiap.comunicaplus_api_main.repository.MessageRepository;
import br.com.fiap.comunicaplus_api_main.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final DeviceRepository deviceRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final Random random = new Random();

    @PostConstruct
    public void seedDatabase() {
        seedUsers();
        seedDevices();
        seedMessages();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            List<User> users = List.of(
                User.builder().name("Alice").email("alice@email.com").password(encoder.encode("123456")).role(Role.USER).build(),
                User.builder().name("Bob").email("bob@email.com").password(encoder.encode("123456")).role(Role.USER).build(),
                User.builder().name("Carol").email("carol@email.com").password(encoder.encode("123456")).role(Role.ADMIN).build()
            );

            userRepository.saveAll(users);
        }
    }

    private void seedDevices() {
        if (deviceRepository.count() == 0) {
            List<Device> devices = List.of(
                Device.builder().deviceName("Celular Alice").bluetoothAddress("00:11:22:33:44:AA").wifiDirectAddress("192.168.49.1").status(DeviceStatus.ONLINE).build(),
                Device.builder().deviceName("Celular Bob").bluetoothAddress("00:11:22:33:44:BB").wifiDirectAddress("192.168.49.2").status(DeviceStatus.ONLINE).build(),
                Device.builder().deviceName("Celular Carol").bluetoothAddress("00:11:22:33:44:CC").wifiDirectAddress("192.168.49.3").status(DeviceStatus.OFFLINE).build()
            );

            deviceRepository.saveAll(devices);
        }
    }

    private void seedMessages() {
        if (messageRepository.count() == 0) {
            List<Device> devices = deviceRepository.findAll();

            createMessage(devices, "Alerta de movimentação incomum no setor norte.", MessageType.ALERT);
            createMessage(devices, "Solicito assistência médica imediata.", MessageType.REQUEST_HELP);
            createMessage(devices, "Informação compartilhada sobre rota segura.", MessageType.INFO);
            createMessage(devices, "Grupo de apoio localizado próximo à escola.", MessageType.INFO);
            createMessage(devices, "Enviando suprimentos para a base oeste.", MessageType.FORWARDED);

            String[] contents = {
                "Sinal fraco nesta região.",
                "Preciso de socorro no bairro Novo Horizonte.",
                "Grupos reunidos no colégio central.",
                "Enviando kit de primeiros socorros.",
                "Sem energia, aguardando instruções."
            };

            for (String content : contents) {
                MessageType determinedType = determineMessageType(content);
                createMessage(devices, content, determinedType);
            }
        }
    }

    private MessageType determineMessageType(String content) {
        String lowerContent = content.toLowerCase();

        if (lowerContent.contains("socorro") || lowerContent.contains("ajuda") || lowerContent.contains("assistência")) {
            return MessageType.REQUEST_HELP;
        } else if (lowerContent.contains("alerta") || lowerContent.contains("sinal")) {
            return MessageType.ALERT;
        } else if (lowerContent.contains("kit") || lowerContent.contains("instruções") || lowerContent.contains("enviando")) {
            return MessageType.FORWARDED;
        } else {
            return MessageType.INFO;
        }
    }

    private void createMessage(List<Device> devices, String content, MessageType type) {
        Device sender = getRandomDevice(devices);
        Device recipient;
        do {
            recipient = getRandomDevice(devices);
        } while (recipient.equals(sender));

        Message message = Message.builder()
            .sender(sender)
            .recipient(recipient)
            .content(content)
            .timestamp(LocalDateTime.now().minusMinutes(random.nextInt(120)))
            .delivered(random.nextBoolean())
            .forwarded(random.nextBoolean())
            .messageType(type)
            .build();

        messageRepository.save(message);

        sender.setTotalMessagesSent(sender.getTotalMessagesSent() + 1);
        recipient.setTotalMessagesReceived(recipient.getTotalMessagesReceived() + 1);

        deviceRepository.save(sender);
        deviceRepository.save(recipient);
    }

    private Device getRandomDevice(List<Device> devices) {
        return devices.get(random.nextInt(devices.size()));
    }
}
