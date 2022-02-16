package by.skilsoft.jh.courses.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CheckUserStatusUtil {

    private static final String ADMIN = "admin";

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static Boolean userHasAdminStatus(Authentication authentication) {
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        List<String> authoritiesStrings = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return authentication.getName().equalsIgnoreCase(ADMIN) || authoritiesStrings.contains(ROLE_ADMIN);
    }
}
