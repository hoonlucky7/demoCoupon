package com.example.demo.coupon;

import com.example.demo.common.ErrorCode;
import com.example.demo.common.StatusEnum;
import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.exception.ApiException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.dto.UserDto;
import com.example.demo.common.util.DateUtil;
import com.example.demo.common.util.FormatUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.coupon.QCoupon.coupon;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

	@Autowired
	CouponRepository couponRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<Coupon> findAll() {
		return couponRepository.findAll();
	}

	@Override
	@Transactional
	public List<Coupon> create(Integer count) {
		//TODO : count >= 10000, 쿠폰 발행이 개수가 많아지면 멀티 쓰레딩으로 처리 해야 함
		List<Coupon> couponList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Coupon coupon = new Coupon();
			coupon.setCode("" + System.nanoTime());
			coupon.setExpirationDate(DateUtil.trimPlusDay(3)); // 3일뒤 00:00:00에 만료
			coupon.setStatus(StatusEnum.N);
			couponList.add(coupon);
		}
		return couponRepository.saveAll(couponList);
	}

	@Override
	@Transactional
	public CouponDto allocate(UserDto userDto) {
		Coupon coupon = couponRepository.findFirstByStatus(StatusEnum.N);
		User user = userRepository.findOneByEmail(userDto.getEmail());
		coupon.setUser(user);
		coupon.setStatus(StatusEnum.A);
		couponRepository.save(coupon);

		CouponDto couponDto = new CouponDto();
		couponDto.setCode(coupon.getCode());
		return couponDto;
	}

	@Override
	public List<Coupon> getCouponsByEmail(String email) {
		if (!FormatUtil.isEmailFormat(email)) {
			throw new ApiException(ErrorCode.COUPON_INVALID_EMAIL);
		}
		User user = userRepository.findOneByEmail(email);
		return user.getCoupons();
	}

	@Override
	@Transactional
	public Coupon useCoupon(String code) {
		Coupon coupon = couponRepository.findOneByCode(code);
		validCoupon(coupon);
		coupon.setUseDate(new Date());
		coupon.setStatus(StatusEnum.Y);
		return couponRepository.save(coupon);
	}

	@Override
	@Transactional
	public Coupon cancelCoupon(String code) {
		Coupon coupon = couponRepository.findOneByCode(code);
		validCoupon(coupon);
		coupon.setStatus(StatusEnum.N);
		return couponRepository.save(coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Coupon> getExpiredByToday(Pageable pageable) {
		BooleanExpression predicate = coupon.isNotNull()
				.and(coupon.expirationDate.eq(DateUtil.trimPlusDay(0)));

		return couponRepository.findAll(predicate, pageable);
	}

	@Override
	public void sendExpiredAfter3days() {
		Pageable wholePage = Pageable.unpaged(); //TODO: find by Page
		Page<Coupon> coupons = getExpiredAfter3days(wholePage);
		for (Coupon coupon : coupons) {
			if (coupon.getUser() != null) {
				log.info("쿠폰이 3일 후 만료됩니다. to {}", coupon.getUser().getEmail());
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Coupon> getExpiredAfter3days(Pageable pageable) {
		BooleanExpression predicate = coupon.isNotNull()
				.and(coupon.expirationDate.eq(DateUtil.trimPlusDay(3))
				.and(coupon.user.isNotNull()));

		return couponRepository.findAll(predicate, pageable);
	}

	private void validCoupon(Coupon coupon) {
		if (coupon == null) {
			throw new ApiException(ErrorCode.COUPON_NOT_FOUND);
		} else if (coupon.getStatus() == StatusEnum.E) {
			throw new ApiException(ErrorCode.COUPON_EXPIRED_CODE);
		} else if (coupon.getStatus() == StatusEnum.Y) {
			throw new ApiException(ErrorCode.COUPON_ALREADY_USE_COUPON);
		}
	}
}
