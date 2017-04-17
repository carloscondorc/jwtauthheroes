# JWT Token Auth and REST API For Angular Tour of Heroes example
JWT token auth to facilitate logging in and protecting the API for the Angular Tour of Heroes example. 

# Getting Started

1. Ensure that you have java 8 and maven installed.
2. Set your secret and issuer in the application properties file.
3. Start the Spring Boot application.
4. Start the accompanying Tour of Heroes repository and login with admin/admin.
5. Check out the dashboard, the heroes, delete a hero, add a hero, search heroes, edit a hero.

# Tutorial Series
This is a series of tutorials on my way to learning JWT with Spring Security.

1. Setup JWT and issue tokens(jwtauth).
2. Utilize the token to protect calls(jwtauthstep2).
3. Enhance on step 2 to back the tour of heroes angular example.

# What are some of the major changes in this step of the tutorial series.
1. Added POM entires for Spring Data JPA and H2.
2. Added config/RestConfig
3. Added config/RestRepositoryConfig
4. Added line to WebSecurityConfig for OPTIONS requests
5. Added model/hero
6. Added repository/HeroRepository
7. Added import.sql to prepopulate some heroes.

# Things to do

* Cleanup the Tour of Heroes angular app and upload it to an accompanying repository.
* Create a reusable verifier object (currently creating a new one each time).
* Setup a proper spring properties object rather than using @Value.
* Figure out using Spring Boot properties for stateless sessions and to disable caching.
* Revisit logic in the JwtAuthFilter not sure I like it.
* Dig into RestConfig/RestRepositoryConfig/WebSecurityConfig seems like way to many spots I need to allow CORS/OPTIONS
* Create a return object for a token rather than just sending it back in the body.
* Write some tests.

# References
A big thanks to the following posts which helped guide me through some tough spots when figuring this out

* [Angular Tour of Heroes Tutorial](https://angular.io/docs/ts/latest/tutorial/)
* [Integrating Angular 2 with Spring Boot, JWT, and CORS, Part 1](http://chariotsolutions.com/blog/post/angular-2-spring-boot-jwt-cors_part1/)
* [Integrating Angular 2 with Spring Boot, JWT, and CORS, Part 2](http://chariotsolutions.com/blog/post/angular-2-spring-boot-jwt-cors_part2//)

