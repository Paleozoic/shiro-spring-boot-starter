package com.maxplus1.access.starter.config.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class PermsFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }

        if(!isPermitted){
            ((HttpServletResponse) response).setStatus(401);
            StringBuffer sb = new StringBuffer();
            sb.append(" { ");
            sb.append("     \"code\":403, ");
            sb.append("     \"success\":false, ");
            sb.append("     \"data\":\"[ERROR===>>>]No permission!\" ");
            sb.append(" } ");
            response.getWriter().write(sb.toString());
        }
        return isPermitted;
    }


}
