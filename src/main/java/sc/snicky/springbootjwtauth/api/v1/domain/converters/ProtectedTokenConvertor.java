package sc.snicky.springbootjwtauth.api.v1.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

@Converter(autoApply = true)
public class ProtectedTokenConvertor implements AttributeConverter<ProtectedToken, String> {
    /**
     * Converts a ProtectedToken entity attribute to its database column representation.
     *
     * @param attribute the ProtectedToken entity attribute, may be null
     * @return the token string for database storage, or null if the attribute is null
     */
    @Override
    public String convertToDatabaseColumn(ProtectedToken attribute) {
        return attribute != null ? attribute.getToken() : null;
    }

    /**
     * Converts a database column value to a ProtectedToken entity attribute.
     *
     * @param dbData the database column value, may be null
     * @return a ProtectedToken object, or null if the database value is null
     */
    @Override
    public ProtectedToken convertToEntityAttribute(String dbData) {
        return dbData != null ? new ProtectedToken(dbData) : null;
    }
}
