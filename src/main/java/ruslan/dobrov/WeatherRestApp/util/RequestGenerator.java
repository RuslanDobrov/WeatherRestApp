package ruslan.dobrov.WeatherRestApp.util;

import org.springframework.web.client.RestTemplate;
import ruslan.dobrov.WeatherRestApp.dto.MeasurementDTO;
import ruslan.dobrov.WeatherRestApp.dto.SensorDTO;

import java.util.Random;

public class RequestGenerator {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String sensorRegistrationURL = "http://localhost:8080/sensors/registration";
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("Sensor name 6");
        String response = restTemplate.postForObject(sensorRegistrationURL, sensorDTO, String.class);
        System.out.println(response);

        System.out.println("=======================================================================");

        String measurementAddURL = "http://localhost:8080/measurements/add";
        MeasurementDTO measurementDTO = new MeasurementDTO();
        Random random = new Random();
        // Генерируем случайное вещественное число в диапазоне от -100 до 100


        // Округляем число до одной цифры после запятой
        for (int i = 0; i < 1; i++) {
            double randomValue = random.nextDouble() * 200 - 100;
            measurementDTO.setValue((float) (Math.round(randomValue * 10.0) / 10.0));
            measurementDTO.setRaining(random.nextBoolean());
            measurementDTO.setSensor(sensorDTO);
            response = restTemplate.postForObject(measurementAddURL, measurementDTO, String.class);
            System.out.println(response);
        }

        System.out.println("=======================================================================");

        String measurementGetURL = "http://localhost:8080/measurements";
        response = restTemplate.getForObject(measurementGetURL, String.class);
        System.out.println(response);
    }
}
