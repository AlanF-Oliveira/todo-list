# Especificação — Refatoração Clean Code (K2-T1)

## 1. Objetivo

Melhorar a clareza, manutenção e testabilidade do backend Java da TODO-List,
aplicando os sete princípios de Clean Code definidos no enunciado da atividade:

1. Nomes autoexplicativos.
2. Regra do Escoteiro.
3. Funções pequenas.
4. Don't Repeat Yourself (DRY).
5. Comentários somente quando estritamente necessários.
6. Tratamento de erros.
7. Testagem.

Esta especificação orienta uma refatoração estrutural. Ela não autoriza novas
funcionalidades nem mudanças de regra de negócio.

## 2. Escopo

### Incluído

- Backend Java em `src/main/java/org/alan/todolist/`.
- Testes Spock em `src/test/groovy/`.
- Documentação diretamente relacionada a esta refatoração.

### Fora do escopo

- Qualquer arquivo em `frontend/`.
- Integração entre backend e frontend.
- Persistência além do armazenamento em memória existente.
- Alterações em dependências, IDE, arquivos gerados ou configuração de frontend.
- Novas funcionalidades ou correções não relacionadas aos sete princípios.

## 3. Comportamentos que devem ser preservados

A refatoração deve manter:

- criação, listagem, filtragem, atualização e exclusão de tarefas;
- exclusão de todas as tarefas;
- ordenação por prioridade realizada pelo serviço;
- prioridades válidas de 1 a 5;
- status `TODO`, `DOING` e `DONE`;
- preservação do ID da tarefa durante atualização;
- alarmes pendentes e a regra de que tarefas `DONE` não disparam alarmes;
- fluxo do menu de terminal;
- resultados, mensagens e exceções atuais, salvo decisão aprovada conforme a
  seção 5.

## 4. Princípios e oportunidades de melhoria

### 4.1 Nomes autoexplicativos

**Problemas observados**

- `delelteAllTodo` contém erro ortográfico e não comunica corretamente a operação.
- Abreviações como `sc` e `dtf`, além de nomes genéricos como `result`, reduzem a
  clareza em classes com responsabilidades diferentes.

**Diretriz**

Melhorar nomes somente quando o nome atual prejudicar a compreensão da
responsabilidade ou do fluxo. Renomeações de métodos públicos devem preservar
compatibilidade ou ser aprovadas previamente, com testes correspondentes. Não
renomear apenas por preferência pessoal.

### 4.2 Regra do Escoteiro

**Problemas observados**

- Há inconsistências locais de espaçamento e encapsulamento, como `lista=`,
  espaços duplicados e o campo `dtf` sem `private`.
- Existem comentários de código obsoleto no repositório e nos testes.

**Diretriz**

Ao alterar um arquivo do backend, deixá-lo ligeiramente mais claro e consistente,
corrigindo apenas problemas locais diretamente relacionados ao trecho alterado.
Não usar a Regra do Escoteiro como justificativa para reescritas amplas ou
alterações funcionais incidentais.

### 4.3 Funções pequenas

**Problemas observados**

- `Menu.start` concentra ciclo de vida, entrada, seleção de opção e navegação.
- `Menu.createTodo` e `Menu.updateTodo` concentram coleta, validação e criação de
  entidades.
- `TodoService` repete estruturas de filtragem.

**Diretriz**

Extrair funções coesas, com uma responsabilidade clara e nomes que expressem a
intenção. A extração não deve mudar a ordem das operações, as mensagens ou os
resultados observáveis.

### 4.4 Don't Repeat Yourself (DRY)

**Problemas observados**

- A criação e a atualização no menu repetem coleta e validação de campos.
- Os filtros por categoria, prioridade e status repetem a mesma estrutura.

**Diretriz**

Extrair somente duplicação real, estável e coesa. Não criar abstrações genéricas
apenas para reduzir quantidade de linhas. Regras de negócio devem permanecer em
uma única camada responsável, sem duplicação entre menu, serviço e repositório.

### 4.5 Comentários somente quando estritamente necessários

**Problemas observados**

- Há código comentado sem função ativa em `TodoRepository.listAll`.
- Há expectativas comentadas em `TodoServiceSpec`.

**Diretriz**

