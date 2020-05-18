package ru.itis.carsharing.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userCandidate = userRepository.findOneByEmail(email);
        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            return new UserDetailsImpl(user);
        }
        throw new UsernameNotFoundException("user not found");
    }
}
