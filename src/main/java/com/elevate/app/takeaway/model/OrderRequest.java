package com.elevate.app.takeaway.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    @NotNull(message = "User is required")
    private long userId;

    @NotNull(message = "Address is required")
    private long addressId;
    private List<OrderItemModel> orderItems;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }
}
