package com.example.orderservice.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomContextHolder {
    private static final ThreadLocal<CustomContext> holder = new ThreadLocal<>();

    public void set(CustomContext context) {
        holder.set(context);
    }

    public CustomContext get() {
        return holder.get();
    }

    public void remove() {
        holder.remove();
    }

    public boolean isAuthenticated() {
        var customContext = get();

        var isUsernameEmptyOrNull = StringUtils.isEmpty(customContext.getUsername());

        return !isUsernameEmptyOrNull && customContext.getUserId() != null;
    }

    public String getCorrelationId() {
        return get().getCorrelationId();
    }

    public Long getUserId() {
        return get().getUserId();
    }

    public String getUsername() {
        return get().getUsername();
    }
}
