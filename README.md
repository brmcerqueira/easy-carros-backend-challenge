# easy-carros-backend-challenge

Desafio de backend da Easy Carros.

# Requisitos

- Docker
- Docker Compose
- MongoDb

# Rodando a aplicação com o Docker

Para executar a aplicação vamos abrir um terminal na raiz do projeto e executar o seguinte comando

``# docker-compose up -d``

Apos isso, precisamos dar uma carga inicial no banco de dados, no mesmo terminal vamos executar o seguinte comando

``# mongo database/create-database.js``

Nesse momento a aplicação deve esta rodando normalmente.

Se achar necessario podemos observar o log da aplicação com o seguinte comando 

``# docker logs easycarros -f``

# Rotas da aplicação

- Rota para fazer a autenticação do usuario

    * Rota: http://localhost:8080/api/sign/in

    * Tipo: POST

    * Corpo da requisição:
    
        ```json
        {
          "email": "Talita.Costa@yahoo.com",
          "password": "b6Yi7N0uVJBFFEz"
        }
        ```
        
    * Resposta esperada(Token de autorização)
    
        ```
        eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjanNnaXl0dzUwMDBrZW53Z2ZqbmFodzlsIiwiaXNzIjoiZWFzeWNhcnJvcyIsImlhdCI6MTU1Mzc4NjMxOCwiZXhwIjoxNTU0MDQ1NTE4LCJhdWQiOiJmcm9udC1lbmQiLCJwZXJtaXNzaW9ucyI6W119.RogpIigEV5DjxuoKU-Bb6ctv-UOe_7wo7ZfK_kormb0
        ```
           
- Rota para fazer uma requisição de um serviço para o usuario corrente

    * Rota: http://localhost:8080/api/attendance/request

    * Tipo: POST
    
    * Requer token de autorização: Sim

    * Corpo da requisição:
    
        ```json
        {
          "kind": 0, //Tipo de serviço, 0 = Troca de Oleo, 1 = Lavagem de carro
          "lat": -23.5609048, //latitude
          "lng": -46.6849555 //longitude
        }
        ```
     * Resposta esperada
     
        ```json
         {
             "_id": "cjsgiytw40001enwgfx7w90kz",
             "name": "Mércia Oliveira",
             "location": {
                 "type": "Point",
                 "coordinates": [-46.6857404, -23.5566759]
             },
             "availableServices": [0, 1]
         }
         ``` 

 - Rota para fazer uma busca por serviços disponiveis naquele endereço
 
     * Rota: http://localhost:8080/api/attendance/search?address=Av. Paulista, 1578 - Bela Vista
 
     * Tipo: GET
     
     * Requer token de autorização: Sim
     
     * Resposta esperada
     
        ```json
            [{
                    "_id": "cjsgiytw40001enwgfx7w90kz",
                    "name": "Mércia Oliveira",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6857404, -23.5566759]
                    },
                    "availableServices": [0, 1]
            }, {
                    "_id": "cjsgiytw40003enwgfcx5gsn9",
                    "name": "Feliciano Barros",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6849555, -23.5609048]
                    },
                    "availableServices": [1, 0]
            }, {
                    "_id": "cjsgiytw40004enwg68ufh5wy",
                    "name": "Fábio Barros",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6857404, -23.5566759]
                    },
                    "availableServices": [0]
            }, {
                    "_id": "cjsgiytw40006enwge0ushrqx",
                    "name": "Karla Albuquerque",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.627023, -23.619575]
                    },
                    "availableServices": [0, 1]
            }, {
                    "_id": "cjsgiytw50008enwgfx8ratik",
                    "name": "Antônio Braga",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6849555, -23.5609048]
                    },
                    "availableServices": [1]
            }, {
                    "_id": "cjsgiytw50009enwg6ige96b4",
                    "name": "Alessandro Braga",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6857404, -23.5566759]
                    },
                    "availableServices": [1]
            }, {
                    "_id": "cjsgiytw5000cenwg02sph3ih",
                    "name": "Fábio Silva",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6849555, -23.5609048]
                    },
                    "availableServices": [1, 0]
            }, {
                    "_id": "cjsgiytw5000eenwga5xf5akk",
                    "name": "Larissa Costa",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6857404, -23.5566759]
                    },
                    "availableServices": [1, 0]
            }, {
                    "_id": "cjsgiytw5000henwgh1yy41o1",
                    "name": "Lorena Xavier",
                    "location": {
                            "type": "Point",
                            "coordinates": [-46.6849555, -23.5609048]
                    },
                    "availableServices": [0, 1]
            }]
         ```