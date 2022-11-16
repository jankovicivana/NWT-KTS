package nvt.kts.project.service;

import nvt.kts.project.model.Role;
import nvt.kts.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findById(Long id){ return this.roleRepository.findById(id); }

    public List<Role> findByName(String name) { return this.roleRepository.findByName(name); }
}
