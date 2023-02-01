package nvt.kts.project.service;

import nvt.kts.project.dto.UserRequest;
import nvt.kts.project.model.Admin;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.ClientDrive;
import nvt.kts.project.model.Role;
import nvt.kts.project.repository.ClientRepository;
import nvt.kts.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void save(Client client){clientRepository.save(client);}

    public Client save(UserRequest userRequest) {
        Client client = new Client();
        client.setEmail(userRequest.getEmail());
        client.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        client.setName(userRequest.getName());
        client.setSurname(userRequest.getSurname());
        client.setCity(userRequest.getCity());
        client.setIsSocialLogin(userRequest.getIsSocialLogin());
        client.setEnabled(userRequest.getIsSocialLogin()); // ako je preko googla odmah je enabled
        client.setTokens(userRequest.getTokens());
        client.setPhoneNumber(userRequest.getPhoneNumber());
        List<Role> roles = roleRepository.findByName("ROLE_" + userRequest.getRole().toLowerCase(Locale.ROOT));
        client.setRoles(roles);
        client.setPhoto(userRequest.getPhoto());

        return this.clientRepository.save(client);
    }

    public Client findClientById(Long id){
        return clientRepository.findClientById(id);
    }
    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }


    public List<Client> getClients(Pageable pageable, HttpHeaders hh){
        Page<Client> clients =clientRepository.findAllByDeletedIsFalse(pageable);
        hh.add("Total-items", Long.toString(clients.getTotalElements()));
        return clients.getContent();
    }
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    public void setRole(Client client) {
        List<Role> roles = roleRepository.findByName("ROLE_client");
        client.setRoles(roles);
    }

    public void setClientsDriving(List<ClientDrive> passengers,Boolean status) {
        for (ClientDrive cd: passengers){
            cd.getClient().setDriving(status);
            clientRepository.save(cd.getClient());
        }
    }
}
