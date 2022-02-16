package by.skilsoft.jh.courses.service.impl;

import by.skilsoft.jh.courses.config.Constants;
import by.skilsoft.jh.courses.domain.PermissionForObject;
import by.skilsoft.jh.courses.security.jwt.TokenProvider;
import by.skilsoft.jh.courses.service.HTTPService;
import by.skilsoft.jh.courses.service.dto.CheckPermissionDto;
import by.skilsoft.jh.courses.service.dto.PermissionDto;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HTTPServiceImpl implements HTTPService {

    private final Logger log = LoggerFactory.getLogger(HTTPServiceImpl.class);

    private final TokenProvider tokenProvider;

    public HTTPServiceImpl(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public List<PermissionForObject> getEntityIds(String entityName) {
        RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<PermissionForObject[]> response = restTemplate.exchange(
            Constants.MAIN_ACL_URL + "/api/get-acl-entries?objE=" + entityName,
            HttpMethod.GET,
            entity,
            PermissionForObject[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public Boolean checkPermissionEntry(CheckPermissionDto checkPermissionDto) {
        RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<CheckPermissionDto> entity = new HttpEntity<>(checkPermissionDto, getHeaders());
        ResponseEntity<Boolean> response = restTemplate.exchange(Constants.CHECK_PERMISSION_URL, HttpMethod.POST, entity, Boolean.class);
        return response.getBody();
    }

    @Override
    public void addPermissionForUser(List<PermissionDto> permissionDto) {
        RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<List<PermissionDto>> entity = new HttpEntity<>(permissionDto, getHeaders());
        restTemplate.exchange(Constants.ADD_PERMISSION_FOR_USER, HttpMethod.POST, entity, String.class);
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tokenProvider.createToken(authentication, false);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());
        headers.set("X-TENANT-ID", Constants.TENANT_ID);
        return headers;
    }
}
