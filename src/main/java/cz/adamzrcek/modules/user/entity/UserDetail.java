package cz.adamzrcek.modules.user.entity;

import cz.adamzrcek.modules.shared.config.EncryptDecryptConverter;
import cz.adamzrcek.modules.connection.entity.Connection;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptDecryptConverter.class)
    private String firstName;
    @Convert(converter = EncryptDecryptConverter.class)
    private String lastName;
    private LocalDate birthDate;
    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Connection connection;
}
