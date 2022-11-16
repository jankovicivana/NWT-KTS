package nvt.kts.project.service;

import nvt.kts.project.dto.UserRequest;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Role;
import nvt.kts.project.repository.ClientRepository;
import nvt.kts.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void save(Client client){clientRepository.save(client);}

    public Client save(UserRequest userRequest) {
        Client client = new Client();
        client.setEmail(userRequest.getEmail());
        client.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        client.setName(userRequest.getName());
        client.setSurname(userRequest.getSurname());
        client.setCity(userRequest.getCity());
        client.setCardNumber(userRequest.getCardNumber());
        client.setEnabled(true);
        client.setPhoneNumber(userRequest.getPhoneNumber());
        List<Role> roles = roleRepository.findByName("ROLE_" + userRequest.getRole().toLowerCase(Locale.ROOT));
        client.setRoles(roles);
        client.setPhoto("");

        return this.clientRepository.save(client);
    }
}
