package nvt.kts.project.service;

import nvt.kts.project.dto.ClientDTO;
import nvt.kts.project.dto.NoteDTO;
import nvt.kts.project.model.Note;
import nvt.kts.project.model.User;
import nvt.kts.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public String formatRole(String role){
        String formatted = role.substring(5);
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1).toLowerCase();
    }

    public boolean activate(String username) {
        User user = findByEmail(username);
        if(user == null || user.isEnabled()){
            return false;
        }
        user.setEnabled(true);
        return true;
    }

}
