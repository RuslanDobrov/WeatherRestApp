package ruslan.dobrov.WeatherRestApp.dto;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Range;

public class MeasurementDTO {

    @NotNull
    @Range(min = -100, max = 100, message = "Temperature must be between -100 and 100")
    private float value;

    @NotNull
    private boolean raining;

    @NotNull
    private SensorDTO sensor;

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

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
