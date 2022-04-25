 package com.kovatech.auth.core.config;

 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.boot.context.properties.ConfigurationProperties;
 import org.springframework.context.annotation.Configuration;

 import java.util.Map;


 @Configuration
 @ConfigurationProperties(prefix = "dxl.basic.auth")
 public class WsBasicAuthProperties
 {
   private Map<String, String> users;
   private String antMatchers;

     @Value("${dxl.basic.auth.staticIv}")
     private String staticIv;

     public String getStaticIv() {
         return staticIv;
     }

     public void setStaticIv(String staticIv) {
         this.staticIv = staticIv;
     }
   
   public Map<String, String> getUsers() {
     return this.users;
   }
   
   public void setUsers(Map<String, String> users) {
    this.users = users;
   }
   
   public String getAntMatchers() {
     return this.antMatchers;
   }
   
   public void setAntMatchers(String antMatchers) {
     this.antMatchers = antMatchers;
   }
 }