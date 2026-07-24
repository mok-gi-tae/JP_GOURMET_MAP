package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.dto.region.RegionResponse;
import com.gitae.jpgourmetmap.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    // regionRepository에 저장된 모든 행 조회 후 List로 return
    public List<RegionResponse> getRegions() {
        return regionRepository.findAll()
                .stream()
                .map(RegionResponse::from)
                .toList();
    }

}
