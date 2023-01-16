package nvt.kts.project.service;

import nvt.kts.project.model.Drive;
import nvt.kts.project.repository.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriveService {

    @Autowired
    private DriveRepository driveRepository;

    public List<Drive> getCurrentDrives() {
        return driveRepository.getCurrentDrives(LocalDateTime.now());
    }

    public Drive findById(Long id) {
        return driveRepository.findById(id).orElse(null);
    }
}
