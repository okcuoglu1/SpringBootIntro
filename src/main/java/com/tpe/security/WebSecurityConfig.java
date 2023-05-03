package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Security katmanina bu clasimin konfigurasyon vlasi oldugunu soyluyorum.
@EnableWebSecurity //Web katmanını security hale getir diyoruz. Requestler security katmanıyla tanışsın.
//prePostEnabled = true --> hasrole anno ya izin veriyoruz.
@EnableGlobalMethodSecurity(prePostEnabled = true)  // method seviyede yetkilendirme yapacagimi soyluyorum
//Class seviyesinde rollendirme de yapılabilir. AdminController ve StudentController olarak sadece adminin yapacaklarını admin classında
//studentın yapacağı methodları sadece student yapsın gibi.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // !!! bu classda amacimiz : AuthManager, Provider , PassEncoder larimi olusturup birbirleriyle
    // tanistirmak

    //AuthenticationProvider UserDetailsService ile tanışması gerekiyor. That's why DI
    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //csrf --> Browserdaki sekmelerden sekmelere atlamayı önler. Kötü amaclı yazılımların sekmeler arası geçişini önleyen bir yapı.
        //csrf in acık olması api üzerinden yapacagımız bazı methodlar(update vb.) düzgün calısmıyor. Spring Security csrfden cok daha güclüdür.
        http.csrf().disable().  // csrf korumasini disable yapiyoruz
                authorizeHttpRequests().  // gelen butun requestleri yetkili mi diye kontrol edecegiz.
                antMatchers("/", //Bazı sayfalara yetki bakmazsızın girilmesi lazım.
                "index.html",
                "/css/*",
                "/js/*").permitAll(). // bu end-pointleri yetkili mi diye kontrol etme
                anyRequest(). // muaf tutulan end-pointler disinda gelen herhangi bir requesti
                authenticated(). // yetkili mi diye kontrol et
                and(). //Yukardakileri yap bir de sunu yap demiş olduk.
                httpBasic(); // bunu yaparkende Basic Auth kullanilacagini belirtiyoruz
    }


    //Ben bu classı app de newlemeden kullanmamız için @Bean anno kullanıyorum.
    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder BASE64 ile encode ediyor.
        //parantez içine 4 ila 31 arasında bi sayı girebiliyorum. Şifreleme(hashleme) seviyesini belirliyoruz.
        //Genelde 10-11-12 gibi rakamlar kullanılır.Performans için.
        return new BCryptPasswordEncoder(10);
    }

    //Dönen değere diğer classlarımızda ulasmak icin bean.
    @Bean
    public DaoAuthenticationProvider authProvider(){
        //Bu classı passwordenco ve userdetails ile tanıstırmak icin newledik.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setPasswordEncoder(passwordEncoder()); // encoder ile tanistirdim
        authProvider.setUserDetailsService(userDetailsService); // UserDetailsService ile tanistirdim

        return authProvider;

    }

    //Bu method hazırda bulunan authManageri aktif edip provider ile tanıstırmamız gerekiyor.
    //Security bean ile değil de benim hazır configure classımı kullan diyor. That's why we are not using @Bean Annotation.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authProvider());
    }
}