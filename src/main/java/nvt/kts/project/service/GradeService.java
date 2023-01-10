package nvt.kts.project.service;

import nvt.kts.project.model.Grade;
import nvt.kts.project.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;


    public Grade getGrade(Long driveId, Long clientId) {
        return gradeRepository.getGrade(clientId,driveId);
    }

    public void save(Grade g) {
        gradeRepository.save(g);
    }
}
