package ruslan.dobrov.WeatherRestApp.util;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class TemperatureChart {

    public static void main(String[] args) {
        List<MeasurementXChart> measurements = getMeasurementsFromServer();

        // Данные для графика
        List<Integer> xData = measurements.stream().map(MeasurementXChart::getX).collect(Collectors.toList());
        List<Float> yData = measurements.stream().map(MeasurementXChart::getY).collect(Collectors.toList());

        // Создание графика
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Temperature Chart")
                .xAxisTitle("Measurement")
                .yAxisTitle("Temperature")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        chart.addSeries("Temperature", xData, yData);

        // График
        new SwingWrapper<>(chart).displayChart();
    }

    // Метод для получения данных из базы данных
    private static List<MeasurementXChart> getMeasurementsFromServer() {
        RestTemplate restTemplate = new RestTemplate();

        String sensorRegistrationURL = "http://localhost:8080/measurements/chart";

        // ParameterizedTypeReference для указания ожидаемого типа
        ResponseEntity<List<MeasurementXChart>> responseEntity = restTemplate.exchange(
                sensorRegistrationURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MeasurementXChart>>() {}
        );

        List<MeasurementXChart> response = responseEntity.getBody();
        return response;
    }
}

