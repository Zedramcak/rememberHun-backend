package cz.adamzrcek.modules.user.entity;

import cz.adamzrcek.config.EncryptDecryptConverter;
import cz.adamzrcek.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    @Convert(converter = EncryptDecryptConverter.class)
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne
    @JoinColumn(name = "user_details_id")
    private UserDetail userDetail;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @CreationTimestamp
    private LocalDateTime lastActiveAt;
    private LocalDateTime scheduledDeletionDate;
}
