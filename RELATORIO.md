# Relatório de implementação

## Contexto

Este relatório registra a execução da refatoração Clean Code do TODO-List,
vinculada à issue [#16](https://github.com/AlanF-Oliveira/todo-list/issues/16).
O trabalho seguiu a especificação em `docs/spec.md` e o plano em
`docs/plano.md`.

## Consumo aproximado por etapa

Os valores abaixo são estimativas de tokens de contexto processados na sessão,
não uma medição de cobrança do provedor. Eles incluem leitura de arquivos,
análise, edição, testes e revisão de cada etapa.

| Etapa | Descrição | Tokens aproximados |
|---|---|---:|
| 1 | Caracterização do comportamento e testes de borda | 5.000 |
| 2 | Decisões de contrato e atualização da spec | 3.000 |
| 3 | Extração de funções e eliminação de duplicação | 4.000 |
| 4 | Encapsulamento, erros, injeção e relógio testável | 4.500 |
| 5 | Limpeza incremental e Regra do Escoteiro | 3.000 |
| 6 | Validação final, fluxo do menu e encerramento | 3.000 |
| Pós-processo | Skill reutilizável e documentação deste relatório | 2.000 |
| **Total estimado** |  | **24.500** |

## Divergência identificada e correção

Durante a implementação inicial, as alterações das Tarefas 1 a 4 foram
acumuladas no mesmo estado de trabalho antes de criar os commits individuais.
Isso divergia da regra de um commit por tarefa do plano. Também foi feita uma
primeira tentativa de executar a aplicação com `./gradlew run < /dev/null`;
como o menu é interativo, o `Scanner` terminou com
`NoSuchElementException`.

A divergência foi corrigida sem descartar as alterações: o diff foi revisado,
as mudanças foram separadas por responsabilidade e foram criados commits
individuais para as quatro tarefas. O fluxo da aplicação foi executado novamente
com entrada válida (`printf '9\n' | ./gradlew run`) e depois com um cenário
completo de criação, listagem, filtros, atualização, exclusão individual,
exclusão total e saída.

## Evidências

- `./gradlew test` passou após cada etapa relevante e na validação final.
- O fluxo completo do menu terminou com `BUILD SUCCESSFUL`.
- As Tarefas 1 a 6 estão marcadas como concluídas em `docs/plano.md`.
- `docs/spec.md` registra contexto, escopo, decisões, critérios de aceite e
  limitações.
- `AGENTS.md` permanece na raiz do projeto.
- A skill genérica está em `skills/feature-com-spec/SKILL.md`.
- O histórico contém um commit por tarefa:
  - `28b6ff3` — caracterização;
  - `4343275` — decisões de contrato;
  - `76cbdb8` — extração de operações;
  - `09fa834` — fronteiras e contratos de erro;
  - `74997fa` — limpeza de legibilidade;
  - `a80e065` — validação e encerramento.

## Escopo e limitações

O backend Java e seus testes foram alterados dentro do escopo aprovado. O
frontend, a integração entre frontend e backend, a persistência externa e os
arquivos gerados permaneceram fora da implementação.
