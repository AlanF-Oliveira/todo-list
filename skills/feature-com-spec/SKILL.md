---
name: feature-com-spec
description: "Use when implementing any feature with Spec-Driven Development (SDD): define and approve a spec, create an executable plan, implement one task at a time, validate behavior, and commit each task separately."
argument-hint: "Descreva a feature e indique o projeto onde ela será implementada"
user-invocable: true
---

# Feature com especificação

## Objetivo

Implementar uma feature seguindo o fluxo:

```text
descrição da feature → spec aprovada → plano executável → implementação incremental → validação final
```

O processo deve transformar uma necessidade em comportamento verificável, limitar o
escopo e deixar um histórico revisável de cada incremento.

## Entradas

- **Descrição da feature:** objetivo, comportamento esperado, restrições,
  usuários afetados e critérios de sucesso conhecidos.
- **Projeto:** repositório, diretório de trabalho, stack, convenções existentes e
  arquivos relevantes.

Antes de editar, leia as instruções do projeto, identifique a stack, examine a
estrutura relevante e verifique o estado do Git. Não presuma integrações,
contratos ou regras que não estejam demonstrados.

## Processo

### Etapa 2 — Criar e aprovar a especificação

Gere ou atualize `docs/spec.md` (ou o local de documentação adotado pelo
projeto) contendo:

- objetivo e contexto;
- escopo incluído e explicitamente excluído;
- comportamento atual que deve ser preservado;
- comportamento pretendido;
- contratos de entrada, saída, erros, persistência e compatibilidade;
- critérios de aceite mensuráveis;
- riscos, ambiguidades e decisões que exigem aprovação;
- estratégia de testes.

Não implemente código enquanto houver decisão de contrato pendente. Apresente as
ambiguidades ao usuário com opções concretas e registre as decisões aprovadas na
spec antes de continuar.

Revise a spec contra a descrição da feature, o código existente e as instruções
do projeto. Confirme que cada mudança de comportamento tem justificativa e teste
de aceitação.

### Etapa 3 — Criar o plano executável

Gere ou atualize `docs/plano.md` com tarefas pequenas, ordenadas e independentes
o suficiente para revisão. Para cada tarefa, registre:

- objetivo;
- arquivos e superfícies afetados;
- atividades de implementação;
- critérios de conclusão;
- testes e verificações;
- mensagem de commit sugerida.

Inclua dependências entre tarefas e uma regra de bloqueio: não iniciar a próxima
quando a atual falhar, houver decisão pendente ou o escopo tiver sido violado.
Leia o plano novamente e confirme que ele cobre todos os critérios da spec sem
incluir trabalho não autorizado.

### Etapa 4 — Implementar uma tarefa por vez

Antes de cada tarefa:

1. marque a tarefa como `em andamento` no plano, se o formato adotado permitir;
2. releia a tarefa, a spec e o diff acumulado;
3. inspecione as superfícies afetadas e procure padrões existentes;
4. implemente somente o incremento descrito;
5. atualize testes e documentação diretamente relacionados.

Não misture refatorações, correções ou funcionalidades fora da tarefa. Preserve
contratos e valide tipos, mensagens, persistência, acessibilidade e integrações
relevantes conforme o projeto.

### Etapa 5 — Revisar e validar a tarefa

Para cada tarefa concluída:

1. leia o diff gerado e explique o que mudou;
2. execute os testes e o build/linter mínimos que cobrem a alteração;
3. rode a aplicação ou uma verificação equivalente quando houver fluxo
   executável;
4. compare os resultados com a spec e os critérios de aceite;
5. corrija falhas antes de prosseguir;
6. revise se arquivos fora do escopo permaneceram inalterados;
7. marque a tarefa como concluída no plano, registrando evidências.

Falhas de teste, comportamento inesperado ou ambiguidade devem bloquear a
próxima tarefa até serem resolvidos ou aprovados explicitamente.

### Etapa 6 — Consolidar um commit por tarefa

Depois que a tarefa passar pela revisão:

1. confira `git diff --check`, o status e os arquivos staged;
2. crie exatamente um commit para aquela tarefa, com mensagem clara e específica;
3. inclua o trailer de coautoria exigido pelo projeto, quando aplicável;
4. confirme o commit e a árvore de trabalho;
5. só então avance para a próxima tarefa.

Ao final, execute a validação completa, revise o histórico e registre na spec e no
plano os testes, limitações, decisões finais e arquivos alterados.

## Regras

- Não escrever código de produção antes da spec ser aprovada.
- Implementar uma tarefa por vez.
- Revisar o diff, rodar a aplicação/testes e pedir correções quando necessário
  antes de concluir cada tarefa.
- Fazer um commit por tarefa; não acumular tarefas em um único commit.
- Não alterar arquivos, contratos ou comportamento fora do escopo aprovado.
- Não iniciar a próxima tarefa com testes falhando, decisões pendentes ou diff
  fora do escopo.
- Preservar alterações preexistentes e nunca desfazer trabalho não relacionado.
- Preferir mudanças pequenas, tipadas, testáveis e compatíveis com as convenções
  do projeto.
- Registrar no plano as evidências de conclusão e no histórico a mensagem do
  commit correspondente.

## Modelos

### Estrutura esperada de `spec.md`

```markdown
# Especificação — <nome da feature>

## 1. Objetivo
<problema, resultado esperado e motivação>

## 2. Escopo
### Incluído
- <superfícies e comportamentos incluídos>
### Fora do escopo
- <superfícies e comportamentos excluídos>

## 3. Comportamento existente
- <contratos e regras que devem permanecer>

## 4. Comportamento pretendido
- <cenários, fluxos e regras novas>

## 5. Contratos e decisões
- entradas, saídas, erros, persistência e compatibilidade
- decisões pendentes que exigem aprovação
- decisões aprovadas e sua justificativa

## 6. Critérios de aceite
1. <critério verificável>
2. <critério verificável>

## 7. Estratégia de testes
- <testes unitários, integração, UI, regressão e validação manual>
```

### Estrutura esperada de `plano.md`

```markdown
# Plano SDD — <nome da feature>

## Referência oficial
Este plano deve ser executado conforme [docs/spec.md](./spec.md).

## Tarefa 1 — <verbo e resultado>
### Objetivo
<incremento>
### Arquivos e superfícies
- <arquivos, módulos ou endpoints>
### Atividades
- <passos limitados ao escopo>
### Critérios de conclusão
- <resultado verificável>
### Testes/verificações
- <comandos e cenários>
### Commit sugerido
`<tipo>: <mensagem>`

## Tarefa N — ...

## Ordem e regra de bloqueio
`Tarefa 1 → Tarefa 2 → ... → Tarefa N`

Não iniciar uma tarefa posterior quando a anterior falhar, houver decisão
pendente ou o diff indicar alteração fora do escopo.
```
