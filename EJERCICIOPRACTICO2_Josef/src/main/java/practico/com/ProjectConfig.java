/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro/nuevo").setViewName("registro/nuevo");

        registry.addViewController("/producto/nuevo").setViewName("producto/nuevo");
        registry.addViewController("/producto/eliminar").setViewName("producto/eliminar");
        registry.addViewController("/producto/listado").setViewName("producto/listado");
        registry.addViewController("/producto/ver").setViewName("producto/ver");
        registry.addViewController("/producto/modificar").setViewName("/producto/modificar");
        registry.addViewController("/producto/guardar").setViewName("/producto/guardar");

        registry.addViewController("/nosotros/listado").setViewName("nosotros/listado");
        
    }
    
  @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(requests -> requests
            
            .requestMatchers(
                "/", "/index", "/errores/**", "/carrito/**", "/pruebas/**",
                "/registro/**", "/js/**", "/webjars/**", 
                "/image/**", "/video/**", "/css/**"
            ).permitAll()

          
            .requestMatchers(
                "/producto/eliminar/**",
                "/categoria/eliminar/**",
                "/usuario/eliminar/**"
            ).hasRole("ADMIN")

           
            .requestMatchers(
                "/producto/nuevo", "/producto/guardar",
                "/producto/modificar/**", 
                "/categoria/nuevo", "/categoria/guardar",
                "/categoria/modificar/**",
                "/usuario/nuevo", "/usuario/guardar",
                "/usuario/modificar/**",
                "/reportes/**"
            ).hasAnyRole("ADMIN", "VENDEDOR")

           
            .requestMatchers(
                "/producto/listado", 
                "/producto/ver/**", 
                "/nosotros/listado"
            ).hasAnyRole("ADMIN", "VENDEDOR", "USER")

            
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
        )
        .logout(logout -> logout.permitAll())
        .csrf().disable(); 

    return http.build();
}

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService)
             .passwordEncoder(new BCryptPasswordEncoder());
    }
}
