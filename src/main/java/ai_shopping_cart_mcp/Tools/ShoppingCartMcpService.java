package ai_shopping_cart_mcp.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import ai_shopping_cart_mcp.Entity.CartItem;
import ai_shopping_cart_mcp.Repository.CartItemRepository;

@Service
public class ShoppingCartMcpService {
  
        private CartItemRepository cartItemRepository;

        public ShoppingCartMcpService(CartItemRepository cartItemRepository) {
            this.cartItemRepository = cartItemRepository;
        }
        //Catalog Service
        private static final Map<String, Double> productCatalog =
        Map.of(
            "iphone", 10.0,
            "ipad", 20.0,
            "MacBook", 30.0
        );
        @Tool(name = "addToCart", description = "Add a product to the shopping cart")
        public String addToCart( @ToolParam String productName, @ToolParam int quantity)
        {
            if(!productCatalog.containsKey(productName))
            {
                return "Product not found in catalog";
            }
            Double price =productCatalog.get(productName);
            CartItem cartItem=cartItemRepository.findByProductId(productName);
            if(cartItem==null){
                cartItem=new CartItem();
                cartItem.setProductId(productName);
                cartItem.setProductName(productName);
                cartItem.setQuantity(quantity);
            }
            else{
                cartItem.setQuantity(cartItem.getQuantity()+quantity);
            }
            cartItem.setPrice(cartItem.getQuantity()*price);
            cartItemRepository.save(cartItem);
            return "Product added to cart successfully total price is "+cartItem.getPrice();
        }   
        @Tool(name = "removeFromCart", description = "Remove a product from the shopping cart")
            public String removeCart(@ToolParam String productName)
            {
                CartItem cartItem=cartItemRepository.findByProductId(productName);
                if(cartItem==null){
                    return "Product not found in cart";
                }
                cartItemRepository.deleteByProductId(productName);
                return "Product removed from cart successfully";
            }
            @Tool(name = "getCart", description = "Get the details of a product in the shopping cart")
            public String getCart(@ToolParam String productName)
            {
                CartItem cartItem=cartItemRepository.findByProductId(productName);
                if(cartItem==null){
                    return "Product not found in cart";
                }
                return "Product: "+cartItem.getProductName()+" Quantity: "+cartItem.getQuantity()+" Price: "+cartItem.getPrice();
            }
            @Tool(name = "getAllCartItems", description = "Get the details of all products in the shopping cart")
            public List<CartItem> getAllCartItems(){
                return cartItemRepository.findAll();
            }
            @Tool(name = "getCartTotal", description = "Get the total price of all products in the shopping cart")
            public double getCartTotal()
            {
                return cartItemRepository.findAll().stream().mapToDouble(CartItem::getPrice).sum();

            }
}