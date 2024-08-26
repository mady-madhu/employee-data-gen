package com.example.employee_data_gen.service;



import com.example.employee_data_gen.config.KafkaProducerProps;
import com.example.employee_data_gen.model.Employee;
import com.github.javafaker.Faker;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;


@Service
public class EmployeeProducerService {

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    private KafkaProducerProps kafkaProducerProps;

    private final Faker faker = new Faker();

    @Scheduled(fixedRate = 5000)
    public void sendEmployee() {
        Employee employee = generateEmployee();
        byte[] serializedEmployee = serializeAvro(employee);
        kafkaTemplate.send(kafkaProducerProps.getTopicName(), employee.getId().toString(), serializedEmployee);
    }



    public Employee generateEmployee() {
        return new Employee(
                UUID.randomUUID().toString(),   // Generates a random UUID for the employee ID
                faker.name().fullName(),        // Generates a random full name
                faker.number().numberBetween(18, 65),  // Generates a random age between 18 and 65
                faker.company().industry()      // Generates a random industry name
        );
    }



    private static byte[] serializeAvro(Employee employee) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DatumWriter<Employee> datumWriter = new SpecificDatumWriter<>(Employee.class);
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

        try {
            datumWriter.write(employee, encoder);
            encoder.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

}
