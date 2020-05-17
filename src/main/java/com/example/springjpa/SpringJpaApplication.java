package com.example.springjpa;

import com.example.springjpa.domain.Customer;
import com.example.springjpa.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SpringJpaApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner setupData(CustomerRepository customerRepository) {

        return (args) -> {

            logger.info("saving dummy customers");

            customerRepository.save(Customer.builder()
                    .firstName("Kapil")
                    .lastName("Arora")
                    .build());

            customerRepository.save(Customer.builder()
                    .firstName("Preeti")
                    .lastName("Miglani")
                    .build());

            customerRepository.save(Customer.builder()
                    .firstName("Jatin")
                    .lastName("Arora")
                    .build());

            /*logger.info("Fetching all customers");
            Iterable<Customer> it = customerRepository.findAll();
            it.forEach(c -> System.out.println(c));

            logger.info("fetch customer by Id");
            List<Customer> list = customerRepository.findByLastName("Arora");
            list.forEach(c -> System.out.println(c.getFirstName()));*/

        };
    }

}
