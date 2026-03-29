# ai-shopping-cart-mcp

## MCP (Model Context Protocol)

MCP (Model Context Protocol) is a protocol that allows AI models to interact with external systems, tools, and data in a structured and controlled way.

MCP acts as a bridge between an AI model and your application, helping the model understand what actions it can perform and how to perform them.

### Without MCP
The AI model does not know how to interact with your system or call your APIs.

### With MCP
The AI model can:
- Understand the user’s request  
- Call the appropriate APIs  
- Pass the correct data  
- Receive responses and act accordingly  

---

## AI Shopping Cart Use Case

For example, if a user says:

“Add the cheapest iPhone to my cart”

With MCP, the AI can:
1. Understand the user’s intent  
2. Call the product service  
3. Identify the cheapest iPhone  
4. Add it to the cart using the appropriate API  

All of this happens in a structured and seamless way using MCP.

---
<img width="900" height="215" alt="CLAUDE Desktop" src="https://github.com/user-attachments/assets/7d5d5785-f00f-4778-90f5-c326153033f4" />

## Shopping Cart MCP Server

In this project, we build a complete Shopping Cart MCP server using Spring Boot and integrate it with Claude Desktop.

The following tools are exposed:

- `addToCart` – Add a product to the cart  
- `removeCart` – Remove a product from the cart  
- `getCart` – Retrieve all items in the cart  
- `getCartTotal` – Get the total price of the cart  

We create the MCP server using Spring AI and expose these endpoints so that the AI model can interact with the system.

For the model, we use Claude Desktop.

---

## Claude Desktop Configuration

We configured the `claude_desktop_config.json` file to register our MCP server:

```json
{
  "mcpServers": {
    "shopping-cart-server": {
      "command": "java",
      "args": [
        "-jar",
        "/Users/adarshkumar/Desktop/Learning/ai-shopping-cart-mcp/target/shopping-cart-server.jar"
      ]
    }
  }
}
```
Below is the developer settings configuration in Claude Desktop:

<img width="890" height="562" alt="image" src="https://github.com/user-attachments/assets/018d117d-720d-48b2-9174-e45d2aad6f78" />

---

## Sample Output

Screenshots after successfully connecting to Claude:

<img width="820" height="450" alt="image" src="https://github.com/user-attachments/assets/52a04f4b-4edb-4915-9702-2878a31fa26d" />
<img width="778" height="209" alt="image" src="https://github.com/user-attachments/assets/902b1d83-0a55-4c89-b2bd-074b35046c4a" />
<img width="797" height="359" alt="image" src="https://github.com/user-attachments/assets/107dfd81-760c-4142-998c-174f4700211b" />

---
# Issues I faced and fixes

## 1. Wrong JAR Name

Initially, different JAR names were used (like ai-shopping-cart-mcp-0.0.1-SNAPSHOT.jar), which caused startup failures.

Fix:
Ensure the correct final JAR name is used:

shopping-cart-server.jar

## 2. Logs Written to stdout

MCP communicates over stdout using JSON. If your application writes logs to stdout, it corrupts the communication.

Error you may see:

Unexpected non-whitespace character after JSON at position 4

Fix:

Redirect logs to stderr using logback-spring.xml
Update application.properties:
spring.main.banner-mode=off
logging.pattern.console=%d{HH:mm:ss} %-5level - %msg%n

## 3. Log File Write Error

Logback tried to write logs to a file (app.log), but the environment was read-only, causing the app to crash.

Fix:

Remove file appenders from logback configuration
Use only console logging (stderr)

## 4. Tools Not Registered (tools: [])

The MCP server connected successfully, but no tools were exposed.

Reason:
@Tool annotated methods were not registered with the MCP server.

Fix:
Add a ToolCallbackProvider bean:

```java
@Bean
public ToolCallbackProvider shoppingCartTools(ShoppingCartMcpService service) {
    return MethodToolCallbackProvider.builder()
            .toolObjects(service)
            .build();
}
```

## What Not to Do
- Do not print anything to stdout (it breaks MCP communication)
- Do not use incorrect JAR names or outdated builds
- Do not forget to register tools using ToolCallbackProvider
- Do not use file-based logging in restricted environments
- Do not skip restarting Claude Desktop after config changes
