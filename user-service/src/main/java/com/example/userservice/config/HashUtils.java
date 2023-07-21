package com.example.userservice.config;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public abstract class HashUtils {
    public static String Sha256Hash(String str) {
        return Hashing
                .sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
