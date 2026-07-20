package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findAllByOrderByCityAscNameAsc();
}
