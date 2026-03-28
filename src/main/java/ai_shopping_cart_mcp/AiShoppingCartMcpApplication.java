package ai_shopping_cart_mcp;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ai_shopping_cart_mcp.Tools.ShoppingCartMcpService;

import java.util.List;

@SpringBootApplication
public class AiShoppingCartMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiShoppingCartMcpApplication.class, args);
	}
}
