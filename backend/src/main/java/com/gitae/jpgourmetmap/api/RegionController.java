package com.gitae.jpgourmetmap.api;

import com.gitae.jpgourmetmap.api.dto.RegionResponse;
import com.gitae.jpgourmetmap.service.RegionQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionQueryService regionQueryService;

    @GetMapping
    public List<RegionResponse> getRegions() {
        return regionQueryService.findAll();
    }
}
