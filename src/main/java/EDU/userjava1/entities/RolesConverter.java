package EDU.userjava1.entities;

import com.google.gson.Gson;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RolesConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String role) {
        if (role == null || role.isEmpty()) {
            // If role is null or empty, default to "ROLE_USER"
            return "[\"ROLE_USER\"]";
        }
        // Convert single string to JSON string
        return new Gson().toJson(role);
    }

    @Override
    public String convertToEntityAttribute(String rolesJson) {
        if (rolesJson == null || rolesJson.isEmpty()) {
            // If JSON string is null or empty, default to "ROLE_USER"
            return "";
        }
        // Convert JSON string to single string
        return new Gson().fromJson(rolesJson, String.class);
    }
}
