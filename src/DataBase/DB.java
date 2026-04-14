package DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Produto;


public class DB {
    static {
        criarBancoSeNaoExistir();
    }
    private static final String URL = "jdbc:mysql://localhost:3306/controle_de_estoque";
    private static final String USUARIO = "root";
    private static final String SENHA = "Patriota29$";

    public static void criarBancoSeNaoExistir() {
        String urlSemBanco = "jdbc:mysql://localhost:3306";
        try (Connection conn = DriverManager.getConnection(urlSemBanco, USUARIO, SENHA)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS controle_de_estoque");
            System.out.println("✅ Banco 'controle_de_estoque' criado!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar banco: " + e.getMessage());
        }
    }
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void criarTabela() {
        String SQL = """
                 CREATE TABLE IF NOT EXISTS produtos(
                 id INT AUTO_INCREMENT PRIMARY KEY,
                 nome_produto VARCHAR(255) NOT NULL,
                 loja VARCHAR(255) NOT NULL,
                 lote VARCHAR(50) NOT NULL,
                 validade DATE NOT NULL
                )
                
                """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
            System.out.println("Tabela criada com sucesso ");
        } catch (SQLException e) {
            System.out.println("Erro ao criar Tabela: " + e.getMessage());
        }

    }

    public static int insercaoproduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome_produto, loja, lote, validade) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, produto.getNome_Do_Produto());
            pstmt.setString(2, produto.getLoja());
            pstmt.setString(3, produto.getLote());
            pstmt.setDate(4, Date.valueOf(produto.getValidade()));
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        }
        return -1;


    }

    public static List<Produto> listarprodutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto p = new Produto("", "", "", LocalDate.now());
                p.setId(rs.getInt("id"));
                p.setNome_Do_Produto(rs.getString("Nome_produto"));
                p.setLoja(rs.getString("Loja"));
                p.setLote(rs.getString("Lote"));
                p.setValidade(rs.getDate("Validade").toLocalDate());
                produtos.add(p);
            }





        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }
    public static List<Produto> buscarProdutosProximosVencimento() {
        List<Produto> produtos = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);

        String sql = "SELECT * FROM produtos WHERE validade BETWEEN ? AND ? AND validade >= ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(hoje));
            pstmt.setDate(2, Date.valueOf(limite));
            pstmt.setDate(3, Date.valueOf(hoje));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto("", "", "", LocalDate.now());
                p.setId(rs.getInt("id"));
                p.setNome_Do_Produto(rs.getString("nome_produto"));
                p.setLoja(rs.getString("loja"));
                p.setLote(rs.getString("lote"));
                p.setValidade(rs.getDate("validade").toLocalDate());
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }
    public static boolean limparTodosProdutos() {
        String sql = "DELETE FROM produtos";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            int deletados = stmt.executeUpdate(sql);
            System.out.println("🗑️  " + deletados + " produtos removidos!");
            return deletados > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao limpar: " + e.getMessage());
            return false;
        }
    }

}
