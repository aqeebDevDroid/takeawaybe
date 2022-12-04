package com.elevate.app.takeaway.service;

import com.elevate.app.takeaway.dto.order.Order;
import com.elevate.app.takeaway.dto.order.OrderItem;
import com.elevate.app.takeaway.dto.order.OrderItemResponse;
import com.elevate.app.takeaway.dto.order.OrderResponse;
import com.elevate.app.takeaway.dto.product.Product;
import com.elevate.app.takeaway.dto.user.User;
import com.elevate.app.takeaway.dto.user.UserAddress;
import com.elevate.app.takeaway.exceptions.CustomException;
import com.elevate.app.takeaway.model.OrderItemModel;
import com.elevate.app.takeaway.model.OrderRequest;
import com.elevate.app.takeaway.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAddressRepository addressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(
                () -> new CustomException("Invalid User"));
        UserAddress address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(
                () -> new CustomException("Invalid Address"));
        try {
            Order order = new Order();
            order.setUser(user);
            order.setUserAddress(address);
            order.setOrderDate(new Date());
            double orderAmount = 0.0;
            long orderId = orderRepository.save(order).getOrderId();
            for (OrderItemModel userOrderItem : orderRequest.getOrderItems()) {
                OrderItem dbOrderItem = new OrderItem();
                dbOrderItem.setOrderId(orderId);
                Product product = productRepository.findById(userOrderItem.getProductId())
                        .orElseThrow(() -> new CustomException("Invalid Product Added"));
                dbOrderItem.setProductId(userOrderItem.getProductId());
                dbOrderItem.setQuantity(userOrderItem.getQuantity());
                orderAmount += product.getPrice();
                orderItemRepository.save(dbOrderItem);
            }
            order.setOrderAmount(orderAmount);
            order.setOrderId(orderId);
            //Update Order with final Order Amount
            orderRepository.save(order);
        } catch (Exception e) {
            throw new CustomException("Error processing your order");
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderResponse> getOrderByUserId(long userId) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        Optional<List<Order>> dbOrders = Optional.ofNullable(orderRepository.getOrdersByUserId(userId).orElseThrow(() -> new CustomException("No Orders Found")));
        if(dbOrders.isPresent()) {
            for(Order dbOrder : dbOrders.get()) {
                OrderResponse response = new OrderResponse();
                response.setOrderId(dbOrder.getOrderId());
                response.setOrderDate(dbOrder.getOrderDate());
                response.setOrderAmount(dbOrder.getOrderAmount());
                List<OrderItemResponse> orderItems = new ArrayList<>();
                List<OrderItem> dbOrderItems = orderItemRepository.findByOrderId(dbOrder.getOrderId());
                for(OrderItem orderItem : dbOrderItems) {
                    Product product = productRepository.findById(orderItem.getProductId()).get();
                    OrderItemResponse orderItemResponse = new OrderItemResponse();
                    orderItemResponse.setOrderItemId(orderItem.getOrderItemId());
                    orderItemResponse.setQuantity(orderItem.getQuantity());
                    orderItemResponse.setName(product.getName());
                    orderItemResponse.setCategory(product.getCategory());
                    orderItemResponse.setType(product.getType());
                    orderItemResponse.setPrice(product.getPrice());
                    orderItems.add(orderItemResponse);
                }
                response.setOrderItemResponses(orderItems);
                orderResponses.add(response);
            }
        }
        return orderResponses;
    }
}
