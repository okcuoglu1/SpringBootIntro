package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // !!! bu classda 1.amacim : Security katmanina User objelerimi verip UserDetails
    // turune cevrilmesini saglamak kisaca kendi Userlarimi security katmanina tanitmis
    // olacagiz
    // 2.amacimiz : Role bilgilerini Granted Auth. a cevirmek

    @Autowired
    UserRepository userRepository;


    //Parametrede username den user'a ihtiyacımız var.Normalde service katmanında halletmemiz gerek ama şuan işler uzamansın diye
    //DI kullandık.--- Bu method Userlarımızı UserDetailse ceviricek.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username).orElseThrow(()->
                new ResourceNotFoundException("user not found with username : " + username));

        if(user != null) {
            return new org.springframework.security.core.userdetails.User(
                    //Bu User classı UserDetailsde bulunnan class. Bu classın parametreli const cagırarak bir nevi  UserDetails objesi olusturmus oluyoruz.
                    user.getUserName(),
                    user.getPassword(),
                    //Bu method direk getRole kabul etmiyor.Granted auth kabul ediyor. O yüzden Granted Auth a ceviren bir method yapıp öyle getRole dedik.
                    buildGrantedAuthority(user.getRole())
            );
        } else {
            throw  new UsernameNotFoundException("User not found with username : " + username);
        }

    }


    //Bu method çağırırken mutlaka role değeri dolsun diye final yaptık.Düzgün calısması icin parametre null olarak gelmemesi lazım.
    //Rollerimizi granted auth a ceviriyoruz. Securtiynin anlayacagı rollere dönüştürüyoruz.
    private static List<SimpleGrantedAuthority> buildGrantedAuthority(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role: roles) {
            //role.getName().name() role enum yapıda oldugu icin getName classın ismini getirir. .name() de istediğimiz string ifade geliyor.
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));

        }

        return authorities;
    }
}