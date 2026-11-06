# Plano SDD — Refatoração Clean Code do backend Java

## Referência oficial

Este plano deve ser executado conforme [docs/spec.md](./spec.md). O escopo está
limitado a `src/main/java/org/alan/todolist/`, `src/test/groovy/` e documentação
relacionada. Nenhum arquivo de `frontend/`, gerado, da IDE ou fora do escopo deve
ser alterado.

As tarefas são independentes no sentido de possuírem um objetivo e uma verificação
próprios, mas devem ser executadas na ordem indicada. Cada tarefa deve resultar em
um incremento pequeno, revisável e passível de commit separado.

## Tarefa 1 — Caracterizar o comportamento atual ✅ Concluída

### Objetivo

Registrar o comportamento existente do backend antes da refatoração, com foco nos
resultados, estados, mensagens e exceções que não podem mudar acidentalmente.

### Arquivos a criar ou modificar

- Modificar `src/test/groovy/org/alan/todolist/service/TodoServiceSpec.groovy`.
- Criar testes Spock adicionais em `src/test/groovy/org/alan/todolist/` somente
  quando a cobertura do serviço não for suficiente para caracterizar o contrato.

### Atividades

- Cobrir criação, listagem, ordenação por prioridade, atualização e exclusão.
- Cobrir atualização preservando ID e alarmes.
- Cobrir filtros por categoria, prioridade e status, com e sem resultados.
- Cobrir prioridades válidas e inválidas.
- Cobrir alarmes antes, no momento e depois do prazo.
- Cobrir a regra de que tarefas `DONE` não disparam alarmes.
- Cobrir IDs inexistentes e exclusão total.

### Critérios de conclusão

- Os testes descrevem o comportamento atual sem alterar a implementação.
- Resultados, mensagens e tipos de exceção observados estão registrados nos testes.
- Casos de borda listados em [docs/spec.md](./spec.md) possuem cobertura.

### Testes/verificações

- Executar `./gradlew test`.
- Confirmar que nenhum arquivo Java de produção foi modificado.

### Commit sugerido

`test: characterize current todo backend behavior`

### Evidência

Os testes de caracterização e casos de borda foram adicionados em
`src/test/groovy/org/alan/todolist/service/TodoServiceSpec.groovy` e
`./gradlew test` passou.

## Tarefa 2 — Aprovar decisões de contrato ✅ Concluída

### Objetivo

Resolver e registrar as decisões pendentes de [docs/spec.md](./spec.md) antes de
alterar qualquer comportamento potencialmente observável.

### Arquivos a criar ou modificar

- Modificar `docs/spec.md` para registrar as decisões aprovadas.
- Não modificar código-fonte nesta tarefa.

### Decisões obrigatórias

- Filtros sem resultado devem continuar lançando exceção ou retornar lista vazia?
- Quais tipos e mensagens devem representar ID inexistente e argumento nulo?
- Qual será a política para prioridade nula ou fora do intervalo?
- Como preservar a identidade e o isolamento dos IDs?
- A lista de alarmes será mutável, uma cópia ou somente leitura?
- `TodoService` receberá `TodoRepository` por injeção?
- O método público `delelteAllTodo` será corrigido com compatibilidade ou terá seu
  contrato alterado?
- Como o horário será fornecido para testes determinísticos?

### Critérios de conclusão

- Cada decisão possui uma resposta explícita e aprovada.
- As decisões que alterarem contrato possuem critérios de teste definidos.
- Nenhuma decisão pendente é implementada por suposição.

### Testes/verificações

- Revisar [docs/spec.md](./spec.md) e confirmar que as decisões estão registradas.
- Executar `git diff -- src/main/java src/test/groovy` e confirmar ausência de
  alterações de código nesta tarefa.

### Commit sugerido

`docs: approve backend refactoring contracts`

### Evidência

As decisões aprovadas estão registradas na seção 5.1 de `docs/spec.md`.

## Tarefa 3 — Extrair funções e eliminar duplicação ✅ Concluída

### Objetivo

Aplicar os princípios de funções pequenas e DRY nas responsabilidades do menu e do
serviço, sem alterar os contratos aprovados.

### Arquivos a criar ou modificar

- Modificar `src/main/java/org/alan/todolist/terminal/Menu.java`.
- Modificar `src/main/java/org/alan/todolist/service/TodoService.java`.
- Ajustar testes somente quando nomes ou pontos de entrada forem alterados.

### Atividades

- Separar em funções menores a seleção de opções e os handlers de `Menu.start`.
- Extrair leitura e validação compartilhadas por criação e atualização.
- Extrair a lógica comum dos filtros de `TodoService`.
- Manter no serviço as regras de ordenação e domínio; o menu permanece responsável
  pela interação com o usuário.

### Critérios de conclusão

- Cada função extraída possui uma responsabilidade identificável.
- A criação, atualização, listagem, filtros, exclusão e alarmes mantêm os mesmos
  resultados, mensagens e exceções aprovados.
- Não foram criadas abstrações genéricas sem duplicação real.

### Testes/verificações

- Executar `./gradlew test`.
- Verificar manualmente o fluxo do menu afetado.
- Revisar o diff para confirmar que somente backend Java e testes relacionados foram
  alterados.

### Commit sugerido

`refactor: extract cohesive backend operations`

### Evidência

O menu passou a usar handlers e funções compartilhadas para leitura, alarmes e
impressão; os filtros do serviço foram extraídos para uma operação comum.
`./gradlew test` passou.

