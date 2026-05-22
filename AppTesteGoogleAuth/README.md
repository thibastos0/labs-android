# AppTesteGoogleAuth 🛡️📍

Este projeto é um laboratório prático de desenvolvimento Android em **Java**, focado na implementação das APIs mais recentes de identidade e localização do Google.

## 🚀 Funcionalidades Principais

- **Autenticação de Identidade (Moderno):**
    - Implementação da **Credential Manager API**, a nova solução unificada do Android para autenticação.
    - Suporte a **Google ID Tokens** para integração segura com Firebase.
- **Histórico de Evolução (Legado):**
    - Contém referências comentadas ao antigo `GoogleSignInClient` (legado), servindo como guia de migração.
- **Geolocalização Precisa:**
    - Uso do `FusedLocationProviderClient` para capturar coordenadas.
    - Implementação de lógica para tratar cache (`getLastLocation`) e solicitações em tempo real (`getCurrentLocation`).
- **Mapas Open-Source:**
    - Integração com **osmdroid** (OpenStreetMap) como alternativa ao Google Maps.
- **Arquitetura e UI:**
    - UI modernizada com **Material Design 3**.
    - Uso de `EdgeToEdge` para uma experiência imersiva.
    - Layouts otimizados com `ConstraintLayout`.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java (JDK 17)
- **Firebase:** Auth, Analytics e BoM para gestão de versões.
- **Identity:** `androidx.credentials` e `googleid`.
- **Location:** `play-services-location`.
- **Mapas:** `osmdroid-android`.
- **SDK:** Compile/Target SDK 36.

## 📋 Configuração Obrigatória

Para que as funcionalidades de autenticação e localização funcionem:

1.  **Google Services:** O arquivo `google-services.json` deve estar em `/app`.
2.  **Firebase Console:**
    - Ativar provedores "Google" e "E-mail/Senha".
    - Configurar o **Web Client ID** nas opções de login do Google.
3.  **SHA-1:** Registrar o SHA-1 da sua chave de depuração (debug.keystore) no projeto do Firebase.
4.  **Permissões:** O app solicita `ACCESS_FINE_LOCATION` em tempo de execução.

## 📂 Estrutura de Telas

- **`LoginActivity`**: Fluxo de login moderno com Credential Manager.
- **`RegisterActivity`**: Cadastro de novos usuários via e-mail.
- **`GeolocActivity`**: Painel de teste para GPS e coordenadas.
- **`HomeActivity`**: Perfil do usuário logado e navegação.
- **`MainActivity`**: Atua como roteador (Splash/Logic de Login) e repositório de referências legadas.

---
*Desenvolvido como um guia técnico para transição de APIs no ecossistema Android.*
