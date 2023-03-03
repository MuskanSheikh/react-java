package reactjavaproject.reactJavaProject.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JWTAuthHelper {
    @Value("${jwt.auth.app}")
    private String appName;
    @Value("${jwt.auth.secret_key}")
    private String secretKey;
    @Value("${jwt.auth.expires_in}")
    private int expiresIn;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Claims getAllClaimFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String getUserFromTokenString(String token) {
        String userName;
        try {
            final Claims claims = this.getAllClaimFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    public String generateToken(String userName) throws InvalidKeyException, NoSuchAlgorithmException{
        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 1000L);
    }
    public Boolean validateToken(String token , UserDetails userDetails){
        final  String username = getUserFromTokenString(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expireDate = getExpirationDate(token);
        return expireDate.before(new Date());
    }

    private Date getExpirationDate(String token) {
        Date expireDate;
        try {
            final  Claims claims = this.getAllClaimFromToken(token);
            expireDate = claims.getExpiration();
        }catch (Exception e ){
            expireDate = null;
        }
        return expireDate;
    }
    public Date getIssuedAndDateFromToken(String token){
        Date issuedAt;
        try {
            final  Claims claims = this.getAllClaimFromToken(token);
            issuedAt = claims.getIssuedAt();
        }catch (Exception e ){
            issuedAt = null;
        }
        return issuedAt;
    }
    public String getToken (HttpServletRequest request){
        String authHeader = getAuthHeaderFromHeader(request);
        if(authHeader!= null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
