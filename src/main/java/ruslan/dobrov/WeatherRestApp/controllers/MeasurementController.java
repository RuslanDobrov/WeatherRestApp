package ruslan.dobrov.WeatherRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ruslan.dobrov.WeatherRestApp.dto.MeasurementDTO;
import ruslan.dobrov.WeatherRestApp.models.Measurement;
import ruslan.dobrov.WeatherRestApp.services.MeasurementService;
import ruslan.dobrov.WeatherRestApp.util.SensorErrorResponse;
import ruslan.dobrov.WeatherRestApp.util.SensorNotCreatedException;
import ruslan.dobrov.WeatherRestApp.util.ValidationError;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ValidationError> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementService.save(measurement);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        List<Measurement> measurements = measurementService.findAllMeasurements();
        return measurements.stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Integer> countRainingDays() {
        Integer count = measurementService.countRainingDays();
        return ResponseEntity.ok(count);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(Exception e) {
        String errorMessage = e instanceof SensorNotCreatedException ?
                e.getMessage() :
                "Sensor with this name is already exist";

        SensorErrorResponse response = new SensorErrorResponse(errorMessage, System.currentTimeMillis());
        return ResponseEntity.badRequest().body(response);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}


/*import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ruslan.dobrov.WeatherRestApp.dto.MeasurementDTO;
import ruslan.dobrov.WeatherRestApp.models.Measurement;
import ruslan.dobrov.WeatherRestApp.services.MeasurementService;
import ruslan.dobrov.WeatherRestApp.services.SensorService;
import ruslan.dobrov.WeatherRestApp.util.SensorNotCreatedException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final SensorService sensorService;
    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(SensorService sensorService, MeasurementService measurementService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotCreatedException(errorMsg.toString());
        }

        Measurement measurement = convertToMeasurement(measurementDTO);

        measurementService.save(measurement);

        // отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAllMeasurements().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList()); //Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/rainyDaysCount")
    public Integer countRainingDays() {
        return measurementService.countRainingDays();
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}*/
