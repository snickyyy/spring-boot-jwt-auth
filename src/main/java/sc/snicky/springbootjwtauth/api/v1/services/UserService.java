package sc.snicky.springbootjwtauth.api.v1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sc.snicky.springbootjwtauth.api.v1.domain.models.User;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserAlreadyExistException;
import sc.snicky.springbootjwtauth.api.v1.exceptions.business.users.UserNotFoundException;
import sc.snicky.springbootjwtauth.api.v1.repositories.JpaUserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository jpaUserRepository;

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to search for; must not be {@code null}
     * @return the {@link User} with the given email
     * @throws UserNotFoundException if no user with the given email is found
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user; must not be {@code null}
     * @return the {@link User} with the given id
     * @throws UserNotFoundException if no user with the given id is found
     */
    @Transactional
    public User getUserById(Integer id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Saves a new user to the repository.
     *
     * @param user the {@link User} entity to be saved; must not be {@code null}
     * @throws UserAlreadyExistException if a user with the same email already exists
     */
    @Transactional
    public void saveUser(User user) {
        try {
            jpaUserRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.debug("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistException("User with email " + user.getEmail() + " already exists");
        }
    }
}
