package com.zeed.assignment.sms.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(SimpleCORSFilter.class.getName());
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		
		String originHeader = request.getHeader("Origin");
		
		response.setHeader("Access-Control-Allow-Origin", originHeader);
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,origin,accept,content-type,access-control-request-method,access-control-request-headers,authorization");
		String ee = request.getMethod();
		if (!request.getMethod().equals("OPTIONS")) {
			try {
				chain.doFilter(req, res);
			}
			catch (ServletException e) {
				logger.error("Erroro occurred due to ", e);
			}
			catch (IOException e) {
				logger.error("Erroro occurred due to ", e);
			}
		} else {
		}
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

	private void handleSerializationError(HttpServletRequest request, HttpServletResponse response, Throwable t) {
		try {
			logger.error("error occured"+t);
			response.sendRedirect("/login?error");
		}
		catch(IOException ex) {
			logger.error("Error occured " + ex);
		}
	}

}