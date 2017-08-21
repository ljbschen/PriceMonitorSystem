# PriceMonitorSystem
## User Interests
* Pull latest deal information in category base
* Receive deal notification periodically from system

## Database & Cache Design
* Price Monitoring System: 
    * Cassandra for persistence, Column oriented No-SQL Database
    * Redis for cache, Customized Data-structure key value store
* Recommendation System: 
    * MongoDB for persistence, User retrieve only, Simple query logic, category based collections
    
* Seeds Management: 
    * MongoDB
    
## Product Detail
    '{
    "adId": "B0728FL8KT",
    "category": "SkinCare",
    "title": "Bea facial peel off black mask cleaning mask ",
    "price": 13.99,
    "prevPrice": 99999,
    "detail_url": https://www.amazon.com/facial-peel-black-cleaning-moisturizing/dp/B0728FL8KT/ref=lp_11060711_1_48_s_it/141-3090949-4694514?s=beauty&ie=UTF8&qid=1502840931&sr=1-48
    }'

## Product Crawler Design
* Scheduler: 
  * Eureka + Ribbon implemented client side load balancer and auto discovery.
  * @Schedule + ScheduledExecutorService handled multi-thread tasks monitoring and scheduling.
* Product Crawler: 
  * Multi-Instances + Eureka implemented auto registration and failover.
  * Topic-RabbitMQs decoupled from PriceMonitoringSystem
  

## Price Monitor System
* Category oriented instance will pull message from THE category queue.
* Price comparison will be made to determine a deal detected or not.
* Serialize into POJO and store into Cassandra database
* Message will be pushed to Deal Queue if deal is confirmed.
![alt text](https://user-images.githubusercontent.com/9488989/29541827-8be28516-868b-11e7-99a2-2bd639ef29e2.png)

