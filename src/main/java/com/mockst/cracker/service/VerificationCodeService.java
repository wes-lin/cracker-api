package com.mockst.cracker.service;

import com.mockst.cracker.entity.VerificationCodeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 13:06
 * @Description:
 */
@Service
public class VerificationCodeService extends AbstractService<VerificationCodeEntity> {

    /**
     * 是否发送过短信
     *
     * @param phone
     * @param codeType
     * @return
     */
    public boolean isSend(String phone, String codeType) {
//        String sql = "select count(*) from tb_verification_code WHERE code_type=? AND code_status=? AND phone=? AND created_date>?";
//        Object[] args = {
//                codeType,VerificationCodeEntity.CodeStatus.unexpired,phone,now
//        };
//        int count = jdbcTemplate.queryForObject(sql, Integer.class,args);
//        return count > 0;
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
        Specification<VerificationCodeEntity> specification = (Root<VerificationCodeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("codeType").as(String.class), codeType));
            predicates.add(criteriaBuilder.equal(root.get("codeStatus").as(VerificationCodeEntity.CodeStatus.class), VerificationCodeEntity.CodeStatus.unexpired));
            predicates.add(criteriaBuilder.equal(root.get("phone").as(String.class), phone));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate").as(LocalDateTime.class), now));
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
            query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            return query.getRestriction();
        };
        return repository.count(specification) > 0;
    }

    /**
     * 校验手机验证码(有效则更新状态)
     *
     * @param phone
     * @param codeType
     * @param smsCode
     * @return
     */
    public VerificationCodeEntity checkSmsCode(String phone, String codeType, String smsCode) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
//        String sql = "SELECT * from tb_verification_code WHERE code_type=? AND code_status=? AND phone=? AND created_date>? AND code=? ORDER BY created_date DESC LIMIT 1";
//        Object[] args = {
//                codeType,VerificationCodeEntity.CodeStatus.unexpired,phone,now,smsCode
//        };
//        List<VerificationCodeEntity> list = jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(VerificationCodeEntity.class));
//        return list.isEmpty()?null:list.get(0);
        Specification<VerificationCodeEntity> specification = (Root<VerificationCodeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("codeType").as(String.class), codeType));
            predicates.add(criteriaBuilder.equal(root.get("codeStatus").as(VerificationCodeEntity.CodeStatus.class), VerificationCodeEntity.CodeStatus.unexpired));
            predicates.add(criteriaBuilder.equal(root.get("phone").as(String.class), phone));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate").as(LocalDateTime.class), now));
            predicates.add(criteriaBuilder.equal(root.get("code").as(String.class), smsCode));
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
            query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            return query.getRestriction();
        };
        List<VerificationCodeEntity> list = repository.findAll(specification);
        return list.isEmpty() ? null : list.get(0);

    }

}
