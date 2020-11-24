package com.upc.demo.controller;

import com.upc.demo.domain.model.Reservation;
import com.upc.demo.domain.service.ReservationService;
import com.upc.demo.resource.ReservationResource;
import com.upc.demo.resource.saving.SaveReservationResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReservationController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ReservationService recommendationService;

    @GetMapping("/services/{serviceId}/recommendations")
    public Page<ReservationResource> getAllReservationsByServiceId(
            @PathVariable(name = "serviceId") Long serviceId,
            Pageable pageable) {
        Page<Reservation> recommendationPage = recommendationService.getAllReservationsByServiceId(serviceId, pageable);
        List<ReservationResource> resources = recommendationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/services/{serviceId}/recommendations/{recommendationId}")
    public ReservationResource getReservationByIdAndServiceId(@PathVariable(name = "serviceId") Long serviceId,
                                                                    @PathVariable(name = "recommendationId") Long recommendationId) {
        return convertToResource(recommendationService.getReservationByIdAndServiceId(serviceId, recommendationId));
    }

    @PostMapping("/services/{serviceId}/recommendations")
    public ReservationResource createReservation(@PathVariable(name = "serviceId") Long serviceId,
                                                       @Valid @RequestBody SaveReservationResource resource) {
        return convertToResource(recommendationService.createReservation(serviceId, convertToEntity(resource)));

    }

    @PutMapping("/services/{serviceId}/recommendations/{recommendationId}")
    public ReservationResource updateReservation(@PathVariable(name = "serviceId") Long serviceId,
                                                       @PathVariable(name = "recommendationId") Long recommendationId,
                                                       @Valid @RequestBody SaveReservationResource resource) {
        return convertToResource(recommendationService.updateReservation(serviceId, recommendationId, convertToEntity(resource)));
    }

    @DeleteMapping("/services/{serviceId}/recommendations/{recommendationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable(name = "serviceId") Long serviceId,
                                                  @PathVariable(name = "recommendationId") Long recommendationId) {
        return recommendationService.deleteReservation(serviceId, recommendationId);
    }

    private Reservation convertToEntity(SaveReservationResource resource) {
        return mapper.map(resource, Reservation.class);
    }

    private ReservationResource convertToResource(Reservation entity) {
        return mapper.map(entity, ReservationResource.class);
    }
}