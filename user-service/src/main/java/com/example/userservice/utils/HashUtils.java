package com.example.userservice.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public abstract class HashUtils {
    public static String sha256Hash(String str) {
        return Hashing
                .sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
