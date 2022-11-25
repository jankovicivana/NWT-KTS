package nvt.kts.project.service;

import nvt.kts.project.model.User;
import nvt.kts.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public String formatRole(String role){
        String formatted = role.substring(5);
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1).toLowerCase();
    }

    public boolean isPasswordCorrect(String oldPass, String password) {
        return passwordEncoder.encode(oldPass).contentEquals(password);

    public boolean activate(String username) {
        User user = findByEmail(username);
        if(user == null || user.isEnabled()){
            System.out.println("VEC JE ENABLED ILI NE POSTOJI");
            return false;
        }
        user.setEnabled(true);
        return true;

    }

}
