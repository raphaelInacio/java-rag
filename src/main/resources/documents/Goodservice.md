# GodService

## Descrição Detalhada
O GodService é o coração da aplicação, centralizando funcionalidades comuns e oferecendo serviços essenciais para todos os outros microserviços. Ele atua como um ponto de entrada para muitas operações, como autenticação, geração de IDs, configuração centralizada e validação de dados.

## Responsabilidades

Gerenciar a autenticação de usuários e a emissão de tokens de acesso.
Definir os níveis de acesso e permissões para cada usuário.
Armazenar e fornecer configurações globais para a aplicação.
Gerar IDs únicos para entidades da aplicação.
Validar dados de entrada para garantir a integridade dos dados.
Centralizar a geração e o armazenamento de logs para toda a aplicação.
Monitorar a saúde da aplicação e coletar métricas.
## Tecnologias Utilizadas

Linguagem de programação: Go
Framework: Gin
Banco de dados: PostgreSQL
Ferramentas: Docker, Kubernetes, Prometheus, Grafana
## APIs

API 1: Autenticação
Descrição: Autentica um usuário e retorna um token de acesso.
Método: POST
URL: /auth/login
Request:
JSON
{
"username": "joao",
"password": "minhaSenha"
}
Use o código com cuidado.

Response:
JSON
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJ1c2VybmFtZSI6ImpvYW8iLCJpYXQiOjE1MTYyMzkwMjJ9.4fg83ec89g9b989fg345g45"
}
Use o código com cuidado.

API 2: Verificação de Token
Descrição: Verifica a validade de um token de acesso.
Método: GET
URL: /auth/verify
Headers: Authorization: Bearer {token}
Response:
JSON
{
"valid": true,
"user": {
"id": 123,
"username": "joao"
}
}
Use o código com cuidado.

## Dependências

Nenhuma (o GodService é um serviço central e não depende de outros)


## Registro de Decisão de Arquitetura: Microserviço com Múltiplas Responsabilidades
Contexto:

Nosso sistema monolítico legado está passando por um processo de migração para uma arquitetura de microserviços. Um dos desafios encontrados nesse processo é a decomposição de funcionalidades complexas em serviços menores e mais coesos. Neste registro, discutiremos a decisão de criar um microserviço com múltiplas responsabilidades, mesmo reconhecendo que isso pode violar princípios de design de microserviços.

Decisão:

### Decidimos criar um microserviço com múltiplas responsabilidades, denominado GodService, para agrupar todas as funcionalidades do legado:

### Justificativa:

### Processo de Migração: A decomposição completa do monólito em microserviços altamente coesos exigiria um esforço significativo de refatoração e poderia impactar a estabilidade do sistema em produção.
Complexidade das Funcionalidades: Algumas funcionalidades estão intrinsecamente ligadas e compartilham um contexto de domínio comum. Separar essas funcionalidades em microserviços distintos poderia aumentar a complexidade da comunicação entre serviços e introduzir novas fontes de latência.
Dependências: Existem dependências fortes entre as funcionalidades agrupadas no microserviço. Separar essas funcionalidades em microserviços distintos aumentaria o acoplamento entre os serviços e dificultaria a manutenção.
Tempo de Mercado: A necessidade de entregar novas funcionalidades rapidamente impulsiona a criação de um microserviço mais abrangente, permitindo que equipe de desenvolvimento trabalhe de forma mais ágil e independente.
Considerações:

### Antipadrão: Reconhecemos que a criação de um microserviço com múltiplas responsabilidades viola o princípio da responsabilidade única (Single Responsibility Principle) e pode levar a problemas de manutenção, escalabilidade e testes no futuro.
Decomposição Gradual: Essa decisão deve ser vista como uma etapa intermediária no processo de migração. À medida que o sistema evoluir e a equipe ganhar mais experiência com a arquitetura de microserviços, planejamos refatorar esse microserviço para torná-lo mais coeso.
Monitoramento: Implementaremos um sistema de monitoramento robusto para identificar gargalos de desempenho e problemas de escalabilidade nesse microserviço.
Limites Contextuais: Definiremos limites contextuais claros para esse microserviço, identificando quais funcionalidades pertencem ao seu escopo e quais devem ser migradas para outros microserviços no futuro.
Mitigação de Riscos:

### Testes Unitários e de Integração: Investiremos em testes unitários e de integração abrangentes para garantir a qualidade do código e detectar problemas precocemente.
Documentação: Manteremos uma documentação clara e detalhada sobre o design e as responsabilidades do microserviço.
Comunicação: Comunicaremos essa decisão para toda a equipe de desenvolvimento e manteremos a equipe informada sobre os planos de refatoração.
Conclusão:

A criação de um microserviço com múltiplas responsabilidades é uma decisão estratégica, tomada com base nas necessidades do projeto e nas restrições de tempo. Embora essa decisão viole alguns princípios de design de microserviços, ela permite que avancemos com a migração para uma arquitetura de microserviços de forma mais rápida e com menor risco. No futuro, planejamos refatorar esse microserviço para torná-lo mais coeso e alinhado com os princípios de design de microserviços.

Observações:

Personalize: Adapte este registro para a sua situação específica, incluindo os nomes dos microserviços, funcionalidades e justificativas.
Detalhe: Seja o mais específico possível ao descrever as funcionalidades e as razões para agrupá-las em um único microserviço.
Mitigação: Proponha medidas concretas para mitigar os riscos associados a essa decisão.
Revisão: Revise este registro periodicamente para acompanhar o progresso da migração e avaliar se a decisão continua sendo a mais adequada.
