package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.model.User;
import nvt.kts.project.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/system")
public class SystemInfoController {

    @Autowired
    private SystemInfoService systemInfoService;


    @GetMapping("/getTokenPrice")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<Double> getTokenPrice() {
        Double price = systemInfoService.getTokenPrice();
        System.out.print("cijena jeeee " +price);
        return new ResponseEntity<>(price,HttpStatus.OK);
    }

}
