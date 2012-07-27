/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.apache.myfaces.extensions.cdi.core.api.provider.BeanManagerProvider;

/**
 *
 * @author rpestano
 */
public class BeanManagerController {

    public static BeanManager getBeanManager() {
            return BeanManagerProvider.getInstance().getBeanManager();
    }

    public static Object getBeanByName(String name) // eg. name=availableCountryDao
    {
        BeanManager bm = getBeanManager();
        Bean bean = bm.getBeans(name).iterator().next();
        CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
        Object o = bm.getReference(bean, bean.getClass(), ctx); // could be inlined with return
        return o;
    }

    public static Object getBeanByName(String name, BeanManager bm) {
        Bean bean = bm.getBeans(name).iterator().next();
        CreationalContext ctx = bm.createCreationalContext(bean);
        Object o = bm.getReference(bean, bean.getClass(), ctx);
        return o;
    }
}
