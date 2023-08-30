package ruslan.dobrov.WeatherRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ruslan.dobrov.WeatherRestApp.models.Sensor;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    Sensor findByName(String name);
}
