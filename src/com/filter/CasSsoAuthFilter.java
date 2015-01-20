package com.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;

import sun.awt.SunHints.Value;

/**
 * <p>Created by: qingHui
 * <p>Date: 13-10-18 下午3:21
 * <p>Version: 1.0
*/
public class CasSsoAuthFilter extends HttpServlet implements Filter {
	
	private static final long serialVersionUID = -7852743321348068943L;
	
	private static final String CAS_SERVICE_URL = "http://cas.server.com:8080/cas";//测试

	private FilterConfig filterConfig;
	static {
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){
 
	        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals(CAS_SERVICE_URL)) {
	                return true;
	            }
	            return false;
	        }
	    });
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			String ticket = httpRequest.getParameter("ticket");
			System.out.println("filter--->ticket: " + ticket);
			
			String legacyServerServiceUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getRequestURI();
			String url = CAS_SERVICE_URL + "/login?service=" + legacyServerServiceUrl;
			
			if(ticket == null || "".equals(ticket.trim())){
				filterChain.doFilter(request, response);
			} else {
				System.out.println("url is"+url);
				Cas20ProxyTicketValidator validator = new Cas20ProxyTicketValidator(CAS_SERVICE_URL);
				validator.setAcceptAnyProxy(true);
				Assertion assertion;
				try {
					assertion = validator.validate(ticket, legacyServerServiceUrl);
					Principal principal = assertion.getPrincipal();
					System.out.println("new logged in 用户名：" + principal.getName());
					httpRequest.getSession().setAttribute("currentUser", assertion);
					httpRequest.getSession().setAttribute("currentUserName", principal.getName());
					filterChain.doFilter(request, response);
					return;
				} catch (TicketValidationException e) {
					e.printStackTrace();
					httpResponse.sendRedirect(url);
				}
			}
			//filterChain.doFilter(request, response);
		} catch (IOException iox) {
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		 this.filterConfig = filterConfig;
	}
}
