package com.rapidkart.customerservice;

import com.rapidkart.customerservice.domain.Address;
import com.rapidkart.customerservice.domain.Customer;
import com.rapidkart.customerservice.model.AddressType;
import com.rapidkart.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableCaching
public class RapidkartCustomerServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(RapidkartCustomerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RapidkartCustomerServiceApplication.class, args);
    }

    @Bean
    @Profile({"local-discovery", "local"})
    public CommandLineRunner setupData(CustomerRepository customerRepository) {

        return (args) -> {

            logger.info("saving dummy customers");

            customerRepository.save(Customer.builder()
                    .firstName("Kapil")
                    .lastName("Arora")
                    .isLegitimateUser(true)
                    .build());

            Customer c2 = Customer.builder().firstName("Preeti").lastName("Miglani")
                    .isLegitimateUser(true)
                    .build();

            Address homeAddress1 = Address.builder().address1("address1_1").address2("address1_2")
                    .city("El Segundo").state("CA").addressType(AddressType.HOME)
                    .build();

            Address homeAddress2 = Address.builder().address1("address2_1").address2("address2_2")
                    .city("El Segundo").state("CA").addressType(AddressType.OFFICE)
                    .build();

            c2.addAddress(homeAddress1);
            c2.addAddress(homeAddress2);
            customerRepository.save(c2);

            Customer c3 = Customer.builder().firstName("Jatin").lastName("Arora")
                    .isLegitimateUser(false).build();

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
