package com.bjsxt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        User user = new User("admin", "123456", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//        inMemoryUserDetailsManager.createUser(user);
//        return inMemoryUserDetailsManager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String s = encrypByMD5("123456");
        System.out.println(s);
        String encode = bCryptPasswordEncoder.encode(s);
        String encode2 = bCryptPasswordEncoder.encode("e10adc3949ba59abbe56e057f20f883e");
        System.out.println(encode);
        System.out.println(encode2);
    }

    private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    //e10adc3949ba59abbe56e057f20f883e
    /***
     * 对指定的字符串进行MD5加密
     */
    public static String encrypByMD5(String originString) {
        try {
            //创建具有MD5算法的信息摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
            byte[] bytes = md.digest(originString.getBytes());
            //将得到的字节数组变成字符串返回
            String s = byteArrayToHex(bytes);
            return s.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将字节数组转换成十六进制，并以字符串的形式返回
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     */
    private static String byteArrayToHex(byte[] b){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHex(b[i]));
        }
        return sb.toString();
    }
    /**
     * 将一个字节转换成十六进制，并以字符串的形式返回
     */
    public static String byteToHex(byte b) {
        int n = b;
        if (n < 0)
            n = n + 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1]+hexArray[d2];
    }
}
