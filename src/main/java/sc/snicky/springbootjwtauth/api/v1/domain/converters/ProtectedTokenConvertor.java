package sc.snicky.springbootjwtauth.api.v1.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sc.snicky.springbootjwtauth.api.v1.domain.types.ProtectedToken;

@Converter(autoApply = true)
public class ProtectedTokenConvertor implements AttributeConverter<ProtectedToken, String> {
    @Override
    public String convertToDatabaseColumn(ProtectedToken attribute) {
        return attribute != null ? attribute.getToken() : null;
    }

    @Override
    public ProtectedToken convertToEntityAttribute(String dbData) {
        return dbData != null ? new ProtectedToken(dbData) : null;
    }
}
