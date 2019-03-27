# easy-carros-backend-challenge
Desafio de backend da easy carros

# criar o banco de dados

``# mongo create-database.js``

# Docker

``docker stop easycarros``

``docker rm easycarros``

``docker build --no-cache -t easycarros:latest .``

``docker run --name easycarros -it -d -p 8080:8080 easycarros``

``docker logs easycarros -f``