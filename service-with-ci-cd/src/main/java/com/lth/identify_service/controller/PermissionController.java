package com.lth.identify_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lth.identify_service.dto.request.PermissionRequest;
import com.lth.identify_service.dto.response.ApiResponse;
import com.lth.identify_service.dto.response.PermissionResponse;
import com.lth.identify_service.service.PermissionService;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/permissions")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        ApiResponse<PermissionResponse> response = new ApiResponse<PermissionResponse>(201, "Permission created successfully", permissionService.create(permissionRequest));
        return response;
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        ApiResponse<List<PermissionResponse>> response = new ApiResponse<List<PermissionResponse>>(200, "Permissions retrieved successfully", permissionService.getAll());
        return response;
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<String> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        ApiResponse<String> response = new ApiResponse<String>(200, "Permission deleted successfully", null);
        return response;
    }
    

}
