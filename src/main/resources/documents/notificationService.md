## Descrição Detalhada
O NotificationService é responsável por gerenciar todas as notificações da aplicação, seja por e-mail, SMS, push notifications ou outros canais. Ele permite que outros serviços enviem notificações de forma padronizada e confiável.

## Responsabilidades

Enviar notificações por diversos canais (e-mail, SMS, push notifications).
Personalizar o conteúdo das notificações.
Agendar o envio de notificações.
Gerenciar templates de notificação.
Rastrear o status de entrega das notificações.
## Tecnologias Utilizadas

Linguagem de programação: Node.js
Framework: Express.js
Banco de dados: MongoDB
Ferramentas: Docker, Kubernetes, AWS SNS
## APIs

API 1: Enviar Notificação
Descrição: Envia uma notificação para um usuário.
Método: POST
URL: /notifications
Request:
JSON
{
"userId": 123,
"channel": "email",
"template": "new_order",
"data": {
"orderId": 456,
"amount": 100.00
}
}
Use o código com cuidado.

Response:
JSON
{
"notificationId": "noti_123",
"status": "sent"
}
Use o código com cuidado.

API 2: Listar Notificações
Descrição: Lista todas as notificações de um usuário.
Método: GET
URL: /notifications?userId=123
Response:
JSON
[
{
"id": "noti_123",
"channel": "email",
"status": "sent",
"createdAt": "2023-11-22T10:23:45Z"
},
// ... outras notificações
]
Use o código com cuidado.

## Dependências

GodService (para autenticação)
