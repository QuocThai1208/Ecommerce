package com.ecommerce.order_service.exception;

public record RemoteError(int code, String message) {}