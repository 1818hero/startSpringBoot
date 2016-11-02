package com.ljz.jwt;
/*
 * 获取token的接口，通过传入用户认证信息进行认证获取
 */
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
  
import com.ljz.pojo.User;  
import com.ljz.repository.UserRepository;
import com.ljz.util.MyUtils;  
  
  
@RestController  
public class JsonWebToken {  
    @Autowired  
    private UserRepository userRepositoy;  
      
    @Autowired  
    private Audience audienceEntity;  
      
    @RequestMapping("oauth/token")  
    public Object getAccessToken(@RequestBody LoginPara loginPara)  
    {   
        try  
        {  
            if(loginPara.getClientId() == null   
                    || (loginPara.getClientId().compareTo(audienceEntity.getClientId()) != 0))  
            return null;
              
            //验证码校验在后面章节添加  
              
              
            //验证用户名密码  
            User user = userRepositoy.findUserByName(loginPara.getUsername());  
            if (user == null)  return null;
            else  
            {  
                String md5Password = MyUtils.getMD5(loginPara.getPassword()+user.getSalt());  
                  
                if (md5Password.compareTo(user.getPassword()) != 0)  return null;
            }  
              
            //拼装accessToken  
            String accessToken = JwtHelper.createJWT(loginPara.getUsername(), String.valueOf(user.getName()),  
                    user.getRole(), audienceEntity.getClientId(), audienceEntity.getName(),  
                    audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());  
              
            //返回accessToken  
            AccessToken accessTokenEntity = new AccessToken();  
            accessTokenEntity.setAccess_token(accessToken);  
            accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());  
            accessTokenEntity.setToken_type("bearer");  
            return accessTokenEntity;  
              
        }  
        catch(Exception ex)  
        {  
        	return null;
        }  
    }  
}  