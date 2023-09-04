package ruslan.dobrov.WeatherRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ruslan.dobrov.WeatherRestApp.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    Integer countByRainingIsTrue();
}