## Tarefa 4 — Ajustar modelo, encapsulamento e tratamento de erros ✅ Concluída

### Objetivo

Aplicar as decisões aprovadas para erros, IDs, alarmes, dependências e horário,
melhorando as fronteiras do modelo sem introduzir regra nova.

### Arquivos a criar ou modificar

- Modificar `src/main/java/org/alan/todolist/model/Todo.java`.
- Modificar `src/main/java/org/alan/todolist/model/Alarm.java` somente se necessário.
- Modificar `src/main/java/org/alan/todolist/repository/TodoRepository.java`.
- Modificar `src/main/java/org/alan/todolist/service/TodoService.java`.
- Criar ou modificar testes de modelo, repositório e serviço em
  `src/test/groovy/`.

### Atividades

- Aplicar a política aprovada para validação de prioridade.
- Aplicar a política aprovada para exceções e mensagens.
- Preservar IDs e alarmes durante atualizações.
- Ajustar a exposição da coleção de alarmes conforme decisão aprovada.
- Tornar o horário controlável nos testes sem alterar a regra dos alarmes.
- Aplicar injeção do repositório somente se aprovada.

### Critérios de conclusão

- As decisões registradas em [docs/spec.md](./spec.md) estão refletidas no código.
- Nenhuma alteração de comportamento não aprovada foi introduzida.
- Ordenação, status, IDs, alarmes e tarefas `DONE` mantêm o comportamento esperado.

### Testes/verificações

- Executar `./gradlew test`.
- Verificar testes determinísticos para alarmes.
- Executar os casos de borda de prioridade, IDs, atualização, filtros e erros.

### Commit sugerido

`refactor: clarify backend boundaries and error contracts`

### Evidência

Os contratos aprovados foram aplicados ao modelo, repositório e serviço, com
testes determinísticos para alarmes, cópia defensiva e erros. `./gradlew test`
passou.

## Tarefa 5 — Aplicar limpeza incremental e Regra do Escoteiro ✅ Concluída

### Objetivo

Melhorar a clareza local do backend após a estabilização estrutural, aplicando nomes
necessários, comentários mínimos e consistência de formatação sem mudanças
funcionais por preferência de estilo.

### Arquivos a criar ou modificar

- Arquivos Java alterados nas tarefas anteriores.
- `src/test/groovy/` nos testes afetados.
- `docs/spec.md` somente se uma decisão já aprovada precisar ser detalhada.

### Atividades

- Corrigir nomes que realmente prejudiquem compreensão, respeitando compatibilidade
  de APIs públicas.
- Remover código comentado obsoleto e expectativas comentadas.
- Corrigir inconsistências locais de espaçamento, visibilidade e organização.
- Não introduzir formatter, linter ou dependência nova.

### Critérios de conclusão

- Os nomes expressam responsabilidades reais.
- Não há comentários que apenas repitam o código.
- A limpeza ficou restrita aos arquivos e trechos relacionados à refatoração.
- Nenhum comportamento foi alterado sem decisão registrada.

### Testes/verificações

- Executar `./gradlew test`.
- Revisar o diff em busca de renomeações sem justificativa.
- Confirmar que contratos públicos e mensagens permanecem conforme a especificação.

### Commit sugerido

`refactor: improve backend readability`

### Evidência

Foram corrigidos nomes locais, visibilidade e consistência de formatação, sem
alteração de contratos ou regras de negócio. Não há comentários de código
obsoletos nos arquivos de produção ou testes. `./gradlew test` e
`printf '9\n' | ./gradlew run` passaram.

## Tarefa 6 — Validar e encerrar a refatoração ✅ Concluída

### Objetivo

Confirmar a preservação integral do backend Java, dos testes e dos comportamentos
especificados, encerrando a atividade com evidências verificáveis.

### Arquivos a criar ou modificar

- Atualizar `docs/spec.md` somente para registrar decisões finais, testes e
  limitações.
- Nenhuma alteração adicional de código deve ser iniciada nesta tarefa.

### Critérios de conclusão

- Todos os critérios de aceite de [docs/spec.md](./spec.md) são atendidos.
- A refatoração não altera o resultado esperado das operações existentes.
- Todos os casos de borda identificados são validados.
- Não existem mudanças comportamentais sem justificativa e aprovação.
- Nenhum arquivo fora do escopo foi alterado.

### Testes/verificações

- Executar `./gradlew test`.
- Executar `./gradlew run` e verificar manualmente criação, listagem, filtros,
  atualização, exclusão, exclusão total, saída e alarmes.
- Executar `git diff --stat` e revisar `git diff -- src/main/java src/test/groovy`.
- Confirmar que `frontend/`, arquivos gerados e configurações da IDE permanecem
  inalterados.

### Commit sugerido

`chore: validate clean code refactoring`

### Evidência

`./gradlew test` passou. O fluxo automatizado do menu cobriu criação, listagem,
filtros, atualização, exclusão individual, exclusão total e saída com sucesso.
O diff final de código ficou vazio e nenhum arquivo fora do escopo foi alterado.

## Ordem e regra de bloqueio

```text
Tarefa 1 → Tarefa 2 → Tarefa 3 → Tarefa 4 → Tarefa 5 → Tarefa 6
```

Não iniciar uma tarefa posterior quando:

- a tarefa anterior não atender seus critérios de conclusão;
- houver decisão de contrato pendente;
- `./gradlew test` estiver falhando sem causa conhecida e registrada;
- o diff indicar alteração fora do escopo.
