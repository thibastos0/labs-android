# Prova Android - Firebase

Este projeto é um aplicativo Android desenvolvido em Java que integra o **Firebase** para gerenciamento de autenticação e dados. Atualmente, o projeto está focado em um sistema de login, cadastro de usuários e visualização de notas.

> **Status:** 🚧 Em construção

## 📱 Telas do Aplicativo

*   **Login:** Interface para autenticação de usuários existentes.
*   **Cadastro:** Tela para registro de novos usuários no sistema.
*   **Principal (Main):** Dashboard com opções para acessar as notas, gerenciar cadastros e logout.
*   **Notas (Grades):** Tela destinada à exibição de informações de desempenho.

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java
*   **IDE:** Android Studio
*   **Banco de Dados/Autenticação:** Firebase (em implementação)
*   **UI/UX:** Material Design, Toolbar customizada, suporte a Dark Mode.
*   **Bibliotecas:**
    *   `androidx.appcompat:appcompat`
    *   `com.google.android.material:material`
    *   `androidx.constraintlayout:constraintlayout`

## 🚀 Como Executar

1.  Clone o repositório.
2.  Abra o projeto no **Android Studio**.
3.  Certifique-se de configurar o arquivo `google-services.json` (necessário para a integração com Firebase).
4.  Sincronize o Gradle.
5.  Execute o aplicativo em um emulador ou dispositivo físico.

## 📝 Próximos Passos

- [ ] Finalizar a integração com o Firebase Authentication.
- [ ] Implementar a persistência de notas no Firebase Realtime Database ou Firestore.
- [ ] Refinar o design e a experiência do usuário.
