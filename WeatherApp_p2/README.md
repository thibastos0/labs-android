# WeatherApp_p2

WeatherApp_p2 é um aplicativo Android de previsão do tempo que permite aos usuários visualizar condições meteorológicas atuais, autenticar-se via Firebase e gerenciar uma lista de cidades favoritas (em desenvolvimento).

## 🚀 Funcionalidades Implementadas

### 1. Autenticação (Login e Cadastro)
- Integração com **Firebase Authentication**.
- Suporte para login convencional e **Google Sign-In** (via Credential Manager).
- Interface de Login e Registro personalizada.

### 2. Tela Principal (Home)
- Visualização do clima atual.
- Integração com a **OpenWeatherMap API** (via OkHttp).
- Obtenção da localização atual do usuário através do **Google Play Services Location**.

### 3. Gerenciamento de Perfil
- Tela dedicada para exibição e edição de informações do usuário logado.

### 4. Persistência de Dados
- Configuração inicial do **Room Database** para armazenamento local de cidades e preferências.

## 🛠 Tecnologias Utilizadas

- **Linguagem:** Java / Kotlin (Build Scripts).
- **Arquitetura:** Baseada em Activities e DAOs para persistência.
- **Bibliotecas Principais:**
  - **Firebase:** Auth e Realtime Database.
  - **Room:** Para persistência local.
  - **OkHttp:** Para requisições HTTP à API de clima.
  - **Google Play Services Location:** Para geolocalização.
  - **Credential Manager:** Para login simplificado com Google.
  - **Material Design:** Componentes de interface moderna.

## 🏗 Estrutura do Projeto

- `LoginActivity`, `MainActivity`, `HomeActivity`: Fluxo principal de navegação.
- `database/`, `dao/`, `model/`: Camadas de persistência local para favoritos e cache.
- `res/layout/`: Definições de interface XML.

## 🚧 O que falta fazer (Próximos Passos)

- [ ] **Lógica de Favoritos:** Implementar a funcionalidade completa de adicionar/remover cidades aos favoritos usando Room.
- [ ] **Integração Home-Favoritos:** Refinar a transição entre a lista de favoritos e a exibição do clima na Home.
- [ ] **Ajustes de UI/UX:** Polimento visual nas telas de clima e transições.
- [ ] **Tratamento de Erros:** Melhorar o feedback ao usuário em caso de falha na API ou falta de conexão.

## ⚙️ Configuração Necessária

Para rodar o projeto localmente, é necessário:
1. Um arquivo `local.properties` na raiz do projeto contendo sua chave da API:
   ```properties
   MINHA_API_KEY=sua_chave_aqui
   ```
2. O arquivo `google-services.json` configurado no Firebase Console e colocado na pasta `app/`.

---
*Este projeto é parte de um desenvolvimento contínuo.*
