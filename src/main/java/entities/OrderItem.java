package entities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItem {
    String id;
    LocalDateTime date;
    String status;
    Map<ProductItem, Integer> products;
    Map<String, String> details; // shiping address, description etc.
}
