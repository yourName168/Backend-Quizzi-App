package com.example.quizservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class UrlService {
    @Value("${app.file-storage.base-url}")
    private String configuredBaseUrl;

    @Value("${app.file-storage.base-port}")
    private String configuredBasePort;

    public String getCompleteFileUrl(String filePath, HttpServletRequest request) {
        if (filePath == null || filePath.isEmpty() || filePath.startsWith("http")) {
            return filePath;
        }

        String baseUrlToUse = request != null ? getBaseUrl(request) : configuredBaseUrl;
        return baseUrlToUse + "/files/" + filePath;
    }

    // upgraded-telegram-9v4jgg9jvjjh465-8080.app.github.dev,localhost:8080
    private String getBaseUrl(HttpServletRequest request) {
        String forwardedProtoHeader = request.getHeader("X-Forwarded-Proto");
        String forwardedHostHeader = request.getHeader("X-Forwarded-Host");

        String forwardedProto = forwardedProtoHeader != null ? forwardedProtoHeader.split(",")[0].trim() : null;
        String forwardedHost = forwardedHostHeader != null ? forwardedHostHeader.split(",")[0].trim() : null;

        if (forwardedProto != null && forwardedHost != null) {
            if (configuredBasePort != null && !configuredBasePort.equals("80") && !configuredBasePort.equals("443")) {
                forwardedHost = forwardedHost.replaceAll("-\\d+\\.app\\.github\\.dev", "-" + configuredBasePort + ".app.github.dev");
                forwardedHost = forwardedHost.replace("8080", configuredBasePort);
            }

            return forwardedProto + "://" + forwardedHost;
        }

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String portPart = (serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort;
        return scheme + "://" + serverName + portPart;
    }
}
