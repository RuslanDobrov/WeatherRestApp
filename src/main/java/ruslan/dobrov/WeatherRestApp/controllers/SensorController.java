package ruslan.dobrov.WeatherRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ruslan.dobrov.WeatherRestApp.dto.SensorDTO;
import ruslan.dobrov.WeatherRestApp.models.Sensor;
import ruslan.dobrov.WeatherRestApp.services.SensorService;
import ruslan.dobrov.WeatherRestApp.util.SensorErrorResponse;
import ruslan.dobrov.WeatherRestApp.util.SensorIsAlreadyExist;
import ruslan.dobrov.WeatherRestApp.util.SensorNotCreatedException;
import ruslan.dobrov.WeatherRestApp.util.ValidationError;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    public final ModelMapper modelMapper;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ValidationError> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        if (sensorService.findByName(sensorDTO.getName()) != null) {
            return ResponseEntity.badRequest().body("Sensor with this name is already exist");
        }

        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        sensorService.save(sensor);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({SensorNotCreatedException.class, SensorIsAlreadyExist.class})
    private ResponseEntity<SensorErrorResponse> handleException(Exception e) {
        String errorMessage = e instanceof SensorNotCreatedException ?
                e.getMessage() :
                "Sensor with this name is already exist";

        SensorErrorResponse response = new SensorErrorResponse(errorMessage, System.currentTimeMillis());
        return ResponseEntity.badRequest().body(response);
    }
}
