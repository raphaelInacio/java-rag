# ProductService

## Descrição Detalhada

O microserviço ProductService é responsável por gerenciar o ciclo de vida completo de produtos em nosso sistema e-commerce. Ele oferece funcionalidades como criação, leitura, atualização e deleção de produtos, além de gerenciar atributos, preços, estoque e outras informações relevantes sobre cada produto. O ProductService também é responsável por integrar-se com outros microserviços, como o InventoryService para verificar a disponibilidade de estoque e o PricingService para calcular preços dinâmicos.

## Responsabilidades

Gerenciamento de produtos: Criação, leitura, atualização e deleção de produtos.
Gerenciamento de atributos: Definição e associação de atributos a produtos.
Gerenciamento de preços: Definição de preços base e cálculo de preços dinâmicos.
Gerenciamento de estoque: Integração com o InventoryService para verificar a disponibilidade de estoque.
Busca e filtragem de produtos: Oferecimento de mecanismos de busca avançados para encontrar produtos com base em diversos critérios.
Gerenciamento de categorias: Organização de produtos em categorias e subcategorias.
Gerenciamento de imagens: Upload, armazenamento e exibição de imagens de produtos.

## Tecnologias Utilizadas

Linguagem de programação: Java
Framework: Spring Boot
Banco de dados: PostgreSQL
Mensageria: RabbitMQ
Containerização: Docker
Orquestração: Kubernetes
Nuvem: AWS
Documentação de APIs: Swagger
## APIs

### Criar um Produto

Método: POST
Endpoint: /products
Requisição:
JSON
{
"name": "Smartphone X",
"description": "Smartphone de última geração",
"price": 2999.99,
"category": "Eletrônicos",
"attributes": {
"tela": "6.5 polegadas",
"camera": "48MP"
}
}
Use o código com cuidado.

Resposta:
JSON
{
"id": 123,
"name": "Smartphone X",
// ... outros campos
}
Use o código com cuidado.

### Buscar um Produto por ID

Método: GET
Endpoint: /products/{id}
Resposta:
JSON
{
// ... dados do produto
}
Use o código com cuidado.

### Atualizar um Produto

Método: PUT
Endpoint: /products/{id}
Requisição:
JSON
{
"name": "Smartphone X (Novo)",
// ... outros campos
}
Use o código com cuidado.

### Deletar um Produto

Método: DELETE
Endpoint: /products/{id}
### Buscar Produtos por Categoria

Método: GET
Endpoint: /products/category/{category}
### Buscar Produtos por Nome

Método: GET
Endpoint: /products/search?name={nome}
