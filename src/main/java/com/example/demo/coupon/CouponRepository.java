package com.example.demo.coupon;

import com.example.demo.common.StatusEnum;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>,
		QuerydslPredicateExecutor<Coupon> {
	Coupon findFirstByCode(String code);
	Coupon findFirstByStatus(StatusEnum statusEnum);
	List<Coupon> findAll(Predicate predicate);
}
