package com.retailsense.common.exception;

public class InsufficientStockException extends RuntimeException {
    private final Integer available;
    private final Integer requested;

    public InsufficientStockException(Integer available, Integer requested) {
        super(String.format("Insufficient stock. Available: %d, Requested: %d", available, requested));
        this.available = available;
        this.requested = requested;
    }

    public Integer getAvailable() {
        return available;
    }

    public Integer getRequested() {
        return requested;
    }
}