Remover comentários obsoletos e não adicionar comentários que apenas descrevam o
que o código já expressa. Comentários novos são permitidos somente para explicar
uma decisão, restrição ou regra que não possa ser comunicada adequadamente por
nomes e estrutura.

### 4.6 Tratamento de erros

**Problemas observados**

- Filtros sem resultado usam `ArrayIndexOutOfBoundsException`.
- O repositório usa mensagens genéricas para IDs inexistentes e
  `NullPointerException` para argumento nulo.
- O menu não possui estratégia uniforme para entradas inválidas de números, datas
  e status.

**Diretriz**

Tornar contratos de erro explícitos e consistentes, preservando o comportamento
atual até que uma mudança seja aprovada. Não substituir exceções, mensagens ou
resultados vazios por outra política apenas por preferência de estilo.

### 4.7 Testagem

**Problemas observados**

- A cobertura existente está concentrada em `TodoServiceSpec`.
- Não há testes específicos para `Todo`, `Alarm`, `TodoRepository` ou `Menu`.
- `checkPendingAlarms` usa o relógio do sistema diretamente.
- Faltam casos de borda para prioridades, datas, status `DONE`, alarmes, filtros,
  IDs inexistentes e atualização.

**Diretriz**

Caracterizar o comportamento atual antes de refatorar regras sensíveis e ampliar
os testes dos casos de borda identificados. Os testes devem verificar resultado,
estado, mensagens e exceções relevantes. A solução para tornar o relógio
testável deve ser aprovada antes de alterar a forma de fornecimento do horário.

## 4.8 Práticas complementares

Formatação consistente, encapsulamento adequado e organização coerente das
estruturas permanecem como práticas de apoio. Elas devem melhorar a legibilidade e
as fronteiras entre modelo, serviço, repositório e menu, mas não substituem os sete
princípios nem justificam mudanças apenas estéticas ou de arquitetura.

Alterações de visibilidade, mutabilidade, dependências ou organização de pacotes
devem ser pequenas, justificadas pelo problema encontrado e acompanhadas de testes
quando puderem afetar o contrato.

## 5. Decisões que exigem aprovação prévia

As seguintes decisões podem alterar contratos ou regras de negócio e não fazem
parte de uma refatoração automática:

- retornar lista vazia ou lançar exceção quando um filtro não encontrar resultados;
- substituir os tipos ou mensagens das exceções atuais;
- política para prioridade nula ou fora do intervalo;
- estratégia de geração e isolamento de IDs;
- exposição mutável ou somente leitura da lista de alarmes;
- injeção de `TodoRepository` no serviço;
- correção do nome público `delelteAllTodo`, incluindo eventual compatibilidade
  com o nome antigo;
- forma de fornecer o horário ao serviço para testes determinísticos.

Cada decisão aprovada deve ser registrada nesta especificação antes da
implementação e acompanhada por testes de aceitação.

## 6. Critérios de aceite

1. `./gradlew test` termina com sucesso.
2. A refatoração não altera o resultado esperado das operações existentes.
3. Os testes cobrem os casos normais e os casos de borda identificados nesta
   especificação.
4. A ordenação por prioridade, os status, os IDs, os alarmes e o fluxo do menu
   permanecem preservados.
5. Tarefas `DONE` continuam sem disparar alarmes.
6. Não existem alterações de comportamento sem justificativa e aprovação
   registrada nesta especificação.
7. Toda alteração de mensagem, exceção, contrato público ou regra de negócio possui
   teste correspondente.
8. Não há código comentado obsoleto nem duplicação evitável introduzida pela
   refatoração.
9. Nenhum arquivo fora do backend Java, dos testes Spock e da documentação em
   escopo é alterado.
10. O diff final permite identificar os arquivos alterados, os testes executados e
    as decisões aprovadas.

## 7. Estratégia de execução

1. Executar a suíte existente e registrar o baseline.
2. Adicionar testes de caracterização e casos de borda sem mudar a implementação.
3. Aprovar e registrar decisões de contrato pendentes.
4. Refatorar funções e duplicações preservando os testes.
5. Aplicar a Regra do Escoteiro, nomes necessários e remoção de comentários
   obsoletos.
6. Ajustar tratamento de erros e encapsulamento somente conforme decisões
   aprovadas.
7. Executar `./gradlew test`, revisar o diff e verificar manualmente o menu Java.
