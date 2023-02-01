package nvt.kts.project.service;

import nvt.kts.project.model.Report;
import nvt.kts.project.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;


    public void save(Report r) {
        reportRepository.save(r);
    }
}
