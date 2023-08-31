package ruslan.dobrov.WeatherRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruslan.dobrov.WeatherRestApp.models.Measurement;
import ruslan.dobrov.WeatherRestApp.models.Sensor;
import ruslan.dobrov.WeatherRestApp.repositories.MeasurementRepository;
import ruslan.dobrov.WeatherRestApp.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void save(Measurement measurement) {
        Sensor sensor = measurement.getSensor();
        if (sensor != null) {
            Sensor existingSensor = sensorRepository.findByName(sensor.getName());
            if (existingSensor != null) {
                measurement.setSensor(existingSensor); // Используем существующий объект Sensor
            } else {
                sensorRepository.save(sensor); // Сохраняем новый объект Sensor
            }
        }
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setCreated_at(LocalDateTime.now());
    }

    public List<Measurement> findAllMeasurements() {
        return measurementRepository.findAll();
    }

    public Integer countRainingDays() {
        return measurementRepository.findAllByRainingIsTrue().size();
    }
}
