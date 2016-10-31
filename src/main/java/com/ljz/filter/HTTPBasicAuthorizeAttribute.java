package com.ljz.filter;

import java.io.IOException;  

import javax.servlet.Filter;  
import javax.servlet.FilterChain;  
import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import sun.misc.BASE64Decoder;  
  
public class HTTPBasicAuthorizeAttribute implements Filter{  
      
    private static String Name = "test";  
    private static String Password = "test";  
    @Override  
    public void destroy() {  
        // TODO Auto-generated method stub  
          
    }  
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
            throws IOException, ServletException {  
        // TODO Auto-generated method stub          
        boolean resultStatusCode = checkHTTPBasicAuthorize(request);  
        if (resultStatusCode != true)  
        {  
            HttpServletResponse httpResponse = (HttpServletResponse) response;  
            httpResponse.setCharacterEncoding("UTF-8");    
            httpResponse.setContentType("application/json; charset=utf-8");   
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
            httpResponse.getWriter().write("Incorrect username or password!");
            return;  
        }  
        else  
        {  
            chain.doFilter(request, response);  
        }  
    }  
  
    @Override  
    public void init(FilterConfig arg0) throws ServletException {  
        // TODO Auto-generated method stub  
          
    }  
      
    private boolean checkHTTPBasicAuthorize(ServletRequest request)  
    {  
        try  
        {  
            HttpServletRequest httpRequest = (HttpServletRequest)request;  
            String auth = httpRequest.getHeader("Authorization");  
            if ((auth != null) && (auth.length() > 6))  
            {  
                String HeadStr = auth.substring(0, 5).toLowerCase();  
                if (HeadStr.compareTo("basic") == 0)  
                {  
                    auth = auth.substring(6, auth.length());    
                    String decodedAuth = getFromBASE64(auth);  
                    if (decodedAuth != null)  
                    {  
                        String[] UserArray = decodedAuth.split(":");  
                          
                        if (UserArray != null && UserArray.length == 2)  
                        {  
                            if (UserArray[0].compareTo(Name) == 0  
                                    && UserArray[1].compareTo(Password) == 0)  
                            {  
                                return true;  
                            }  
                        }  
                    }  
                }  
            }  
            return false;  
        }  
        catch(Exception ex)  
        {  
            return false;  
        }  
          
    }  
      
    private String getFromBASE64(String s) {    
        if (s == null)    
            return null;    
        BASE64Decoder decoder = new BASE64Decoder();    
        try {    
            byte[] b = decoder.decodeBuffer(s);    
            return new String(b);    
        } catch (Exception e) {    
            return null;    
        }    
    }  
  
}  