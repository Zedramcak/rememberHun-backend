package cz.adamzrcek.modules.importantdate.entity;

import cz.adamzrcek.modules.connection.entity.Connection;
import cz.adamzrcek.modules.referencedata.entity.ImportantDateCategory;
import cz.adamzrcek.modules.shared.config.EncryptDecryptConverter;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "category_id", name = "category_id_idx"),
        @Index(columnList = "connection_id", name = "connection_id_idx"),
        @Index(columnList = "date", name = "date_idx"),
        @Index(columnList = "date, category_id", name = "date_category_id_idx"),
        @Index(columnList = "should_be_notified", name = "should_be_notified_idx")
})
public class ImportantDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptDecryptConverter.class)
    private String title;

    @Convert(converter = EncryptDecryptConverter.class)
    private String note;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ImportantDateCategory category;

    private boolean shouldBeNotified;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Connection connection;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdateAt;

}
