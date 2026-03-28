package ai_shopping_cart_mcp.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ai_shopping_cart_mcp.Entity.CartItem;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    CartItem findByProductId(String productId);
    void deleteByProductId(String productId);
}
