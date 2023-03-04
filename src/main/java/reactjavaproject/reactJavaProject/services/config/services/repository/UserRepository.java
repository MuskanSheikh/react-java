package reactjavaproject.reactJavaProject.services.config.services.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reactjavaproject.reactJavaProject.services.config.services.entity.Users;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);

    Users findByEmailIgnoreCase(String email);
}
