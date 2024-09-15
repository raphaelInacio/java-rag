# 🤖 SAN-AI:  Software Architecture Navigation.

## Seu copiloto para arquiteturas de software.

Cansado de perder tempo procurando informações em documentos extensos? 
O SAN-AI utiliza inteligência artificial de ponta para transformar sua arquitetura em um mapa interativo, permitindo que você navegue e encontre as respostas que precisa com apenas um clique. 

Quer entender as decisões de design, identificar dependências ou visualizar o fluxo de dados? 

O SAN-AI é a sua ferramenta ideal. Simplifique sua vida, otimize seus projetos e entregue software de alta qualidade com o SAN-AI."



## Técnicas utillzadas

### Natural Language Querie
Natural Language Querie permitem iteragir com bases de dados utilizando linguagem natural, dentro SAN-IA estamos utilizando essa técnica para permitir que o usuário navegue pelos dados do Grafo utilizando linguagem natural

![SAN](/docs/san-ia-01.gif)

### Graph RAG 
Graph RAG permite aumentar o conhecimento da LLM adicionando dados internos ao conhecimento da LLM, dentro do SAN estamos utilizando Vector Database provido pelo Neo4J para armazenar as documentações arquiteturais da nossa arquitetura
![SAN](/docs/san-ia-02.gif)

## Modelos:
- gpt-3.5-turbo
- Gemini Flash 1.5

## Tecnologias
 - JAVA
 - Vertex
 - Neo4j
 - Lanchain 4j

## Váriaveis de Ambiente
- OPENAI_API_KEY
- PROJECT_ID
- LOCATION


## Rodando o projeto

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```
