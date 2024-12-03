# Small Shop

### Author: Ivar Mihhailov / imihhail

## Description

Project objective is to create spring boot user authentication and authorization with jwt token. Also with little bit frontend added. Project is using postgresql for database. Admin user and normal user can do different things. Admin can insert new products, delete products and see all products. Normal user can buy products and see only owned and available products.

# Usage: how to run ?

1. Run the spring boot application

2. cd frontend

3. Run npm install

4. Run npm start and go to localhost:3030

5. Admin user is already registered. 
    Username: Admin
    Password: 1234

6. If you dont have postgresql installed, then you can also get database wtih Docker.
    Docker commands are:
        docker pull ivardr/postgres:latest
        docker run -d --name postgres -p 5432:5432 --env-file .env ivardr/postgres:latest
