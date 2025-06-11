package br.com.fiap.comunicaplus_api_main.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.comunicaplus_api_main.model.Device;
import br.com.fiap.comunicaplus_api_main.model.Message;
import br.com.fiap.comunicaplus_api_main.model.MessageType;

public class MessageSpecification {

    public static Specification<Message> hasContent(String content) {
        return (root, query, builder) -> 
            content == null ? null : builder.like(builder.lower(root.get("content")), "%" + content.toLowerCase() + "%");
    }

    public static Specification<Message> hasDevice(Device device) {
        return (root, query, builder) -> 
            device == null ? null : builder.or(
                builder.equal(root.get("sender"), device),
                builder.equal(root.get("recipient"), device)
            );
    }

    public static Specification<Message> isDelivered(Boolean delivered) {
        return (root, query, builder) -> 
            delivered == null ? null : builder.equal(root.get("delivered"), delivered);
    }

    public static Specification<Message> isForwarded(Boolean forwarded) {
        return (root, query, builder) -> 
            forwarded == null ? null : builder.equal(root.get("forwarded"), forwarded);
    }

    public static Specification<Message> hasMessageType(MessageType type) {
        return (root, query, builder) ->
            type == null ? null : builder.equal(root.get("messageType"), type);
    }
}
