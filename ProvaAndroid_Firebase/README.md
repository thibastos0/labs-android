# Prova Android - Firebase 🚀

Este projeto é uma aplicação Android desenvolvida em **Java** que integra diversos serviços do **Firebase** (Authentication, Realtime Database, Firestore) para gerenciar um sistema acadêmico simplificado de notas e usuários.

## 📱 Funcionalidades

- **Splash Screen:** Tela de abertura personalizada com branding do app.
- **Sistema de Autenticação Híbrido:**
    - Login via **E-mail/Senha** com Firebase Auth.
    - Login via **Google Sign-In** (integração legada e preparo para Credential Manager).
- **Gestão de Usuários:** Tela de cadastro para novos alunos/usuários.
- **Dashboard Principal:** Central de navegação para as funcionalidades do sistema.
- **Visualização de Notas:** Módulo destinado à exibição do desempenho acadêmico (Grades).
- **Logout Seguro:** Encerramento de sessão tanto no Firebase quanto no cliente Google.

## 🛠️ Tecnologias e Bibliotecas

- **Linguagem:** Java (JDK 11+)
- **Android SDK:** Compile/Target SDK 36 (Android 15+)
- **Firebase Services:**
    - `firebase-auth`: Autenticação segura.
    - `firebase-database`: Persistência de dados em tempo real.
    - `firebase-firestore`: Banco de dados NoSQL flexível.
    - `firebase-analytics`: Monitoramento de uso do app.
- **Google Play Services:** Autenticação unificada via Google.
- **UI Components:**
    - Material Design 3.
    - Toolbars customizadas com navegação `up`.
    - Edge-to-Edge para melhor aproveitamento de tela.

## ⚙️ Requisitos de Configuração

1.  **Firebase Console:**
    - Registre o pacote `com.example.provaandroid_firebase`.
    - Adicione o arquivo `google-services.json` na pasta `app/`.
    - Habilite os provedores de Login (Google e E-mail) no menu Authentication.
    - Configure as regras do Realtime Database e Firestore conforme necessário.
2.  **Credenciais de Debug:** Certifique-se de adicionar o certificado SHA-1 da sua máquina no console do Firebase para que o login do Google funcione corretamente.

## 📂 Estrutura do Projeto

- `SplashActivity`: Gerencia a exibição da tela de entrada.
- `LoginActivity`: Interface principal para entrada de usuários.
- `RegisterActivity`: Cadastro de novos perfis.
- `MainActivity`: Dashboard de controle após o login.
- `GradesActivity`: Visualização detalhada de notas e frequências.

---
*Projeto desenvolvido como parte de avaliação acadêmica focada em integração mobile com serviços em nuvem.*
