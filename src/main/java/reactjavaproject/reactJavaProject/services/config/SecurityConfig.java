package reactjavaproject.reactJavaProject.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JWTAuthHelper jwtAuthHelper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Bean
    public FilterRegistrationBean coresFilter(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.addAllowedHeader("Content-Type");
        corsConfiguration.addAllowedHeader("Accept");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.setMaxAge(3600L);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(urlBasedCorsConfigurationSource));
        return filterRegistrationBean;
    }
    @Bean
    public UserDetailsService getUserDetailService()
    {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder encoder (){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.authorizeHttpRequests((request) -> request.requestMatchers("/**").permitAll().requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(userDetailsService,jwtAuthHelper), UsernamePasswordAuthenticationFilter.class);
//        http.authorizeHttpRequests().anyRequest().authenticated()
//                .and().formLogin();
//                .requestMatchers("/**").permitAll()
//                .anyRequest().authenticated();
//                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
//                .anyRequest().authenticated()
//                .and().formLogin()
//                .loginPage("/login")
//                .usernameParameter("email")
//                .permitAll()
//                .and()
//                .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
//                .and()
//                .logout().permitAll();

        http.headers().frameOptions().sameOrigin();
        http.csrf().disable().headers().frameOptions().disable();

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
//    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(getUserDetailService());
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }
}
