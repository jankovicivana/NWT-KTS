package nvt.kts.project.service;

import nvt.kts.project.model.Admin;
import nvt.kts.project.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

//    public Admin findAdminById(Long id){
//        return adminRepository.findAdminById(id);
//    }

}
