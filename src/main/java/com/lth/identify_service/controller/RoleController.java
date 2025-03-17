package com.lth.identify_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lth.identify_service.dto.request.RoleRequest;
import com.lth.identify_service.dto.response.ApiResponse;
import com.lth.identify_service.dto.response.RoleResponse;
import com.lth.identify_service.service.RoleService;

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


@RequestMapping("/roles")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> response = new ApiResponse<RoleResponse>(201, "Role created successfully", roleService.create(roleRequest));
        return response;
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> response = new ApiResponse<List<RoleResponse>>(200, "Roles retrieved successfully", roleService.getAll());
        return response;
    }

    @DeleteMapping("/{roleName}")
    ApiResponse<String> delete(@PathVariable String roleName) {
        roleService.delete(roleName);
        ApiResponse<String> response = new ApiResponse<String>(200, "Role deleted successfully", null);
        return response;
    }
}
