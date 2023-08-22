package com.example.orderservice.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ContextHolder {
    public static final String CORRELATION_ID = "correlationId";

    private static final ThreadLocal<ContextData> holder = new ThreadLocal<>();

    public void set(ContextData context) {
        holder.set(context);
    }

    public ContextData get() {
        return holder.get();
    }

    public void remove() {
        holder.remove();
    }

    public boolean isAuthenticated() {
        var customContext = this.get();

        var isUsernameEmptyOrNull = StringUtils.isEmpty(customContext.getUsername());

        return !isUsernameEmptyOrNull && customContext.getUserId() != null;
    }

    public String getCorrelationId() {
        return this.get().getCorrelationId();
    }

    public Long getUserId() {
        return this.get().getUserId();
    }

    public String getUsername() {
        return this.get().getUsername();
    }
}
