/*
  Copyright 2013 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.j2e.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract filter which handles configuration.
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
public abstract class AbstractConfigFilter implements Filter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String getStringParam(final FilterConfig filterConfig, final String name, final String defaultValue) {
        final String param = filterConfig.getInitParameter(name);
        final String value;
        if (param != null) {
            value = param;
        } else {
            value = defaultValue;
        }
        logger.debug("String param: {}: {}", name, value);
        return value;
    }

    protected void checkUselessParameter(final FilterConfig filterConfig, final String name) {
        final String parameter = getStringParam(filterConfig, name, null);
        if (CommonHelper.isNotBlank(parameter)) {
            final String message = "the " + name + " servlet parameter is no longer available and will be ignored";
            logger.error(message);
            throw new TechnicalException(message);
        }
    }

    protected void checkForbiddenParameter(final FilterConfig filterConfig, final String name) {
        final String parameter = getStringParam(filterConfig, name, null);
        if (CommonHelper.isNotBlank(parameter)) {
            final String message = "the " + name + " servlet parameter is no longer supported";
            logger.error(message);
            throw new TechnicalException(message);
        }
    }


    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        internalFilter(req, resp, chain);
    }

    protected abstract void internalFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException;

    public void destroy() {
    }
}
