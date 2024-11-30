# Product Calculator Service

## Intention
Implement the simpliest solution possible with minimum code/dependencies.

---

## Intuition

1. **Possibility of applying discounts are the features; not the main functionality.**

2. **Prices for products:**  
   I didn't want to setup whole DB, run some liquibase scripts to setup initial data.  
   I've create `ProductsDao` that will return random price in some range.

3. **Configurable policies:**
    - At first, I wanted to have 2 `.properties` files, that we will be listening for updates, and if any, read it out and update our `TreeMap`. But that means if application is deployed on instance, we will need to SSH and manually update it, which is not convenient.
    - Second option, is to create endpoints for policy updates, but that will mean we need authentication, to restrict those endpoint from average users, introducing a lot of boilerplate code.
    - Third option, is to use JMX and expose few methods like enable/disable/update policy.
    - I chose to go with saving policies into a **Consul service**, since it is more UI user-friendly (IMO), and more understandable for non-programming people, let's say some Store owner.  
      On our project, we use JMX + Consul to update values and pick them up on instance restart, so they will boot with already predefined options.
4. **Applying discount:**
   Utilize ```TreeMap.floorEntry``` method to get the correct discount.
---

## Tools

- Spring Initializr to generate project
- Spring Boot
- Spring WebFlux to work in non-blocking way
- Consul to store and update discount policies
- Docker / Docker Compose

---

## How to Run

```bash
docker compose up -d
```
You can run test requests from `generated-requests.http` file.

