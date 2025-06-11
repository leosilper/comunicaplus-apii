package br.com.fiap.comunicaplus_api_main.specification;

import br.com.fiap.comunicaplus_api_main.model.Device;
import br.com.fiap.comunicaplus_api_main.model.User;
import org.springframework.data.jpa.domain.Specification;

public class DeviceSpecification {

    public static Specification<Device> hasIdentifier(String identifier) {
        return (root, query, builder) ->
            identifier == null ? null : builder.like(builder.lower(root.get("identifier")), "%" + identifier.toLowerCase() + "%");
    }

    public static Specification<Device> hasModel(String model) {
        return (root, query, builder) ->
            model == null ? null : builder.like(builder.lower(root.get("model")), "%" + model.toLowerCase() + "%");
    }

    public static Specification<Device> hasBrand(String brand) {
        return (root, query, builder) ->
            brand == null ? null : builder.like(builder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public static Specification<Device> hasUser(User user) {
        return (root, query, builder) ->
            user == null ? null : builder.equal(root.get("user"), user);
    }

    public static Specification<Device> isConnected(Boolean isConnected) {
        return (root, query, builder) ->
            isConnected == null ? null : builder.equal(root.get("connected"), isConnected);
    }
}
