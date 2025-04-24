# HonESTa – Sistema de Gestão de Cartões e Cupões

## 1. Visão Geral

Este protótipo implementa a gestão de um sistema de fidelização (cartões e cupões de desconto) para a cadeia HonESTa.  
Baseado no enunciado, suportamos:
- Acumular saldo e descontos em cêntimos.
- Emitir e associar cupões a cartões.
- Ativar cupões (uso único) e aplicar o melhor desconto por produto.
- Simular vendas numa janela (caixa) e numa janela cliente (ativação de cupões).

### Principais packages

- **model**: `ProdutoInfo`, `Inventario` (armazenamento em memória).
- **controller**: `Cartao`, `Cupao`, `ProdutoVendido`, `Venda` (lógica de negócio).
- **view**: `JanelaCompra`, `JanelaCartao` (interfaces Swing).
- **boot**: `Main` (popula dados de teste e arranca as janelas).

---

## 2. Fluxo da Aplicação

1. **Carga de dados (Main):**
    - Cria produtos, cupões e cartões e adiciona-os ao `Inventario`.
2. **Arranque das janelas (SwingUtilities):**
    - `JanelaCompra`: lista produtos (inventário), permite adicionar itens, aplicar cupões, usar saldo e pagar.
    - `JanelaCartao`: lista cartões, mostra saldo e cupões disponíveis/futuros, permite ativar cupões antes da compra.
3. **Interação Cliente → Caixa:**
    - No cliente, escolhe-se cupões e ativa‑se o cartão.
    - Na caixa, escolhem-se produtos; ao pagar, a lógica de `Cartao.usar(Venda)`:
        1. Aplica cupões (o de maior desconto primeiro).
        2. Calcula ganho de saldo (diferença entre total bruto e líquido).
        3. Acrescenta esse ganho ao saldo.
        4. Remove cupões usados e desativa o cartão.

---

## 3. Casos de Uso & Validações

| Caso de Uso                    | Método / Local          | Validação                                                                 |
|--------------------------------|-------------------------|----------------------------------------------------------------------------|
| Criar cartão                   | `new Cartao(...)`       | Número não nulo/vazio; saldo inicial ≥ 0.                                  |
| Adicionar cupões a um cartão   | `Cartao.adicionarCupoes`| Cupões não nulos; evita duplicados.                                        |
| Ativar cupões no cartão        | `Cartao.ativar(...)`    | Seleção não vazia; pertencem ao cartão; todos dentro do prazo.             |
| Atualizar cupões expiráveis    | `Cartao.atualizarCupoes`| Remove cupões cuja dataFim < hoje.                                         |
| Aplicar cupões numa venda      | `Venda.aplicarMelhoresCupons` | Ordena por percentual desc.; aplica apenas um cupão por item.          |
| Uso do cartão numa venda       | `Cartao.usar(Venda)`    | Só se cartão ativo; aplica cupões; acumula saldo; limpa cupões usados.     |
| Reduzir saldo                  | `Cartao.reduzirSaldo`   | Gasto ≥ 0; se gasto ≥ saldo, saldo fica 0.                                  |
| Acumular saldo                 | `Cartao.acumularSaldo`  | Valor ≥ 0.                                                                 |

---

## 4. Diagrama de Classes

```mermaid
classDiagram
    class ProdutoInfo {
        - String codigoBarras
        - String marca
        - String modelo
        - long precoAtual
        + getCodigoBarras()
        + getMarca()
        + getModelo()
        + getPrecoAtual()
        + setPrecoAtual(long)
    }

    class Cupao {
        - String numero
        - String resumo
        - int percentual
        - LocalDate dataInicio
        - LocalDate dataFim
        - List<String> produtos
        + estaValido()
        + abrange(ProdutoVendido)
        + aplicar(ProdutoVendido)
    }

    class ProdutoVendido {
        - ProdutoInfo produto
        - long preco
        - long desconto
        + precoFim()
        + setDesconto(long)
    }

    class Venda {
        - LocalDate data
        - List<ProdutoVendido> itensVendidos
        - List<Cupao> cuponsAplicados
        + adicionarItem(ProdutoVendido)
        + aplicarMelhoresCupons(List<Cupao>)
        + totalBruto()
        + totalLiquido()
        + numeroDeCuponsUsados()
    }

    class Cartao {
        - String numero
        - long saldo
        - List<Cupao> listaCupoes
        - List<Cupao> ativos
        - boolean ativo
        + ativar(List<Cupao>)
        + usar(Venda)
        + getCupoesDisponiveis()
        + getCupoesFuturos()
        + atualizarCupoes()
        + reduzirSaldo(long)
        + acumularSaldo(long)
    }

    class Inventario {
        - Map<String,ProdutoInfo> produtos
        - Map<String,Cartao> cartoes
        - Map<String,Cupao> cupoes
        + addProduto(ProdutoInfo)
        + addCartao(Cartao)
        + addCupao(Cupao)
        + todosProdutos()
        + todosCartoes()
        + todosCupoes()
    }

    %% Relações
    ProdutoVendido --> ProdutoInfo
    Venda "1" o-- "*" ProdutoVendido : itensVendidos
    Venda "1" o-- "*" Cupao : cuponsAplicados
    Cartao "1" o-- "*" Cupao : listaCupoes
    Cartao "1" o-- "*" Cupao : ativos
    Inventario "1" o-- "*" ProdutoInfo : produtos
    Inventario "1" o-- "*" Cartao : cartoes
    Inventario "1" o-- "*" Cupao : cupoes
