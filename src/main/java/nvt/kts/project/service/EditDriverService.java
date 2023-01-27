package nvt.kts.project.service;

import nvt.kts.project.dto.DriverCarDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.EditDriver;
import nvt.kts.project.repository.EditDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditDriverService {

    @Autowired
    private EditDriverRepository editDriverRepository;

    public void save(EditDriver e) {
        editDriverRepository.save(e);
    }

    public List<EditDriver> getPendingDriverChanges(Pageable pageable, HttpHeaders hh) {
        Page<EditDriver> clients =editDriverRepository.findAllByStatus(pageable);
        hh.add("Total-items", Long.toString(clients.getTotalElements()));
        return clients.getContent();
    }

    public EditDriver getDriverChangesById(Long id) {
        return editDriverRepository.getReferenceById(id);
    }
}
