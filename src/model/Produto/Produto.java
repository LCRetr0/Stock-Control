package model.Produto;

import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;

    public class Produto {
        private int id;
        private String Nome_Do_Produto;
        private String Lote;
        private String Loja;
        private LocalDate Validade;

        public Produto(String Nome_Do_Produto, String Lote, String Loja, LocalDate Validade) {
            this.Nome_Do_Produto = Nome_Do_Produto;
            this.Lote = Lote;
            this.Loja = Loja;
            this.Validade = Validade;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome_Do_Produto() {
            return Nome_Do_Produto;
        }

        public void setNome_Do_Produto(String nome_Do_Produto) {
            Nome_Do_Produto = nome_Do_Produto;
        }

        public String getLote() {
            return Lote;
        }

        public void setLote(String lote) {
            Lote = lote;
        }

        public String getLoja() {
            return Loja;
        }

        public void setLoja(String loja) {
            Loja = loja;
        }

        public LocalDate getValidade() {
            return Validade;
        }

        public void setValidade(LocalDate validade) {
            Validade = validade;
        }

        public boolean proximodevencer() {
            LocalDate hoje = LocalDate.now();
            LocalDate vencimento = hoje.plusDays(30);
            return Validade.isBefore(vencimento) && Validade.isAfter(hoje);
        }

        public long getDiasParaVencer() {
            return ChronoUnit.DAYS.between(LocalDate.now(), Validade);
        }

        @Override
        public String toString() {
            return "Produto{id=" + id + ", nome='" + Nome_Do_Produto + "', loja='" + Loja +
                    "', lote='" + Lote + "', validade=" + Validade + "}";
        }

        public boolean jaVenceu() {
            LocalDate hoje = LocalDate.now();
            return Validade != null && Validade.isBefore(hoje);
        }
    }