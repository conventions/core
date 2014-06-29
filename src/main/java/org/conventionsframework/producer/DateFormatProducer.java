package org.conventionsframework.producer;

import org.conventionsframework.qualifier.DateFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmpestano on 6/28/14.
 */
@ApplicationScoped
public class DateFormatProducer implements Serializable{

    Map<String,SimpleDateFormat> dateFormatCache = new HashMap<String,SimpleDateFormat>();

    @Produces
    @DateFormat
    public SimpleDateFormat getDateFormat(InjectionPoint ip){
        DateFormat df = ip.getAnnotated().getAnnotation(DateFormat.class);
        String pattern = df.pattern();
        if(dateFormatCache.containsKey(pattern)){
            return dateFormatCache.get(pattern);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(df.pattern());
        dateFormatCache.put(pattern,sdf);
        return sdf;
    }
}
