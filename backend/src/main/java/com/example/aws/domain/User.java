package com.example.aws.domain;

public record User(
        String id,
        String name,
        String email,
        UserRole role
) {



}
