
import model.Produto.Produto;
import model.DataBase.DB;
import Controller.services.NotificacaoService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static model.DataBase.DB.buscarProdutosProximosVencimento;
import static model.DataBase.DB.limparTodosProdutos;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🚀 === SISTEMA DE CONTROLE DE ESTOQUE === 🚀");
        criarTabela();

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarProdutoInterativo();
                    break;
                case 2:
                    listarTodosProdutos();
                    break;
                case 3:
                    verificarVencimentos();
                    break;
                case 4:
                    buscarProdutosProximosVencimento();
                    break;
                case 5:
                    limparTodosProdutos();
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        } while (opcao != 6);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n📋 === MENU ===");
        System.out.println("1️⃣  Cadastrar Novo Produto");
        System.out.println("2️⃣  Listar Todos Produtos");
        System.out.println("3️⃣  Verificar Próximos 30 Dias (Email)");
        System.out.println("4️⃣  Verificar VENCIDOS (Email 🚨)");
        System.out.println("5️⃣  LIMPAR BANCO (🗑️ CUIDADO!)");
        System.out.println("6️⃣  Sair");
        System.out.println("=".repeat(40));
    }

    private static void criarTabela() {
        try {
            DB.criarTabela();
            System.out.println("✅ Tabela criada com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar tabela: " + e.getMessage());
        }
    }

    private static void cadastrarProdutoInterativo() {
        System.out.println("\n📦 === CADASTRAR PRODUTO ===");
        String nome = lerString("Nome do Produto: ");
        String loja = lerString("Loja: ");
        String lote = lerString("Lote: ");
        LocalDate validade = lerData("Validade (dd/MM/yyyy): ");
        Produto produto = new Produto(nome, lote, loja, validade);

        // Salvar no banco
        int id = DB.insercaoproduto(produto);
        if (id > 0) {
            System.out.println("✅ Produto cadastrado com ID: " + id);
            System.out.println("📋 Dados salvos:");
            System.out.println(produto);
        } else {
            System.out.println("❌ Erro ao cadastrar produto!");
        }
    }

    private static void listarTodosProdutos() {
        System.out.println("\n📋 === LISTA DE PRODUTOS ===");
        List<Produto> produtos = DB.listarprodutos();

        if (produtos.isEmpty()) {
            System.out.println("📭 Nenhum produto cadastrado!");
            return;
        }

        System.out.printf("%-3s | %-20s | %-10s | %-10s | %s%n",
                "ID", "PRODUTO", "LOTE", "LOJA", "VALIDADE");
        System.out.println("-".repeat(60));

        for (Produto p : produtos) {
            long dias = p.getDiasParaVencer();
            String status = dias <= 30 ? "⚠️" : "✓";
            System.out.printf("%-3d | %-20s | %-10s | %-10s | %s (%d dias) %s%n",
                    p.getId(), p.getNome_Do_Produto(), p.getLote(),
                    p.getLoja(), p.getValidade(), dias, status);
        }
    }

    private static void verificarVencimentos() {
        System.out.println("\n📧 === VERIFICANDO VENCIMENTOS ===");
        List<Produto> produtosProximos = buscarProdutosProximosVencimento();

        if (produtosProximos.isEmpty()) {
            System.out.println("✅ Nenhum produto próximo do vencimento!");
        } else {
            NotificacaoService.verificarProdutosProximoVencimento(produtosProximos);
        }
    }


    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Digite apenas números!");
            }
        }
    }

    private static LocalDate lerData(String mensagem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine().trim();
                return LocalDate.parse(dataStr, formatter);
            } catch (Exception e) {
                System.out.println("❌ Formato inválido! Use: dd/MM/yyyy (ex: 25/12/2025)");
            }
        }
    }
    private static void verificarVencidos() {
        System.out.println("\n🚨 === VERIFICANDO PRODUTOS VENCIDOS ===");
        List<Produto> vencidos = DB.buscarProdutosProximosVencimento();

        if (vencidos.isEmpty()) {
            System.out.println("✅ Nenhum produto vencido!");
        } else {
            NotificacaoService.enviarEmailVencido((Produto) vencidos);
        }
    }
}