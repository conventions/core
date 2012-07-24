 
package org.conventionsframework.util;

import com.sun.faces.renderkit.ApplicationObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import org.conventionsframework.qualifier.ConventionsBundle;

@ConventionsBundle
public class ResourceBundle extends java.util.PropertyResourceBundle implements Serializable {


	private static final long serialVersionUID = 1L;

        public ResourceBundle() throws IOException {
            super(new ApplicationObjectInputStream());
            }
        public ResourceBundle(InputStream stream) throws IOException {
                    super(stream);
	}

	public String getString(String key, List<Object> params) {
		return MessageFormat.format(this.getString(key), params.toArray());
	}

}
