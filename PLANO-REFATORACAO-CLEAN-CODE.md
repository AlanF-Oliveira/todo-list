# Plano de refatoração Clean Code — backend Java

## 1. Objetivo

Refatorar exclusivamente o backend Java e seus testes Spock, aplicando os sete
princípios definidos em [SPEC-CLEAN-CODE.md](./SPEC-CLEAN-CODE.md): nomes
autoexplicativos, Regra do Escoteiro, funções pequenas, DRY, comentários somente
quando estritamente necessários, tratamento de erros e testagem. Não introduzir
funcionalidades nem alterar o comportamento existente sem decisão registrada.

Arquivos em escopo:

- `src/main/java/org/alan/todolist/`
- `src/test/groovy/`
- documentação diretamente relacionada ao backend

Arquivos fora do escopo:

- `frontend/`
- arquivos gerados, `.gradle/`, `build/` e `bin/`
- configurações da IDE
- o arquivo não relacionado já existente no worktree

## 2. Princípios de execução

1. Trabalhar em incrementos pequenos e verificáveis.
2. Escrever ou ajustar testes antes de alterar uma regra sensível.
3. Preservar contratos observáveis: funcionalidades do menu, ordenação por
   prioridade, status, IDs, alarmes, mensagens e exceções, salvo decisão aprovada.
4. Executar `./gradlew test` ao final de cada etapa que alterar Java ou testes.
5. Não misturar limpeza mecânica com mudança de comportamento.
6. Registrar arquivos alterados, testes executados e decisões em cada incremento.
7. Interromper a implementação quando uma decisão de contrato ainda estiver aberta.

## 3. Fases do plano

### Fase 0 — Baseline e caracterização

**Objetivo:** congelar o comportamento atual antes da refatoração.

**Atividades:**

- Executar `./gradlew test`.
- Revisar e ampliar `TodoServiceSpec` para cobrir o comportamento atual de:
  - ordenação por prioridade;
  - atualização preservando o ID;
  - atualização com alarmes;
  - alarmes para tarefas `TODO`, `DOING` e `DONE`;
  - alarmes antes, no momento e depois do prazo;
  - exclusão por ID existente e inexistente;
  - exclusão total;
  - filtros com e sem resultados;
  - prioridade válida e inválida.
- Criar testes específicos para `TodoRepository` e `Todo`, se necessário para
  caracterizar contratos que não estejam cobertos pelo serviço.
- Registrar mensagens e tipos de exceção observados, sem corrigi-los nesta fase.

**Arquivos prováveis:**

- `src/test/groovy/org/alan/todolist/service/TodoServiceSpec.groovy`
- novos testes em `src/test/groovy/org/alan/todolist/`

**Critério de saída:**

- `./gradlew test` passa.
- Os testes descrevem o comportamento que deverá ser preservado.
- Nenhuma decisão de design é implementada nesta fase.

### Fase 1 — Decisões de contrato

**Objetivo:** resolver as decisões que podem alterar comportamento.

Antes de editar a implementação, definir e registrar:

- Se filtros sem resultado continuam lançando exceção ou passam a retornar lista
  vazia.
- Quais exceções representam ID inexistente e argumento nulo.
- Se a validação de prioridade deve rejeitar com exceção, manter o valor atual ou
  aceitar outra política.
- Como IDs serão gerados sem perder a identidade atual nem o isolamento dos testes.
- Se a lista de alarmes será exposta como cópia ou coleção somente leitura.
- Se `TodoService` receberá `TodoRepository` por injeção ou continuará criando sua
  dependência.
- Se a correção do nome público `delelteAllTodo` terá compatibilidade por método
  legado ou alteração de contrato.
- Como o relógio será fornecido para tornar alarmes determinísticos em testes.

**Entregável:**

- Atualização da especificação ou um registro de decisão vinculado a esta fase.

**Critério de saída:**

- Todas as decisões que alteram contratos estão aprovadas.
- Cada decisão possui testes de aceitação definidos.

### Fase 2 — Extração de responsabilidades

**Objetivo:** reduzir funções longas e duplicação sem alterar contratos.

**Atividades:**

- Decompor `Menu.start` em seleção de opção e handlers de operação.
- Extrair da entrada do menu a leitura/validação de data, prioridade, categoria e
  status.
- Unificar a coleta repetida entre criação e atualização sem misturar apresentação
  com regra de negócio.
- Extrair a lógica comum dos filtros de `TodoService`, preservando mensagens,
  ordenação e exceções aprovadas.
- Manter `TodoRepository` como armazenamento em memória, sem introduzir nova
  persistência.

**Arquivos prováveis:**

- `src/main/java/org/alan/todolist/terminal/Menu.java`
- `src/main/java/org/alan/todolist/service/TodoService.java`
- testes correspondentes

**Critério de saída:**

- Fluxos de criação, listagem, filtros, atualização, exclusão e alarmes continuam
  cobertos e aprovados.
- `./gradlew test` passa.

### Fase 3 — Modelo, encapsulamento e erros

**Objetivo:** tornar responsabilidades e falhas explícitas, conforme as decisões da
Fase 1.

**Atividades:**

