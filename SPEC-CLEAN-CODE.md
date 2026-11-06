# Spec: oportunidades de melhoria com Clean Code

## Status

Proposta para a próxima etapa de refatoração. Esta especificação não altera o
comportamento da aplicação nem autoriza a implementação de funcionalidades novas.

## Objetivo

Reduzir complexidade, duplicação e ambiguidade no backend Java, no menu de terminal
e nos testes Spock, preservando as funcionalidades existentes descritas em
[AGENTS.md](./AGENTS.md).

Os sete princípios abaixo são a base desta atividade: nomes autoexplicativos, Regra
do Escoteiro, funções pequenas, DRY, comentários somente quando estritamente
necessários, tratamento de erros e testes.

## Escopo

Inclui:

- `src/main/java/org/alan/todolist/`
- `src/test/groovy/`
- documentação diretamente relacionada à refatoração do backend

Não inclui:

- novas funcionalidades;
- qualquer alteração em `frontend/`;
- integração entre backend e frontend;
- troca da persistência em memória;
- mudança deliberada de mensagens, regras de prioridade/status ou alarmes;
- alteração de dependências sem uma especificação própria;
- alterações em arquivos gerados, IDE ou no arquivo não relacionado já existente no
  worktree.

## Diagnóstico e oportunidades

### 1. Nomes autoexplicativos

**Evidências**

- `TodoService` possui o método `delelteAllTodo`, com erro ortográfico
  ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):68).
- Há nomes genéricos ou abreviados como `list`, `sc`, `dtf`, `result`, `todo` e
  `dados` em classes que concentram regras distintas.
- O código alterna português e inglês em nomes de domínio e mensagens.

**Oportunidade**

Padronizar nomes de métodos, atributos e variáveis para que expressem intenção e
sejam consistentes dentro de cada camada. Renomeações públicas devem preservar
compatibilidade ou ser acompanhadas de uma decisão explícita e testes de contrato.

### 2. Regra do Escoteiro

**Evidências**

- Há inconsistências locais de espaçamento, visibilidade e nomenclatura, como
  `List<Todo> lista=`, `return  new ArrayList<>()` e o campo `dtf` sem
  encapsulamento ([TodoRepository.java](./src/main/java/org/alan/todolist/repository/TodoRepository.java):22;
  [Menu.java](./src/main/java/org/alan/todolist/terminal/Menu.java):15).
- Existem comentários obsoletos no repositório e nos testes.
- `Todo` expõe diretamente a lista mutável de alarmes e gera IDs por estado
  estático global ([Todo.java](./src/main/java/org/alan/todolist/model/Todo.java):12).
- O serviço cria diretamente sua implementação concreta de repositório
  ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):14).

**Oportunidade**

Ao tocar em um arquivo do backend, deixá-lo ligeiramente melhor sem ampliar o
escopo: corrigir inconsistências locais, remover código comentado obsoleto e
melhorar clareza. Não fazer reescritas amplas nem incluir mudanças funcionais
incidentais.

### 3. Funções pequenas

**Evidências**

- `Menu.start` coordena ciclo de vida, entrada, seleção de opção e navegação
  ([Menu.java](./src/main/java/org/alan/todolist/terminal/Menu.java):16).
- `Menu.createTodo` e `Menu.updateTodo` repetem coleta e validação de quase todos os
  campos ([Menu.java](./src/main/java/org/alan/todolist/terminal/Menu.java):78).
- `TodoService` repete a mesma estrutura de filtragem em categoria, prioridade e
  status ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):25).
**Oportunidade**

Extrair funções coesas para entrada, validação, criação/atualização e filtros. A
decomposição deve manter o fluxo observável e não duplicar regras de domínio entre
o menu e o serviço.

### 4. Don't Repeat Yourself (DRY)

**Evidências**

- `Menu.createTodo` e `Menu.updateTodo` repetem coleta e validação de quase todos os
  campos ([Menu.java](./src/main/java/org/alan/todolist/terminal/Menu.java):78).
- `TodoService` repete a mesma estrutura de filtragem em categoria, prioridade e
  status ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):25).

**Oportunidade**

Extrair lógica compartilhada somente quando houver duplicação real e coesa. A
extração deve preservar mensagens, ordenação, exceções e demais contratos aprovados.

