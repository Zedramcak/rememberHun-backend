package cz.adamzrcek.modules.shared.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
@Order(1)
public class EncryptDecryptConverter implements AttributeConverter<String, String> {

    @Value( "${encryption.algorithm}")
    private final String algorithm;
    @Value( "${encryption.key}")
    private final String secretKey;

    public EncryptDecryptConverter(
            @Value( "${encryption.algorithm}") String algorithm,
            @Value( "${encryption.key}") String secretKey
    ) {
        this.algorithm = algorithm;
        this.secretKey = secretKey;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(attribute.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Could not encrypt data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decValue = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decValue);
        } catch (Exception e) {
            throw new RuntimeException("Could not decrypt data", e);
        }
    }
}
