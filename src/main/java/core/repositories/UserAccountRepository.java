package core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.models.entities.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
