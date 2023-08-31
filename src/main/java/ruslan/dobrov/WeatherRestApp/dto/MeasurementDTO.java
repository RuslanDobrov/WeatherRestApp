package ruslan.dobrov.WeatherRestApp.dto;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Range;
import ruslan.dobrov.WeatherRestApp.models.Sensor;
import java.time.LocalDateTime;

public class MeasurementDTO {

    @NotNull
    @Range(min = -100, max = 100, message = "Temperature must be between -100 and 100")
    private float value;

    @NotNull
    private boolean raining;

    @NotNull
    private LocalDateTime created_at;

    @NotNull
    private Sensor sensor;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
