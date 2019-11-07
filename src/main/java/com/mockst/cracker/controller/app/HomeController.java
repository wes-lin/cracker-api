package com.mockst.cracker.controller.app;

import com.mockst.cracker.config.WebConfigurer;
import com.mockst.cracker.entity.em.BookTypeEnum;
import com.mockst.cracker.model.WordBook;
import com.mockst.cracker.model.WordBookType;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 21:00
 * @Description:
 */
@RestController
@RequestMapping(WebConfigurer.APP_BASE_PATH + "/home")
public class HomeController extends AbstractController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("bookType")
    public APIResult bookType() {
        BookTypeEnum[] bookTypeEnums = BookTypeEnum.values();
        List<WordBookType> wordTypes = new ArrayList<>(bookTypeEnums.length);
        for (int i = 0; i < bookTypeEnums.length; i++) {
            BookTypeEnum bookTypeEnum = bookTypeEnums[i];
            WordBookType wordBookType = new WordBookType();
            wordBookType.setBookType(bookTypeEnum.name());
            wordBookType.setTitle(BookTypeEnum.getTitle(bookTypeEnum));
            wordTypes.add(wordBookType);
        }
        return APIResultUtil.returnSuccessResult(wordTypes);
    }

    @RequestMapping("books")
    public APIResult books(@RequestParam String customerId, @RequestParam String bookType) {
        List<WordBook> books = customerService.findCustomerBooks(customerId, BookTypeEnum.valueOf(bookType));
        return APIResultUtil.returnSuccessResult(books);
    }

}
