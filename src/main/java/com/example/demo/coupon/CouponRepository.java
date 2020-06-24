package com.example.demo.coupon;

import com.example.demo.common.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CouponRepository extends JpaRepository<Coupon, Long>,
		QuerydslPredicateExecutor<Coupon> {
	Coupon findOneByCode(String code);
	Coupon findFirstByStatus(StatusEnum statusEnum);
}
