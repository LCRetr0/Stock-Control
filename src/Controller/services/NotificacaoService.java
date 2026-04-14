package Controller.services;

import model.Produto.Produto;

import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificacaoService {

    // Configurações de Email
    private static final String EMAIL_REMETENTE = "----";
    private static final String SENHA_APP = "";
    private static final String EMAIL_DESTINATARIO = "----";

    // Enviar Email
    public static boolean enviarEmail(Produto produto) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMETENTE, SENHA_APP);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_REMETENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_DESTINATARIO));
            message.setSubject("⚠️ Alerta de Validade - " + produto.getNome_Do_Produto());

            String corpo = String.format("""
                ALERTA DE PRODUTO PRÓXIMO DO VENCIMENTO
                
                Produto: %s
                Loja: %s
                Lote: %s
                Validade: %s
                Dias restantes: %d
                
                Atenção: Este produto vencerá em %d dias!
                """,
                    produto.getNome_Do_Produto(),
                    produto.getLoja(),
                    produto.getLote(),
                    produto.getValidade(),
                    produto.getDiasParaVencer(),
                    produto.getDiasParaVencer()
            );

            message.setText(corpo);
            Transport.send(message);
            System.out.println("✅ Email enviado com sucesso!");
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar email: " + e.getMessage());
            return false;
        }
    }


    public static void enviarNotificacaoCompleta(Produto produto) {
        System.out.println("\n=== ENVIANDO NOTIFICAÇÃO POR EMAIL ===");
        System.out.println(produto);

        boolean emailEnviado = enviarEmail(produto);

        if (emailEnviado) {
            System.out.println("✅ Notificação por email enviada com sucesso!");
        } else {
            System.out.println("❌ Falha no envio do email.");
        }
    }

    // Verificar e notificar todos os produtos próximos do vencimento
    public static void verificarProdutosProximoVencimento(List<Produto> produtos) {
        System.out.println("\n=== VERIFICANDO PRODUTOS PRÓXIMOS DO VENCIMENTO ===");

        for (Produto produto : produtos) {
            if (produto.proximodevencer()) {
                System.out.println("⚠️ Produto próximo do vencimento encontrado!");
                enviarNotificacaoCompleta(produto);
            }
        }
    }
    public static boolean enviarEmailVencido(Produto produto) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMETENTE, SENHA_APP);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_REMETENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_DESTINATARIO));
            message.setSubject("🚨 PRODUTO VENCIDO URGENTE - " + produto.getNome_Do_Produto());

            long diasAtraso = Math.abs(produto.getDiasParaVencer());
            String corpo = String.format("""
            🚨 ALERTA: PRODUTO VENCIDO!
            
            PRODUTO ESTÁ VENCIDO HÁ %d DIA(S)!
            
            Produto: %s
            Loja: %s
            Lote: %s
            Validade: %s (VENCIDO)
            
            ⚠️ AÇÃO IMEDIATA NECESSÁRIA:
            - Remover do estoque
            - Comunicar responsável
            - Verificar devoluções
            
            Este é um alerta automático do sistema.
            """,
                    diasAtraso, produto.getNome_Do_Produto(), produto.getLoja(),
                    produto.getLote(), produto.getValidade()
            );

            message.setText(corpo);
            Transport.send(message);
            System.out.println("🚨 Email VENCIDO enviado: " + produto.getNome_Do_Produto());
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Erro email vencido: " + e.getMessage());
            return false;
        }
    }
}