package com.ljz.jwt;
/*
 * 获取token的接口，通过传入用户认证信息进行认证获取
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
  
import com.ljz.pojo.User;  
import com.ljz.repository.UserRepository;
import com.ljz.util.MyUtils;
import com.ljz.util.ResultMsg;
import com.ljz.util.ResultStatusCode;  
  
  
@RestController  
public class JsonWebToken {  
    @Autowired  
    private UserRepository userRepositoy;  
      
    @Autowired  
    private Audience audienceEntity;  
    
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
      
    @RequestMapping("oauth/token")  
    public Object getAccessToken(@RequestBody LoginPara loginPara)  
    {   
    	ResultMsg resultMsg;
        try  
        {  
            if((loginPara.getClientId() == null  ) 
                    || (loginPara.getClientId().compareTo(audienceEntity.getClientId()) != 0))  
            {
            	resultMsg = new ResultMsg(ResultStatusCode.INVALID_CLIENTID.getErrcode(),
            			ResultStatusCode.INVALID_CLIENTID.getErrmsg(),null);
            	return resultMsg;
            }
              
            //验证码校验在后面章节添加  
            String captchaCode = loginPara.getCaptchaCode();
            try{
            	if(captchaCode == null)		throw new Exception();
            	String captchaValue = redisTemplate.opsForValue().get(captchaCode);
            	if(captchaValue == null)	throw new Exception();
            	redisTemplate.delete(captchaCode);	//这是啥意思？
            	if(captchaValue.compareTo(loginPara.getCaptchaValue())!=0) throw new Exception();
            }catch(Exception e){
            	resultMsg = new ResultMsg(ResultStatusCode.INVALID_CAPTCHA.getErrcode(),   
                        ResultStatusCode.INVALID_CAPTCHA.getErrmsg(), null);  
                return resultMsg;
            }
              
            //验证用户名密码  
            User user = userRepositoy.findUserByName(loginPara.getUsername());  

            if (user == null){
            	resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),  
                        ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
            	return resultMsg;
            }
            else  
            {  
                String md5Password = MyUtils.getMD5(loginPara.getPassword()+user.getSalt());  
                  
                if (md5Password.compareTo(user.getPassword()) != 0){
                    resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),  
                            ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);  
                    return resultMsg;  
                }
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
            resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),   
                    ResultStatusCode.OK.getErrmsg(), accessTokenEntity); 
            return accessTokenEntity;  
              
        }  
        catch(Exception ex)  
        {  
            resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(),   
                    ResultStatusCode.SYSTEM_ERR.getErrmsg(), null);  
            return resultMsg; 
        }  
    }  
}  