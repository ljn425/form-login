package security.formlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import security.formlogin.domain.entity.Resources;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Resources findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

    List<Resources> findAllByOrderByOrderNumDesc();
}
