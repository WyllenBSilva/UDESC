# SISTEMA DE BIBLIOTECA - USANDO SQL
## Trabalho de Banco de Dados 2 na Udesc de Joinville.

Foi proposto a criação de um sistema de biblioteca para gerenciar empréstimos de exemplares de livros a usuários.

O Esquema conceitual do Banco está como abaixo:

![Part1 - Esquema conceitual Corrigido](https://github.com/WyllenBSilva/UDESC/assets/74624671/574dbf48-0765-4574-9067-e73581b9d413)

# Requisitos do Sistema de Controle de Biblioteca

Considere os seguintes requisitos para um sistema de controle de biblioteca:

- **Cadastro de Bibliotecários e Assistentes:**
  - O sistema deve permitir o cadastro dos bibliotecários e seus assistentes da biblioteca. 
  - Cada assistente é supervisionado por um ou mais bibliotecários.

- **Cadastro de Usuários:**
  - O sistema deve permitir o cadastro dos usuários, incluindo seu nome, endereço, telefones de contato e categoria. 

- **Categorias de Usuários e Tempos de Empréstimo:**
  - Alunos de graduação: 15 dias
  - Alunos de pós-graduação: 1 mês
  - Professores: 1 mês
  - Professores pós-graduação: 3 meses

- **Cadastro de Livros:**
  - O sistema deve permitir o cadastro dos livros, incluindo título, autores, ISBN, editora, qual coleção ele pertence, etc.

- **Cadastro de Exemplares:**
  - O sistema deve permitir o cadastro dos exemplares de cada livro, onde cada exemplar tem um número único de identificação.

- **Empréstimo de Exemplares:**
  - O sistema deve permitir o empréstimo dos exemplares para os usuários.
  - O empréstimo só é autorizado se o usuário não tiver livros atrasados, multas pendentes e não exceder o número de livros que tem direito.

- **Localização de Exemplares:**
  - O sistema deve localizar qual usuário está com o exemplar de um determinado livro.

- **Restrições de Empréstimo:**
  - O sistema não pode permitir o empréstimo de um exemplar da coleção de reserva.
  - O sistema não pode cobrar multa de professores em tempo integral.

- **Reserva de Exemplares:**
  - O sistema deve permitir a reserva de um exemplar emprestado para outro usuário.

- **Renovação de Empréstimo:**
  - O sistema deve permitir a renovação do empréstimo por até 3 vezes.

