package top.dddpeter.ishare.job.admin.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import top.dddpeter.ishare.job.admin.core.model.JobUser;
import top.dddpeter.ishare.job.admin.core.util.CookieUtil;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.admin.core.util.JacksonUtil;
import top.dddpeter.ishare.job.admin.dao.JobUserDao;
import top.dddpeter.ishare.job.biz.model.ReturnT;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author hqins 2019-12-10
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "JOB_LOGIN_IDENTITY";
    public static final String HIDDEN_MENU = "HIDDEN_MENU";
    @Resource
    private JobUserDao jobUserDao;


    private String makeToken(JobUser jobUser){
        String tokenJson = JacksonUtil.writeValueAsString(jobUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private JobUser parseToken(String tokenHex){
        JobUser jobUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            jobUser = JacksonUtil.readValue(tokenJson, JobUser.class);
        }
        return jobUser;
    }


    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, String username, String password, boolean ifRemember){

        // param
        if (username==null || username.trim().length()==0 || password==null || password.trim().length()==0){
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
        JobUser jobUser = jobUserDao.loadByUserName(username);
        if (jobUser == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(jobUser.getPassword())) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }

        String loginToken = makeToken(jobUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return ReturnT.SUCCESS;
    }

    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, String token){
        JobUser jobUser = parseToken(token);
        JobUser jobUser1 = jobUserDao.loadByUserName(jobUser.getUsername());
        if(jobUser1==null){
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        // do login
        String isAdmin = request.getParameter("is_admin");
        if(isAdmin!=null){
            String domain = request.getServerName().replaceAll(".*\\.(?=.*\\.)", "");
            CookieUtil.set(response, HIDDEN_MENU, isAdmin, domain,"/",request.getSession().getMaxInactiveInterval(),false);
        }
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, token, false);
        return ReturnT.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public JobUser ifLogin(HttpServletRequest request, HttpServletResponse response){
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            JobUser cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null) {
                JobUser dbUser = jobUserDao.loadByUserName(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }


}
