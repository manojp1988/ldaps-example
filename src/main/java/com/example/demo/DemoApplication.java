package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
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
            final String ldapAdServer = "ldap://localhost:389";


            final String ldapUsername = "uid=user.0,ou=People,dc=example,dc=com";
            final String ldapPassword = "Newgen@123";

            Hashtable<String, Object> env = new Hashtable<>();
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
            env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapAdServer);
            env.put("java.naming.ldap.attributes.binary", "objectSID");
            DirContext ctx = new InitialDirContext(env);

            String[] attrIDs = {"cn"};
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "(objectClass=person)";

            NamingEnumeration<SearchResult> searchResults = ctx.search("dc=example,dc=com", filter, searchControls);

            String commonName = null;
            String distinguishedName = null;
            while (searchResults.hasMore()) {

                SearchResult result = (SearchResult) searchResults.next();
                Attributes attrs = result.getAttributes();

                distinguishedName = result.getNameInNamespace();
                commonName = attrs.get("cn").toString();
                System.out.println("Distinguised name :" + distinguishedName);
                System.out.println("Common name :" + commonName);

            }

            System.out.println("COmpleted..");
        };
    }


}



