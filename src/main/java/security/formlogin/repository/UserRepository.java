package security.formlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.formlogin.domain.Account;
@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

}
