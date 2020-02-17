package com.air.server.user.utils;

import com.air.server.user.entity.EduUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtUtil {
    //过期时间
    private static final long EXPIRE_TIME = 15 * 60 * 1000;
    //私钥
    private static final String TOKEN_SECRET = "heimao123";

    /**
     * 生成jwt
     */
    public static String getTokenSecret(EduUser eduUser) {

        if (eduUser == null || StringUtils.isEmpty(eduUser.getUsername())
        || StringUtils.isEmpty(eduUser.getId())
    || StringUtils.isEmpty(eduUser.getMobile())) {
            return null;
        }

        String token = Jwts.builder().setSubject("heimao")
                .claim("id",eduUser.getId())
                .claim("username",eduUser.getUsername())
                .claim("mobile",eduUser.getMobile())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +1000 * 60 * 30))
                .signWith(SignatureAlgorithm.HS256,TOKEN_SECRET).compact();


        return token;

    }

    /**
     * 检验jwt
     *
     */
 public static Claims checkJwt(String token) {
     Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
     return claims;
 }


    public static void main(String[] args) {
        EduUser eduUser = new EduUser();
        eduUser.setId("122");
        eduUser.setMobile("18946303556");
        eduUser.setUsername("tom");
        String tokenSecret = getTokenSecret(eduUser);
        System.out.println(tokenSecret);
        System.out.println("---------------------");
        Claims claims = checkJwt(tokenSecret);
        String id = (String) claims.get("id");
        String username = (String) claims.get("username");
        String mobile = (String) claims.get("mobile");
        System.out.println("id: "+id);
        System.out.println("username "+username);
        System.out.println("mobile: "+mobile);
    }
}
