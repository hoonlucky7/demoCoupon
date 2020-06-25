package com.example.demo.coupon;

import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.user.dto.EmailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {

	List<Coupon> findAll();

	List<Coupon> create(Integer count);

	CouponDto allocate(EmailDto emailDto);

	List<Coupon> getCouponsByEmail(String email);

	Coupon useCoupon(String code);

	Coupon cancelCoupon(String code);

	void updateExpiredByToday();

	Page<Coupon> getExpiredAfter3days(Pageable pageable);

	void sendExpiredAfter3days();

	Page<Coupon> getExpiredByToday(Pageable pageable);
}
