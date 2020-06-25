package com.example.demo.coupon;

import com.example.demo.common.StatusEnum;
import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.exception.ApiException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.dto.EmailDto;
import com.example.demo.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({CouponServiceImpl.class, CouponRepository.class, UserRepository.class})
@ActiveProfiles("test")
public class CouponRepositoryTest {

	@Autowired
	private CouponService couponService;

	@MockBean
	private CouponRepository couponRepository;

	@MockBean
	private UserRepository userRepository;

	private UserDto userDto;

	@BeforeEach
	void setup() {
		userDto = new UserDto();
		userDto.setEmail("test@test.com");
		userDto.setPassword("123");
	}

	@Test
	public void createTest() {
		int count = 10;
		Mockito.when(couponRepository.saveAll(Mockito.any())).thenReturn(Arrays.asList(new Coupon[count]));
		int size = couponService.create(count).size();
		assertEquals(count , size);

		ArgumentCaptor<List<Coupon>> CouponListArgumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(couponRepository).saveAll(CouponListArgumentCaptor.capture());
		assertEquals(count, CouponListArgumentCaptor.getValue().size());
	}

	@Test
	public void allocateTest() {
		Coupon coupon = new Coupon();
		coupon.setStatus(StatusEnum.N);
		coupon.setCode("testCode");
		Mockito.when(couponRepository.findFirstByStatus(Mockito.any())).thenReturn(coupon);
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("123");
		Mockito.when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(user);
		coupon.setUser(user);
		coupon.setStatus(StatusEnum.A);
		Mockito.when(couponRepository.save(Mockito.any())).thenReturn(coupon);

		EmailDto emailDto = new EmailDto();
		emailDto.setEmail("test@test.com");
		CouponDto couponDto = couponService.allocate(emailDto);
		assertEquals(coupon.getCode(), couponDto.getCode());

		ArgumentCaptor<StatusEnum> statusEnumArgumentCaptor = ArgumentCaptor.forClass(StatusEnum.class);
		verify(couponRepository).findFirstByStatus(statusEnumArgumentCaptor.capture());
		assertEquals(StatusEnum.N, statusEnumArgumentCaptor.getValue());

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(userRepository).findOneByEmail(stringArgumentCaptor.capture());
		assertEquals(user.getEmail(), stringArgumentCaptor.getValue());

		ArgumentCaptor<Coupon> couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);
		verify(couponRepository).save(couponArgumentCaptor.capture());
		assertEquals(coupon.getCode(), couponArgumentCaptor.getValue().getCode());
	}

	@Test
	public void useCouponTest() {
		Coupon nCoupon = new Coupon();
		nCoupon.setUseDate(new Date());
		nCoupon.setStatus(StatusEnum.N);
		String code = "testCode";
		Mockito.when(couponRepository.findFirstByCode(Mockito.anyString())).thenReturn(nCoupon);
		Coupon yCoupon = new Coupon();
		yCoupon.setUseDate(nCoupon.getUseDate());
		yCoupon.setStatus(StatusEnum.Y);
		Mockito.when(couponRepository.save(Mockito.any())).thenReturn(yCoupon);

		couponService.useCoupon(code);

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(couponRepository).findFirstByCode(stringArgumentCaptor.capture());
		assertEquals(code, stringArgumentCaptor.getValue());

		ArgumentCaptor<Coupon> couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);
		verify(couponRepository).save(couponArgumentCaptor.capture());
		assertEquals(StatusEnum.Y, couponArgumentCaptor.getValue().getStatus());
	}

	@Test
	public void useCouponNotFoundTest() {
		Coupon nCoupon = null;
		String code = "testCode";
		Mockito.when(couponRepository.findFirstByCode(Mockito.anyString())).thenReturn(nCoupon);

		Assertions.assertThrows(ApiException.class,
				() -> couponService.useCoupon(code));
	}

	@Test
	public void useCouponExpiredCodeTest() {
		Coupon nCoupon = new Coupon();
		nCoupon.setUseDate(new Date());
		nCoupon.setStatus(StatusEnum.E);
		String code = "testCode";
		Mockito.when(couponRepository.findFirstByCode(Mockito.anyString())).thenReturn(nCoupon);

		Assertions.assertThrows(ApiException.class,
				() -> couponService.useCoupon(code));
	}

	@Test
	public void useCouponAlreadyUseCouponTest() {
		Coupon nCoupon = new Coupon();
		nCoupon.setUseDate(new Date());
		nCoupon.setStatus(StatusEnum.Y);
		String code = "testCode";
		Mockito.when(couponRepository.findFirstByCode(Mockito.anyString())).thenReturn(nCoupon);

		Assertions.assertThrows(ApiException.class,
				() -> couponService.useCoupon(code));
	}

	@Test
	public void cancelCouponTest() {
		Coupon nCoupon = new Coupon();
		nCoupon.setUseDate(new Date());
		nCoupon.setStatus(StatusEnum.A);
		String code = "testCode";
		Mockito.when(couponRepository.findFirstByCode(Mockito.anyString())).thenReturn(nCoupon);
		Coupon yCoupon = new Coupon();
		yCoupon.setUseDate(nCoupon.getUseDate());
		yCoupon.setStatus(StatusEnum.N);
		Mockito.when(couponRepository.save(Mockito.any())).thenReturn(yCoupon);

		couponService.cancelCoupon(code);

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(couponRepository).findFirstByCode(stringArgumentCaptor.capture());
		assertEquals(code, stringArgumentCaptor.getValue());

		ArgumentCaptor<Coupon> couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);
		verify(couponRepository).save(couponArgumentCaptor.capture());
		assertEquals(StatusEnum.N, couponArgumentCaptor.getValue().getStatus());
	}
}
