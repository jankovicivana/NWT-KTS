package nvt.kts.project.config;

import nvt.kts.project.security.RestAuthenticationEntryPoint;
import nvt.kts.project.security.TokenAuthenticationFilter;
import nvt.kts.project.service.UserService;
import nvt.kts.project.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(){ return new RestAuthenticationEntryPoint();}

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return authentication -> null;
    }


    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // Definisemo uputstva AuthenticationManager-u:

                // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
                // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
                .userDetailsService(userService)

                // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
                // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
                .passwordEncoder(this.passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint()).and()
                .authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and()
                .logout(logout -> logout.logoutUrl("/").logoutSuccessUrl("/").invalidateHttpSession(true));
                //.addFilterBefore(new TokenAuthenticationFilter(new TokenUtils(), userService), BasicAuthenticationFilter.class);

        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

}
