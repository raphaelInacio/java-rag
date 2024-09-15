# ShippingService

## Descrição Detalhada
O ShippingService é responsável por gerenciar todas as operações relacionadas ao envio de produtos, desde a criação de etiquetas de envio até o rastreamento de pacotes. Ele depende do GodService para autenticação e do ProductService para obter informações sobre os produtos a serem enviados.

## Responsabilidades

Criar etiquetas de envio.
Calcular custos de envio.
Rastrear pacotes.
Gerenciar diferentes transportadoras.
## Tecnologias Utilizadas

Linguagem de programação: Node.js
Framework: Express.js
Banco de dados: MongoDB
Ferramentas: Docker, Kubernetes
## APIs

API 1: Criar Etiquetas de Envio
Descrição: Cria etiquetas de envio para um pedido.
Método: POST
URL: /shipments
Request:
JSON
{
"orderId": 123,
"shippingAddress": {
"street": "Rua Principal",
"city": "São Paulo",
"country": "Brasil"
}
}
Use o código com cuidado.

Response:
JSON
{
"shippingLabel": "base64encodedlabel",
"trackingNumber": "123456789"
}
Use o código com cuidado.

## Dependências

GodService
ProductService
