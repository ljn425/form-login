package security.formlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.formlogin.domain.entity.Resources;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Resources findByResourceNameAndHttpMethod(String resourceName, String httpMethod);
}
