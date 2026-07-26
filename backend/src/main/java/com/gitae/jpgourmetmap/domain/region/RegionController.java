package com.gitae.jpgourmetmap.domain.region;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    // 지역 조회 시 지역들 List로 반환
    @GetMapping
    public List<RegionResponse> getRegions() {
        return regionService.getRegions();
    }

}
