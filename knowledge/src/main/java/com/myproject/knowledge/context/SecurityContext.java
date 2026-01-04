package com.myproject.knowledge.context;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;


/**
 * Security context information.
 */
@Slf4j
public final class SecurityContext {

    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    private SecurityContext() {
    }

    /**
     * Gets the tenant identifier from current context.
     * @return The Tenant identifier.
     */
    public static String getTenantId() {
       return TENANT_ID.get();
    }

    public static String getUser() {
        return USERNAME.get();
    }

    /**
     * Sets the tenant identifier in thread local variable.
     * @param tenantId The tenant identifier.
     */
    public static void setTenantId(final String tenantId) {
       
        if (Objects.nonNull(TENANT_ID.get())) {
            TENANT_ID.remove();
        }
        log.debug("Setting tenantId to {}" , tenantId);
        TENANT_ID.set(tenantId);
    }

    public static void setUser(final String username) {
        if (Objects.nonNull(USERNAME.get())) {
            USERNAME.remove();
        }
        log.debug("Setting User to {}" , username);
        USERNAME.set(username);
    }

    /**
     * Clears the current thread local context.
     */
    public static void clear() {
        TENANT_ID.remove();
        USERNAME.remove();
    }
}