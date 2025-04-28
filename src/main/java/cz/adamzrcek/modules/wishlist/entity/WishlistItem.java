package cz.adamzrcek.modules.wishlist.entity;


import cz.adamzrcek.modules.referencedata.entity.WishlistCategory;
import cz.adamzrcek.modules.shared.config.EncryptDecryptConverter;
import cz.adamzrcek.modules.user.entity.User;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class WishlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptDecryptConverter.class)
    private String title;

    @Convert(converter = EncryptDecryptConverter.class)
    private String description;

    private boolean fulfilled = false;

    @ManyToOne
    private WishlistCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdateAt;

}
