package nvt.kts.project.service;
import nvt.kts.project.repository.SystemInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemInfoService {

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    public Double getTokenPrice(){
        return systemInfoRepository.getSystemInfo().getTokenPrice();
    }

}
