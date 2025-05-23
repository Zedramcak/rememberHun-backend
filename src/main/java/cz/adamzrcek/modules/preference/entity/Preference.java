package cz.adamzrcek.modules.preference.entity;

import cz.adamzrcek.modules.referencedata.entity.PreferenceCategory;
import cz.adamzrcek.modules.shared.config.EncryptDecryptConverter;
import cz.adamzrcek.modules.user.entity.User;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(indexes = {
        @Index(columnList = "user_id", name = "user_id_idx"),
        @Index(columnList = "user_id, category_id", name = "user_id_category_id_idx")
})
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private PreferenceCategory category;

    @Convert(converter = EncryptDecryptConverter.class)
    private String value;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdateAt;
}
