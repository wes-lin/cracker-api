package com.mockst.cracker.service;

import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.entity.CustomerWordEntity;
import com.mockst.cracker.entity.WordBookEntity;
import com.mockst.cracker.entity.WordSceneEntity;
import com.mockst.cracker.entity.em.BookTypeEnum;
import com.mockst.cracker.model.WordBook;
import com.mockst.cracker.repository.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 10:15
 * @Description:
 */
@Service
public class CustomerService extends AbstractService<CustomerEntity>{

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WordSceneRepository wordSceneRepository;
    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private CustomerWordRepository customerWordRepository;

    @Transactional
    public CustomerEntity register(String phone, String password) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhone(phone);
        //密码两次MD5加密
        String md5password = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        customerEntity.setPassword(md5password);
        customerEntity.setCustomerStatus(CustomerEntity.CustomerStatus.normal);
        //创建词本
        //"insert tb_word_book(id,bookName,customerId,sceneId,lockStatus,bookType)";
        CustomerEntity DB = save(customerEntity);
        List<WordSceneEntity> scenes = wordSceneRepository.findAll(new Sort(Sort.Direction.ASC,"sort"));
        List<WordBookEntity> works = new ArrayList<>();
        scenes.forEach(s->{
            for (int i = 0; i < BookTypeEnum.values().length; i++) {
                BookTypeEnum bookType = BookTypeEnum.values()[i];
                WordBookEntity wordBookEntity = new WordBookEntity();
                wordBookEntity.setCustomerId(DB.getId());
                wordBookEntity.setSceneId(s.getId());
                wordBookEntity.setBookName(s.getSceneName());
                //暂时开放所有
                wordBookEntity.setLockStatus(WordBookEntity.LockStatus.unlocked);
                wordBookEntity.setBookType(bookType);
                works.add(wordBookEntity);
            }
        });
        wordBookRepository.saveAll(works);
        return DB;
    }

    public CustomerEntity findByPhoneAndPassword(String phone, String password) {
        String md5password = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        return customerRepository.findByPhoneAndPassword(phone,md5password);
    }

    public List<WordBook> findCustomerBooks(String customerId,BookTypeEnum bookType){
        List<WordBookEntity> books = wordBookRepository.findByBookTypeAndCustomerId(bookType,customerId);
        List<WordBook> bookList = books.stream().map(b->{
            WordBook wordBook = new WordBook();
            wordBook.setBookId(b.getId());
            wordBook.setBookName(b.getBookName());
            wordBook.setLockStatus(b.getLockStatus().name());
            //解锁状态查询所有
            if (b.getLockStatus()==WordBookEntity.LockStatus.unlocked){
                wordBook.setTotal(wordRepository.countByBookTypeAndSceneId(bookType,b.getSceneId()));
                List<CustomerWordEntity> customerWords = customerWordRepository.findByBookId(b.getId());
                wordBook.setStudyQuantity(customerWords.size());
                wordBook.setFullLevelQuantity(customerWords.stream().filter(c->c.getLevel()==6).count());
            }
            return wordBook;
        }).collect(Collectors.toList());

        return bookList;
    }

    public CustomerEntity findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }
}
