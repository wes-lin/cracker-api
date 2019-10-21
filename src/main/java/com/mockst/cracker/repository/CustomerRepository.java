package com.mockst.cracker.repository;

import com.mockst.cracker.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 20:28
 * @Description:
 */
@Repository
public interface CustomerRepository extends AbstractRepository<CustomerEntity> {
    CustomerEntity findByPhoneAndPassword(String phone, String password);

    CustomerEntity findByPhone(String phone);
}
