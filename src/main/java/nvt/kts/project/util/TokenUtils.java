package nvt.kts.project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nvt.kts.project.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    @Value("uber")
    private String appName;

    @Value("somesecret")
    public String secret;

    @Value("1800000")
    private int expiresIn;

    @Value("Authorization")
    private String authHeader;

    private static final String AUDIENCE_WEB = "web";

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, secret).compact();
    }

    private String generateAudience() {
        return AUDIENCE_WEB;
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn);
    }

    public String getToken(HttpServletRequest request) {
        String authorizationHeader = getAuthHeaderFromHeader(request);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    public String getUsernameFromToken(String token){
        String username = null;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims != null)
                username = claims.getSubject();
        } catch (ExpiredJwtException | NullPointerException ex) {
            throw ex;
        } catch (Exception e) {
            return null;
        }

        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt = null;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims != null)
                issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            return null;
        }
        return issueAt;
    }

    public String getAudienceFromToken(String token) {
        String audience = null;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims != null)
                audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            return null;
        }
        return audience;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if(claims != null)
                expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            return null;
        }

        return expiration;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        return (username != null
                && username.equals(userDetails.getUsername())
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public int getExpiredIn() {
        return expiresIn;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {

        return request.getHeader(authHeader);
    }
}
