package ruslan.dobrov.WeatherRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruslan.dobrov.WeatherRestApp.models.Sensor;
import ruslan.dobrov.WeatherRestApp.repositories.SensorRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    public Sensor findOne(String name) {
        Optional<Sensor> sensor = sensorRepository.findByName(name);
        return sensor.orElse(null);
    }
}
