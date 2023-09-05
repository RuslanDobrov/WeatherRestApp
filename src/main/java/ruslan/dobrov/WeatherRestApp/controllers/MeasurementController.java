package ruslan.dobrov.WeatherRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ruslan.dobrov.WeatherRestApp.dto.MeasurementDTO;
import ruslan.dobrov.WeatherRestApp.models.Measurement;
import ruslan.dobrov.WeatherRestApp.services.MeasurementService;
import ruslan.dobrov.WeatherRestApp.util.MeasurementXChart;
import ruslan.dobrov.WeatherRestApp.util.SensorErrorResponse;
import ruslan.dobrov.WeatherRestApp.util.SensorNotCreatedException;
import ruslan.dobrov.WeatherRestApp.util.ValidationError;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/chart")
    public List<MeasurementXChart> getChartMeasurements() {
        // Здесь получите данные из базы данных, используя MeasurementService или MeasurementRepository
        List<Measurement> measurements = measurementService.findAllMeasurements();

        // Преобразуйте данные в формат, подходящий для построения графика, например, в список x и y значений
        List<Integer> xData = new ArrayList<>();
        List<Float> yData = new ArrayList<>();

        for (Measurement measurement : measurements) {
            xData.add(measurement.getId()); // Используйте нужное поле для оси x
            yData.add(measurement.getValue()); // Используйте нужное поле для оси y
        }

        // Верните данные в виде списка MeasurementDTO (или другого формата, если необходимо)
        List<MeasurementXChart> measurementXCharts = new ArrayList<>();

        for (int i = 0; i < xData.size(); i++) {
            MeasurementXChart measurementXChart = new MeasurementXChart();
            measurementXChart.setX(xData.get(i));
            measurementXChart.setY(yData.get(i));
            measurementXCharts.add(measurementXChart);
        }

        return measurementXCharts;
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
