package ruslan.dobrov.WeatherRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ruslan.dobrov.WeatherRestApp.models.Measurement;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findAllByRainingIsTrue();
}