- Ajustar a fronteira mutável de `Todo.alarms`.
- Centralizar ou tornar explícita a validação de prioridade.
- Aplicar a política aprovada para IDs.
- Aplicar tipos de exceção e mensagens aprovados.
- Tornar o relógio injetável ou fornecer uma abstração equivalente, sem mudar a
  regra de que tarefas `DONE` não disparam alarmes.
- Avaliar injeção de `TodoRepository` apenas se aprovada.

**Arquivos prováveis:**

- `src/main/java/org/alan/todolist/model/Todo.java`
- `src/main/java/org/alan/todolist/model/Alarm.java`
- `src/main/java/org/alan/todolist/service/TodoService.java`
- `src/main/java/org/alan/todolist/repository/TodoRepository.java`
- testes de modelo, serviço e repositório

**Critério de saída:**

- IDs e alarmes são preservados em atualização.
- A ordenação, os status e a regra de alarmes permanecem iguais.
- Os testes de borda passam sem depender do horário real.
- `./gradlew test` passa.

### Fase 4 — Nomes, comentários e formatação

**Objetivo:** aplicar a limpeza mecânica depois que a estrutura estiver estável.

**Atividades:**

- Corrigir nomes ambíguos, abreviações e erros ortográficos.
- Tratar a compatibilidade do método `delelteAllTodo` conforme decisão aprovada.
- Remover comentários obsoletos e expectativas comentadas.
- Uniformizar visibilidade, espaçamento, declarações e convenções Java/Groovy.
- Não introduzir formatter, linter ou dependência nova nesta fase.

**Arquivos prováveis:**

- todos os arquivos Java em `src/main/java/`
- testes em `src/test/groovy/`

**Critério de saída:**

- Não há código comentado obsoleto no escopo.
- Os nomes refletem responsabilidades.
- Não há alteração funcional não documentada.
- `./gradlew test` passa.

### Fase 5 — Validação final e documentação

**Objetivo:** confirmar a preservação do sistema e encerrar a atividade.

**Atividades:**

- Executar `./gradlew test`.
- Executar a aplicação com `./gradlew run` e verificar manualmente o menu:
  criação, listagem, filtros, atualização, exclusão, exclusão total, saída e
  exibição de alarmes.
- Revisar o diff para confirmar que somente backend, testes e documentação
  relacionada foram alterados.
- Atualizar `SPEC-CLEAN-CODE.md` com decisões tomadas, se necessário.
- Registrar limitações e comportamentos preservados.

**Critérios de conclusão:**

- Testes automatizados aprovados.
- Verificação manual do menu concluída.
- Nenhum arquivo de frontend ou gerado foi alterado.
- Todos os requisitos de aceitação da especificação foram atendidos.

## 4. Critérios de aceite

1. A refatoração não altera o resultado esperado das operações existentes.
2. Todas as funcionalidades atuais do menu Java continuam disponíveis.
3. A ordenação por prioridade, os status, os IDs e a regra de alarmes para tarefas
   `DONE` permanecem inalterados.
4. Os testes validam os casos normais e os casos de borda identificados durante a
   análise, incluindo alarmes, prioridades, filtros, atualização, exclusão e IDs
   inexistentes.
5. `./gradlew test` passa ao final de cada incremento e na conclusão do plano.
6. Não existem alterações de comportamento sem justificativa e aprovação registrada
   na especificação.
7. Mensagens, tipos de exceção e contratos públicos só mudam quando houver decisão
   explícita e testes correspondentes.
8. Nenhum arquivo de `frontend/`, gerado ou fora do escopo é alterado.

## 5. Dependências entre fases

```text
Fase 0 → Fase 1 → Fase 2 → Fase 3 → Fase 4 → Fase 5
```

- A Fase 1 depende da caracterização da Fase 0.
- A Fase 2 depende dos contratos aprovados na Fase 1.
- A Fase 3 depende dos testes de caracterização e das decisões de contrato.
- A Fase 4 depende da estabilização estrutural das Fases 2 e 3.
- A Fase 5 depende da conclusão de todas as alterações e testes.

Não executar fases posteriores com testes de caracterização ausentes ou decisões de
contrato pendentes.

## 6. Riscos e controles

| Risco | Controle |
|---|---|
| Alterar mensagens ou exceções por engano | Caracterizar e testar contratos antes da refatoração |
| Perder IDs durante atualização | Teste explícito de identidade antes e depois do update |
| Perder alarmes durante atualização | Teste de preservação da coleção de alarmes |
| Mudar ordenação por prioridade | Teste com prioridades variadas e iguais |
| Tornar alarmes dependentes do relógio | Fornecer tempo controlável nos testes |
| Quebrar o menu interativo | Verificação manual na Fase 5 |
| Expandir o escopo para frontend | Revisão do diff e restrição de arquivos |
| Esconder uma mudança funcional em limpeza de nomes | Separar fases e registrar decisões |

## 7. Resultado esperado

Ao final, o backend Java deverá manter as mesmas capacidades e contratos aprovados,
mas apresentar funções mais coesas, nomes mais claros, menor duplicação, limites de
mutabilidade definidos, erros explícitos e testes suficientes para proteger as regras
de tarefas, prioridades, status, atualização e alarmes.
