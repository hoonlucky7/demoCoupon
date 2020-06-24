package com.example.demo.coupon;

import com.example.demo.coupon.dto.CountDto;
import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.user.dto.UserDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/coupon")
@Slf4j
public class CouponApiController {

	@Autowired
	CouponService couponService;

	@PostMapping("/user/coupon")
	public List<CouponDto> getCouponByEmail(@RequestBody UserDto userDto) {
		List<Coupon> coupons = couponService.getCouponsByEmail(userDto.getEmail());

		List<CouponDto> couponDtos = new ArrayList<>();
		for (Coupon coupon : coupons) {
			CouponDto couponDto = new CouponDto();
			couponDto.setCode(coupon.getCode());
			couponDtos.add(couponDto);
		}
		return couponDtos;
	}


	@PostMapping("/create")
	public void create(@RequestBody CountDto countDto) {
		log.info("coupon create - count : {}", countDto);
		couponService.create(countDto.getCount());
	}

	@PostMapping("/allocate")
	public CouponDto allocate(@RequestBody UserDto userDto) {
		log.info("coupon allocate - userDto : {}", userDto);
		return couponService.allocate(userDto);
	}

	@PutMapping("/use/{code}")
	public Coupon use(@PathVariable String code) {
		return couponService.useCoupon(code);
	}

	@PutMapping("/cancel/{code}")
	public Coupon cancel(@PathVariable String code) {
		return couponService.cancelCoupon(code);
	}

	@GetMapping("/expired")
	public Page<Coupon> getExpiredByToday(
			@PageableDefault(size=10, sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
		return couponService.getExpiredByToday(pageable);
	}

	@GetMapping("/sendExpiredAfter3daystest")
	public void sendExpiredAfter3days() {
		couponService.sendExpiredAfter3days();
	}
}
