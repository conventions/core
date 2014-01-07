package org.conventionsframework.security;

import javax.enterprise.inject.spi.Extension;

/**
 * Created by rmpestano on 1/5/14.
 */
public class SecurityExtension implements Extension {
    //AnnotatedTypeBuilder  builder = null;


    /** public  void processAnnotatedType(@Observes ProcessAnnotatedType<DefaultSecurityContext> pat) {
         if(!pat.getAnnotatedType().getBaseType().getClass().equals(DefaultSecurityContext.class)){
               builder = new AnnotatedTypeBuilder().readFromType(pat.getAnnotatedType());
               builder.addToClass(new SpecializesLiteral());
               pat.setAnnotatedType(builder.create());
           }

    }   **/


}