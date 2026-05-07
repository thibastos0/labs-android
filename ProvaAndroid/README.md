# Prova Android - Cadastro e Cálculo de Notas

Este projeto é um aplicativo Android desenvolvido em Java que permite o cadastro de alunos e a realização de cálculos de notas/médias. O projeto demonstra o uso de persistência local de dados e navegação entre telas.

## 🚀 Funcionalidades

*   **Cadastro de Alunos:** Armazena RA, Nome e Email do aluno.
*   **Persistência de Dados:** Utiliza **SQLite** (via `SQLiteOpenHelper`) para salvar as informações localmente.
*   **Cálculo de Média:** Tela dedicada para inserção de notas e cálculo de desempenho.
*   **Navegação:** Interface simples com botões para acessar as diferentes funcionalidades do sistema.

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java
*   **Banco de Dados:** SQLite (nativo)
*   **Componentes:** Activity, Intent, DAO Pattern, Edge-to-Edge.

## 🌿 Branches Específicas

Este projeto possui uma branch alternativa onde a persistência de dados foi implementada utilizando a biblioteca **Room**, demonstrando uma abordagem mais moderna e reativa para o gerenciamento do banco de dados local.

## 📂 Estrutura do Projeto

*   `model/`: Contém a classe `Aluno`.
*   `dao/`: Contém a lógica de acesso aos dados (`AlunoDAO`).
*   `database/`: Gerencia a conexão com o banco SQLite (`DatabaseConnection`).
*   `MainActivity`: Tela principal de navegação.
*   `CadastroActivity`: Tela de formulário para novos alunos.
*   `CaluloNotaActivity`: Tela para lógica de médias.

## ⚙️ Como Executar

1.  Abra o projeto no **Android Studio**.
2.  Sincronize o Gradle.
3.  Execute no emulador ou dispositivo físico com API 24 ou superior.
