package com.rapidkart.customerservice.domain;

import com.rapidkart.model.AddressType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;

    @Enumerated(value = EnumType.STRING)
    private AddressType addressType;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
