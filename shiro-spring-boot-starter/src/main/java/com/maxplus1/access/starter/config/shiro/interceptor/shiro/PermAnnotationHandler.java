package com.maxplus1.access.starter.config.shiro.interceptor.shiro;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * Checks to see if a @{@link com.maxplus1.access.starter.config.shiro.interceptor.shiro.Perms Perms} annotation is
 * declared, and if so, performs a permission check to see if the calling <code>Subject</code> is allowed continued
 * access.
 *
 * @since 0.9.0
 */
@Deprecated
public class PermAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler looks for
     * {@link com.maxplus1.access.starter.config.shiro.interceptor.shiro.Perms Perms} annotations.
     */
    public PermAnnotationHandler() {
        super(Perms.class);
    }

    /**
     * Returns the annotation {@link Perms#value value}, from which the Permission will be constructed.
     *
     * @param a the Perms annotation being inspected.
     * @return the annotation's <code>value</code>, from which the Permission will be constructed.
     */
    protected String[] getAnnotationValue(Annotation a) {
        Perms rpAnnotation = (Perms) a;
        return rpAnnotation.value();
    }

    /**
     * Ensures that the calling <code>Subject</code> has the Annotation's specified permissions, and if not, throws an
     * <code>AuthorizingException</code> indicating access is denied.
     *
     * @param a the RequiresPermission annotation being inspected to check for one or more permissions
     * @throws org.apache.shiro.authz.AuthorizationException
     *          if the calling <code>Subject</code> does not have the permission(s) necessary to
     *          continue access or execution.
     */
    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (!(a instanceof Perms)) return;

        Perms rpAnnotation = (Perms) a;
        String[] perms = getAnnotationValue(a);
        Subject subject = getSubject();

        if (perms.length == 1) {
            subject.checkPermission(perms[0]);
            return;
        }
        if (Logical.AND.equals(rpAnnotation.logical())) {
            getSubject().checkPermissions(perms);
            return;
        }
        if (Logical.OR.equals(rpAnnotation.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms) if (getSubject().isPermitted(permission)) hasAtLeastOnePermission = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOnePermission) getSubject().checkPermission(perms[0]);

        }
    }
}
