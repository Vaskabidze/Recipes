package recipes.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.persistence.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));
        return user.map(MyUserDetails::new).get();
    }

    /**
     * Return user by ID from DB
     *
     * @param id User ID
     * @throws ResponseStatusException is user not found in the database (HttpStatus.NOT_FOUND)
     */
    public void deleteUser(Long id) throws ResponseStatusException {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    /**
     * Search for a user in the database by email
     *
     * @param email User email
     * @return User with specified email
     * @throws UsernameNotFoundException if email not found
     */
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));
        return user.get();
    }

    /**
     * Save a new user to the DB
     *
     * @param user User for saving
     * @throws ResponseStatusException - if the user is already in the database (HttpStatus.BAD_REQUEST)
     */
    public void saveUser(User user) throws ResponseStatusException {
        if (emailExisting(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Check if the user is in the database
     *
     * @param email User email
     * @return if there a user in the database - return true, otherwise - return false
     */
    private boolean emailExisting(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
