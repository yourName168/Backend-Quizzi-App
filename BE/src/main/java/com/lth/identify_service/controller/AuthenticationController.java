package com.lth.identify_service.controller;

import com.lth.identify_service.dto.request.AuthenticationRequest;
import com.lth.identify_service.dto.request.IntrospectRequest;
import com.lth.identify_service.dto.response.ApiResponse;
import com.lth.identify_service.dto.response.AuthenticationResponse;
import com.lth.identify_service.dto.response.IntrospectResponse;
import com.lth.identify_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse = authenticationService.authenticate(request);
        ApiResponse response = new ApiResponse(200, null, authResponse);
        return response;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspect = authenticationService.introspect(request);
        ApiResponse response = new ApiResponse(200, null, introspect);
        return response;
    }
}
