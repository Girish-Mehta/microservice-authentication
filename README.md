# microservice-authentication
This is an implementation of Netflix Architecture to create a login and register service

There are 4 services running on the following ports:-
1. Database Service     - 8484
2. Login Service        - 8200
3. Register Service     - 8100
4. Eureka               - 8761
5. Zuul                 - 8765

To run the Application run the services in the following order:-
  1. Eureka
  2. Database Service
  3. Login Service
  4. Register Service
  5. Zuul
