package com.gitae.jpgourmetmap.domain.restaurant;

import com.gitae.jpgourmetmap.config.BaseTimeEntity;
import com.gitae.jpgourmetmap.domain.region.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "restaurants",
        indexes = @Index(name = "idx_restaurants_region_id", columnList = "region_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "region_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_restaurants_region")
    )
    private Region region;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "tabelog_score", precision = 3, scale = 2)
    private BigDecimal tabelogScore;

    @Column(name = "tabelog_url", length = 1000)
    private String tabelogUrl;

    @Column(name = "youtube_url", length = 1000)
    private String youtubeUrl;

    public Restaurant(
            Region region,
            String name,
            String category,
            String address,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal tabelogScore,
            String tabelogUrl,
            String youtubeUrl
    ) {
        this.region = region;
        this.name = name;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tabelogScore = tabelogScore;
        this.tabelogUrl = tabelogUrl;
        this.youtubeUrl = youtubeUrl;
    }
}
