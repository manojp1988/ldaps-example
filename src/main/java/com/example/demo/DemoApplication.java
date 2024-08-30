package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {

            String[] attrIDs = { "cn" };
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(attrIDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter ="(objectClass=person)";
            Hashtable env = new Hashtable();
            env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(DirContext.PROVIDER_URL, "ldaps://localhost:636" );
            env.put(DirContext.SECURITY_AUTHENTICATION, "simple");
            env.put(DirContext.SECURITY_PRINCIPAL, "uid=user.0,ou=People,dc=example,dc=com");
            env.put(DirContext.SECURITY_CREDENTIALS, "Newgen@123");
            env.put("java.naming.ldap.factory.socket", "com.example.demo.MySSLSocketFactory");
            DirContext dirContext = null;
            try {
                dirContext  = new InitialDirContext(env);
                NamingEnumeration e = dirContext.search("dc=example,dc=com", filter,ctls);
                while (e.hasMore()) {
                    SearchResult entry = (SearchResult) e.next();
                    System.out.println(entry.getName());
                    Attributes attrs = entry.getAttributes();
                    Attribute attr = attrs.get("cn");
                    var phone = (String) attr.get();
                    System.out.println(phone);
                }
            }
            catch (Exception e) {
                System.err.println(e);
            }

            System.out.println("COmpleted..");
        };
    }


}



