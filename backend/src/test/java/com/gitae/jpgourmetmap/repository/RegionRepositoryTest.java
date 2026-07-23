package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void 지역_조회() {
        Region region = new Region(
                "Tokyo",
                "Tokyo",
                new BigDecimal("1.1000000000"),
                new BigDecimal("2.2000000000")
        );
        regionRepository.save(region);

        Optional<Region> result = regionRepository.findById(region.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Tokyo");

    }


}
