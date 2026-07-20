package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.api.dto.RegionResponse;
import com.gitae.jpgourmetmap.repository.RegionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionQueryService {

    private final RegionRepository regionRepository;

    public List<RegionResponse> findAll() {
        return regionRepository.findAllByOrderByCityAscNameAsc().stream()
                .map(RegionResponse::from)
                .toList();
    }
}
