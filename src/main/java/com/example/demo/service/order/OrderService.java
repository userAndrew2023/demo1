package com.example.demo.service.order;

import com.example.demo.dto.order.OrderDto;
import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderSide;
import com.example.demo.entity.order.OrderStatus;
import com.example.demo.entity.order.OrderType;
import com.example.demo.entity.stock.Stock;
import com.example.demo.entity.user.User;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.stock.StockRepository;
import com.example.demo.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, StockRepository stockRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getByStockSymbol(String symbol) {
        return orderRepository.findByStockSymbolAndStatus(symbol, OrderStatus.CREATED);
    }

    public Order create(OrderDto orderDto) {
        Stock stock = stockRepository.findBySymbol(orderDto.getStockSymbol());
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow();

        return switch (orderDto.getType()) {
            case "limit" -> createLimit(orderDto, user, stock);
            case "market" -> createMarket(orderDto, user, stock);
            default -> throw new RuntimeException("Неизвестный тип ордера");
        };
    }

    private Order createMarket(OrderDto orderDto, User user, Stock stock) {
        List<Order> orders = orderRepository.findByStockSymbolAndStatus(stock.getSymbol(), OrderStatus.CREATED);
        Order newOrder;
        double maxQuantity;
        Float currentPrice = stock.getPrice();
        switch (orderDto.getType()) {
            case "buy":
                orders = orders.stream()
                        .filter(order -> order.getSide() == OrderSide.SELL)
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                maxQuantity = orders.stream()
                        .mapToDouble(Order::getQuantity)
                        .sum();
                if (maxQuantity <= orderDto.getDollars() / currentPrice) {
                    throw new RuntimeException("Нельзя создать рыночный ордер, превышающий ликвидацию рынка");
                }
                for (Order currentOrder : orders) {
                    Float quantity = orderDto.getDollars() / currentOrder.getPrice();
                    if (quantity.equals(currentOrder.getQuantity())) {
                        newOrder = Order.builder()
                                .quantity(quantity)
                                .price(currentOrder.getPrice())
                                .side(OrderSide.BUY)
                                .status(OrderStatus.CREATED)
                                .type(OrderType.MARKET)
                                .stock(stock)
                                .user(user)
                                .build();
                        currentOrder.setStatus(OrderStatus.FILLED);
                        this.orderRepository.save(newOrder);
                        this.orderRepository.save(currentOrder);

                        return newOrder;
                    } else if (quantity < currentOrder.getQuantity()) {
                        currentOrder.setQuantity(currentOrder.getQuantity() - quantity);

                        newOrder = Order.builder()
                                .quantity(quantity)
                                .price(currentOrder.getPrice())
                                .side(OrderSide.BUY)
                                .status(OrderStatus.CREATED)
                                .type(OrderType.MARKET)
                                .stock(stock)
                                .user(user)
                                .build();
                        this.orderRepository.save(newOrder);
                        this.orderRepository.save(currentOrder);

                        return newOrder;
                    } else if (quantity > currentOrder.getQuantity()) {
                        currentOrder.setStatus(OrderStatus.FILLED);
                        orderDto.setQuantity(orderDto.getQuantity() - currentOrder.getQuantity());

                        orderRepository.save(currentOrder);
                    }
                }
                break;
            case "sell":
                orders = orders.stream()
                        .filter(order -> order.getSide() == OrderSide.BUY)
                        .sorted(Comparator.comparing(Order::getPrice).reversed())
                        .toList();
                maxQuantity = orders.stream()
                        .mapToDouble(Order::getQuantity)
                        .sum();
                if (maxQuantity <= orderDto.getDollars() / currentPrice) {
                    throw new RuntimeException("Нельзя создать рыночный ордер, превышающий ликвидацию рынка");
                }
                for (Order currentOrder : orders) {
                    Float quantity = orderDto.getDollars() / currentOrder.getPrice();
                    if (quantity.equals(currentOrder.getQuantity())) {
                        newOrder = Order.builder()
                                .quantity(quantity)
                                .price(currentOrder.getPrice())
                                .side(OrderSide.SELL)
                                .status(OrderStatus.CREATED)
                                .type(OrderType.MARKET)
                                .stock(stock)
                                .user(user)
                                .build();
                        currentOrder.setStatus(OrderStatus.FILLED);
                        this.orderRepository.save(newOrder);
                        this.orderRepository.save(currentOrder);

                        return newOrder;
                    } else if (quantity < currentOrder.getQuantity()) {
                        currentOrder.setQuantity(currentOrder.getQuantity() - quantity);

                        newOrder = Order.builder()
                                .quantity(quantity)
                                .price(currentOrder.getPrice())
                                .side(OrderSide.SELL)
                                .status(OrderStatus.CREATED)
                                .type(OrderType.MARKET)
                                .stock(stock)
                                .user(user)
                                .build();
                        this.orderRepository.save(newOrder);
                        this.orderRepository.save(currentOrder);

                        return newOrder;
                    } else if (quantity > currentOrder.getQuantity()) {
                        currentOrder.setStatus(OrderStatus.FILLED);
                        orderDto.setQuantity(orderDto.getQuantity() - currentOrder.getQuantity());

                        orderRepository.save(currentOrder);
                    }
                }
                break;
            default:
                throw new RuntimeException("Неверный Side ордера");
        }
        throw new RuntimeException("Что-то пошло не так");
    }

    private Order createLimit(OrderDto orderDto, User user, Stock stock) {
        if (orderDto.getPrice() == null) {
            throw new RuntimeException("Не указана цена");
        }
        if (orderDto.getQuantity() == null) {
            throw new RuntimeException("Не указано количество");
        }
        Order order;
        switch (orderDto.getSide()) {
            case "buy":
                if (orderDto.getPrice() >= stock.getPrice()) {
                    orderDto.setType("market");
                    orderDto.setQuantity(orderDto.getQuantity() * orderDto.getPrice());
                    return createMarket(orderDto, user, stock);
                }
                order = Order.builder()
                        .price(orderDto.getPrice())
                        .quantity(orderDto.getQuantity())
                        .status(OrderStatus.CREATED)
                        .type(OrderType.LIMIT)
                        .side(OrderSide.BUY)
                        .user(user)
                        .stock(stock)
                        .build();
                return orderRepository.save(order);
            case "sell":
                if (orderDto.getPrice() <= stock.getPrice()) {
                    orderDto.setType("market");
                    orderDto.setQuantity(orderDto.getQuantity() * orderDto.getPrice());
                    return createMarket(orderDto, user, stock);
                }
                order = Order.builder()
                        .price(orderDto.getPrice())
                        .quantity(orderDto.getQuantity())
                        .status(OrderStatus.CREATED)
                        .type(OrderType.LIMIT)
                        .side(OrderSide.SELL)
                        .user(user)
                        .stock(stock)
                        .build();
                return orderRepository.save(order);
            default:
                throw new RuntimeException("Неверный Side ордера");
        }
    }
}
