package com.example.demo.coupon;

import com.example.demo.common.StatusEnum;
import com.example.demo.coupon.dto.CountDto;
import com.example.demo.coupon.dto.CouponDto;
import com.example.demo.user.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CouponApiController.class)
@WebAppConfiguration()
@AutoConfigureMockMvc
@Rollback
public class CouponApiControllerTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@MockBean
	CouponService couponService;

	@Autowired
	private MockMvc mockMvc;

	private String requestUserDtoJson;

	@BeforeEach
	void setup() {
		UserDto userDto = new UserDto();
		userDto.setEmail("test@test.com");
		userDto.setPassword("123");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		try {
			requestUserDtoJson = ow.writeValueAsString(userDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	void getCouponByEmail() throws Exception {
		List<Coupon> coupons = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Coupon coupon = new Coupon();
			coupon.setStatus(StatusEnum.N);
			coupon.setExpirationDate(new Date());
			coupon.setCode("" + i);
			coupons.add(coupon);
		}

		Mockito.when(couponService.getCouponsByEmail(Mockito.anyString())).thenReturn(coupons);
		mockMvc.perform(post("/api/coupon/user/coupon")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestUserDtoJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].code").value("0"))
				.andExpect(jsonPath("$.data[1].code").value("1"))
				.andExpect(jsonPath("$.data[2].code").value("2"));

		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
		verify(couponService).getCouponsByEmail(acString.capture());
		assertEquals("test@test.com", acString.getValue());
	}

	@Test
	public void createTest() throws Exception {
		Mockito.doNothing().when(couponService).create(Mockito.anyInt());

		CountDto countDto = new CountDto();
		countDto.setCount(3);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(countDto);
		mockMvc.perform(post("/api/coupon/create")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestJson))
				.andExpect(status().isOk());

		ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);
		verify(couponService).create(acInteger.capture());
		assertEquals(3, acInteger.getValue());
	}

	@Test
	public void allocateTest() throws Exception {
		CouponDto couponDto = new CouponDto();
		couponDto.setCode("testCode");
		Mockito.when(couponService.allocate(Mockito.any())).thenReturn(couponDto);
		mockMvc.perform(post("/api/coupon/allocate")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestUserDtoJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.code").value("testCode"));

		ArgumentCaptor<UserDto> userDtoArgumentCaptor = ArgumentCaptor.forClass(UserDto.class);
		verify(couponService).allocate(userDtoArgumentCaptor.capture());
		assertEquals("test@test.com", userDtoArgumentCaptor.getValue().getEmail());
	}

	@Test
	public void useTest() throws Exception{
		Coupon coupon = new Coupon();
		coupon.setCode("testCode");
		Mockito.when(couponService.useCoupon(Mockito.anyString())).thenReturn(coupon);
		mockMvc.perform(put("/api/coupon/use/test"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.code").value("testCode"));

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(couponService).useCoupon(stringArgumentCaptor.capture());
		assertEquals("test", stringArgumentCaptor.getValue());
	}

	@Test
	public void cancelTest() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setCode("testCode");
		Mockito.when(couponService.cancelCoupon(Mockito.anyString())).thenReturn(coupon);
		mockMvc.perform(put("/api/coupon/cancel/test"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.code").value("testCode"));

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(couponService).cancelCoupon(stringArgumentCaptor.capture());
		assertEquals("test", stringArgumentCaptor.getValue());
	}
	/*
	@GetMapping("/expired")
	public Page<Coupon> getExpiredByToday(@PageableDefault(size=10, sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
		return couponService.getExpiredByToday(pageable);
	}*/
}
