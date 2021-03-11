package edu.kpi.testcourse.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.token.jwt.generator.claims.JwtClaims;
import io.micronaut.security.token.jwt.generator.claims.JwtClaimsSetAdapter;
import java.text.ParseException;

/**
 * Static class with useful methods for work with cookies.
 */
public class JwtHelper {
  public String getUsernameFromToken(String token) throws ParseException {
    JWT jwtToken = JWTParser.parse(token);
    return (String) new JwtClaimsSetAdapter(jwtToken.getJWTClaimsSet()).get(JwtClaims.SUBJECT);
  }

  public String getTokenFromRequest(HttpRequest<?> httpRequest) {
    return httpRequest.getCookies().get("JWT").getValue();
  }

  public String getUsernameFromRequest(HttpRequest<?> httpRequest) throws ParseException {
    return getUsernameFromToken(getTokenFromRequest(httpRequest));
  }
}
