package by.skilsoft.jh.courses.config;

import by.skilsoft.jh.courses.domain.CustomObjectIdentity;
import by.skilsoft.jh.courses.service.HTTPService;
import by.skilsoft.jh.courses.service.dto.CheckPermissionDto;
import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.core.Authentication;

@Configuration
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(getClass());

    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();

    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();

    private final HTTPService httpService;

    public AclPermissionEvaluator(HTTPService httpService) {
        this.httpService = httpService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }

        if (domainObject instanceof Optional) {
            domainObject = ((Optional<?>) domainObject).get();
        }

        ObjectIdentity objectIdentity = this.objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);

        return httpService.checkPermissionEntry(
            new CheckPermissionDto(new CustomObjectIdentity(objectIdentity.getIdentifier(), objectIdentity.getType()), permission)
        );
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator.createObjectIdentity(targetId, targetType);
        return httpService.checkPermissionEntry(
            new CheckPermissionDto(new CustomObjectIdentity(objectIdentity.getIdentifier(), objectIdentity.getType()), permission)
        );
    }

    public void setObjectIdentityRetrievalStrategy(ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    public void setObjectIdentityGenerator(ObjectIdentityGenerator objectIdentityGenerator) {
        this.objectIdentityGenerator = objectIdentityGenerator;
    }
}
