package core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.models.entities.AssetAccount;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long> {
}
