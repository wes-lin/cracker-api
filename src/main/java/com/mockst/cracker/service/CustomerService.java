package com.mockst.cracker.service;

import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.repository.CustomerRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 10:15
 * @Description:
 */
@Service
public class CustomerService extends AbstractService<CustomerEntity>{

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public CustomerEntity register(String phone, String password) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhone(phone);
        //密码两次MD5加密
        String md5password = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        customerEntity.setPassword(md5password);
        customerEntity.setCustomerStatus(CustomerEntity.CustomerStatus.normal);
        return save(customerEntity);
    }

    public CustomerEntity findByPhoneAndPassword(String phone, String password) {
        String md5password = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        return customerRepository.findByPhoneAndPassword(phone,md5password);
    }

    public CustomerEntity findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }
}
