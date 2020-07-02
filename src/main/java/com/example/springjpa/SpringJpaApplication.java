package com.example.springjpa;

import com.example.springjpa.domain.Address;
import com.example.springjpa.domain.Customer;
import com.example.springjpa.model.AddressType;
import com.example.springjpa.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringJpaApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaApplication.class, args);
    }

    @Bean
    @Profile("local-discovery")
    public CommandLineRunner setupData(CustomerRepository customerRepository) {

        return (args) -> {

            logger.info("saving dummy customers");

            customerRepository.save(Customer.builder()
                    .firstName("Kapil")
                    .lastName("Arora")
                    .build());

            Customer c2 = Customer.builder().firstName("Preeti").lastName("Miglani").build();
            Address homeAddress1 = Address.builder().address1("address1_1").address2("address1_2")
                    .city("El Segundo").state("CA").addressType(AddressType.HOME)
                    .build();

            Address homeAddress2 = Address.builder().address1("address2_1").address2("address2_2")
                    .city("El Segundo").state("CA").addressType(AddressType.OFFICE)
                    .build();

            c2.addAddress(homeAddress1);
            c2.addAddress(homeAddress2);
            customerRepository.save(c2);

            Customer c3 = Customer.builder().firstName("Jatin").lastName("Arora").build();

            Address homeAddress3 = Address.builder().address1("address1_1").address2("address1_2")
                    .city("El Segundo").state("CA").addressType(AddressType.HOME)
                    .build();

            Address homeAddress4 = Address.builder().address1("address2_1").address2("address2_2")
                    .city("El Segundo").state("CA").addressType(AddressType.OFFICE)
                    .build();

            c3.addAddress(homeAddress3);
            c3.addAddress(homeAddress4);
            customerRepository.save(c3);
        };
    }
}
