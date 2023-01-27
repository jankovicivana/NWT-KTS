package nvt.kts.project.service;

import nvt.kts.project.model.Car;
import nvt.kts.project.model.CarType;
import nvt.kts.project.repository.CarRepository;
import nvt.kts.project.repository.CarTypeRepository;
import nvt.kts.project.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CarService {

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private CarRepository carRepository;

    public List<CarType> getCarTypes() {
        return carTypeRepository.findAll();
    }

    public CarType findCarTypeByName(String typeName) {
        List<CarType> types = getCarTypes();
        for (CarType type:types)
        {if (Objects.equals(type.getType(), typeName)) return type;}
        return null;
    }

    public CarType findCarTypeById(String typeId) {
        List<CarType> types = getCarTypes();
        for (CarType type:types)
        {if (Objects.equals(type.getId().toString(), typeId)) return type;}
        return null;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }
}
