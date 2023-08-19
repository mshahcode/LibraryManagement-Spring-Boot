package az.library.management.service;

import az.library.management.dao.entity.CustomUserDetails;
import az.library.management.dao.entity.User;
import az.library.management.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user exist with such email!"));

        return new CustomUserDetails(user);
    }
}
