package com.example.demo.auth;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.ErrorCode;
import com.example.demo.common.util.JwtTokenProvider;
import com.example.demo.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Profile("!test")
public class AuthRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (request.getRequestURI().contains("swagger")
				|| request.getRequestURI().equalsIgnoreCase("/v2/api-docs")
				|| request.getRequestURI().equalsIgnoreCase("/api/auth/create/token")
				|| request.getRequestURI().equalsIgnoreCase("/api/user/signup")) {
			chain.doFilter(request, response);
			return;
		}
		final String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			jwtTokenProvider.validateToken(jwtToken);
		} else {
			throw new ApiException(ErrorCode.AUTH_INVALID_CODE);
		}

		chain.doFilter(request, response);
	}
}