package com.example.demo.coupon;

import com.example.demo.coupon.dto.CountDto;
import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.user.dto.EmailDto;
import com.example.demo.user.dto.UserDto;
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
	public List<CouponDto> getCouponByEmail(
			@RequestHeader(value="Authorization") String authorization,
			@RequestBody EmailDto emailDto) {
		List<Coupon> coupons = couponService.getCouponsByEmail(emailDto.getEmail());

		List<CouponDto> couponDtos = new ArrayList<>();
		for (Coupon coupon : coupons) {
			CouponDto couponDto = new CouponDto();
			couponDto.setCode(coupon.getCode());
			couponDtos.add(couponDto);
		}
		return couponDtos;
	}

	@PostMapping("/create")
	public void create(@RequestHeader(value="Authorization") String authorization,
					   @RequestBody CountDto countDto) {
		log.info("coupon create - count : {}", countDto);
		couponService.create(countDto.getCount());
	}

	@PostMapping("/allocate")
	public CouponDto allocate(
			@RequestHeader(value="Authorization") String authorization,
			@RequestBody EmailDto emailDto) {
		log.info("coupon allocate - allocateDto : {}", emailDto);
		return couponService.allocate(emailDto);
	}

	@PutMapping("/use/{code}")
	public void use(@RequestHeader(value="Authorization") String authorization,
					  @PathVariable String code) {
		couponService.useCoupon(code);
	}

	@PutMapping("/cancel/{code}")
	public void cancel(@RequestHeader(value="Authorization") String authorization,
						 @PathVariable String code) {
		couponService.cancelCoupon(code);
	}

	@GetMapping("/expired")
	public Page<Coupon> getExpiredByToday(
			@RequestHeader(value="Authorization") String authorization,
			@PageableDefault(size=10, sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
		return couponService.getExpiredByToday(pageable);
	}
}