### 5. Comentários somente quando estritamente necessários

**Evidências**

- Existe código comentado sem função ativa em `TodoRepository.listAll`
  ([TodoRepository.java](./src/main/java/org/alan/todolist/repository/TodoRepository.java):18).
- Há expectativas comentadas nos testes, como `//thrown(...)`
  ([TodoServiceSpec.groovy](./src/test/groovy/org/alan/todolist/service/TodoServiceSpec.groovy):57).
- Alguns efeitos colaterais, como mensagens impressas, não estão claros pelos nomes
  atuais.

**Oportunidade**

Remover comentários obsoletos e evitar comentários que apenas repitam o código.
Adicionar comentário somente para registrar uma decisão ou regra que não possa ser
expressa claramente por nomes e estrutura. Qualquer mensagem removida ou alterada
exige decisão de compatibilidade.

### 6. Tratamento de erros

**Evidências**

- Ausência de resultados em filtros lança `ArrayIndexOutOfBoundsException`
  ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):35).
- `TodoRepository` lança mensagens genéricas para IDs inexistentes e
  `NullPointerException` para argumento nulo ([TodoRepository.java](./src/main/java/org/alan/todolist/repository/TodoRepository.java):28).
- O menu usa `nextInt`, `valueOf` e `parse` sem uma estratégia uniforme para entradas
  inválidas ([Menu.java](./src/main/java/org/alan/todolist/terminal/Menu.java):36).

**Oportunidade**

Estabelecer contratos de erro explícitos, tipos de exceção adequados e mensagens
consistentes. A mudança não deve transformar uma lista vazia em erro, nem o inverso,
sem decisão documentada, pois isso altera o comportamento atual.

### 7. Testagem

**Evidências**

- O teste existente concentra-se em `TodoServiceSpec` e cobre principalmente fluxos
  básicos de serviço.
- Não há testes automatizados para `Todo`, `Alarm`, `TodoRepository` ou `Menu`.
- `checkPendingAlarms` usa `LocalDateTime.now()` diretamente, tornando o teste
  dependente do relógio ([TodoService.java](./src/main/java/org/alan/todolist/service/TodoService.java):77).
- Há casos sem assertivas de borda para prioridade, datas, alarmes concluídos,
  IDs inexistentes e entradas inválidas.

**Oportunidade**

Expandir testes antes ou junto de cada refatoração, priorizando comportamento de
alarme, ordenação por prioridade, atualização, erros e isolamento de estado.

## Requisitos de aceitação da refatoração

1. `./gradlew test` continua passando.
2. A refatoração não altera o resultado esperado das operações existentes.
3. Todas as funcionalidades atuais do menu Java continuam disponíveis.
4. Os testes validam também os casos de borda identificados durante a análise,
   incluindo alarmes, prioridades, filtros, atualização, exclusão e IDs inexistentes.
5. A ordenação por prioridade do backend, os status existentes e a regra de que
   tarefas `DONE` não disparam alarmes permanecem inalterados.
6. IDs e dados de alarmes não são perdidos durante atualizações.
7. Não existem alterações de comportamento sem justificativa e aprovação registrada
   na especificação.
8. Cada mudança de contrato, exceção, mensagem ou persistência é registrada como
   decisão explícita antes da implementação.
9. A refatoração não introduz código comentado obsoleto, duplicação evitável ou
   funções que combinem entrada, regra de negócio e apresentação sem justificativa.
10. A especificação de cada incremento deve indicar arquivos afetados, comportamento
   preservado, testes adicionados/ajustados e critério de rollback.

## Ordem sugerida de execução

1. Caracterizar o comportamento atual com testes de borda no serviço e repositório.
2. Extrair duplicações e funções longas no serviço e no menu, sem alterar contratos.
3. Reforçar encapsulamento da entidade e tornar o tratamento de erros explícito,
   após aprovar os contratos resultantes.
4. Aplicar limpeza de nomes, comentários e formatação no backend e nos testes.
5. Executar os testes e uma verificação manual do menu Java.

## Fora de decisão nesta spec

Ainda não está definido se as exceções atuais devem ser substituídas por exceções
de domínio, se o serviço deve receber o repositório por injeção, ou se a lista de
alarmes deve ser imutável. Essas decisões exigem uma especificação própria antes da
implementação.
