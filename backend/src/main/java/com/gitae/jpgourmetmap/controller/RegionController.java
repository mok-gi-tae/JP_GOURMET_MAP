package com.gitae.jpgourmetmap.controller;

import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.dto.region.RegionResponse;
import com.gitae.jpgourmetmap.service.RegionService;
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

    @GetMapping
    public List<RegionResponse> getRegions() {
        return regionService.getRegions();
    }

}
