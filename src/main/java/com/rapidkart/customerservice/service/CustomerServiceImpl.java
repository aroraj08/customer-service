package com.rapidkart.customerservice.service;

import com.rapidkart.customerservice.domain.Address;
import com.rapidkart.customerservice.exceptions.CustomerNotFoundException;
import com.rapidkart.customerservice.mapper.AddressMapper;
import com.rapidkart.customerservice.mapper.CustomerMapper;
import com.rapidkart.model.AddressDto;
import com.rapidkart.model.CustomerDto;
import com.rapidkart.customerservice.domain.Customer;
import com.rapidkart.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper,
                               AddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressMapper = addressMapper;
    }

    /**
     * This method uses Entity Graph to load address data eagerly and avoids N+1 SELECT problem.
     * Using Entity Graph is helpful as it provides you ability to decide which method to load eagerly
     * and which one as lazily (default behavior of one to many). Since, in this case, we knew that we
     * had to always get the addresses for all customers, we went with EAGER loading, otherwise, it is not
     * recommended.
     * @return
     */
    @Override
    @Cacheable(cacheNames = "customers", unless = "#result?.size()==0")
    public Optional<List<CustomerDto>> getCustomers() {

        List<Customer> customerList = new ArrayList<>();
        this.customerRepository.findAll().forEach(customerList :: add);

        // convert Customer list to CustomerDto List using MapStruct
        List<CustomerDto>  customerDtoList = customerList.stream()
                    .map(customerMapper :: customerToCustomerDto)
                    .collect(Collectors.toList());

        return Optional.of(customerDtoList);
    }

    /**
     * As this method uses Lazy Loading (default) to load address data for a customer,
     * @Transactional annotation has been put to avoid LazyInitializationException.
     * The only problem with using it this way is N+1 Select problem, which is OK for this
     * as this method only retrieves data for 1 customer, so there will be two calls
     * @param customerId
     */
    @Override
    @Transactional
    @Cacheable(cacheNames = "customer", key="#customerId")
    public CustomerDto getCustomerById(Long customerId) throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);
        // map customer object to CustomerDto object using MapStruct
        return this.customerMapper.customerToCustomerDto(customer);
    }

    @Override
    @Caching(put = @CachePut(cacheNames = "customer", key = "#customerId"),
            evict = @CacheEvict(cacheNames = "customers", allEntries = true))
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto)
            throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);

        // update Customer domain object with data from Dto and save it back to DB
        customer.setCustomerId(customerId);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());

        Set<AddressDto> addressDtoSet = customerDto.getAddressSet();
        Set<Address> addresses = new HashSet<>();

        if (addressDtoSet != null) {
            addresses  = addressDtoSet.stream()
                                    .map(a -> {
                                       Address address = this.addressMapper.addressDtoToAddress(a);
                                       address.setCustomer(customer);
                                       return address;
                                    }).collect(Collectors.toSet());
        }
        customer.setAddressSet(addresses);

        Customer savedCustomer = this.customerRepository.save(customer);
        return this.customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    @Caching(evict = @CacheEvict(cacheNames = "customers", allEntries = true),
            put = @CachePut(cacheNames = "customer", key = "#result.longValue()",
                    unless = "#result == null"))
    public Long saveCustomer(CustomerDto customerDto) {

        // create customer object using customer Dto object
        Customer customer = this.customerMapper.customerDtoToCustomer(customerDto);

        if (customer.getAddressSet() != null) {
            customer.getAddressSet()
                    .forEach(a -> a.setCustomer(customer));
        }

        // call repository to save customer
        Customer savedCustomer = this.customerRepository.save(customer);
        return savedCustomer.getCustomerId();
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(cacheNames = "customers", allEntries = true),
            @CacheEvict(cacheNames = "customer", key = "#customerId") })
    public void deleteCustomer(Long customerId) {

        this.customerRepository.deleteByCustomerId(customerId);
    }

    private Customer checkIfPresent(Long customerId) throws CustomerNotFoundException {

        Optional<Customer> customerObj = customerRepository.findByCustomerId(customerId);
        return customerObj.orElseThrow(() -> new CustomerNotFoundException("Customer not found : " + customerId));
    }
}
