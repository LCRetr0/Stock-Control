📦 CONTROLE DE ESTOQUE - Guia Completo
✨ Funcionalidades  
📦 Cadastro interativo de produtos    
📋 Listagem com status visual (✓⚠️)  
⚠️ Email 30 dias antes do vencimento  
🚨 Email URGENTE para vencidos  
🗑️ Limpar banco (com confirmação)  
🔒 Senhas protegidas (.gitignore)  

🛠️ Pré-requisitos  
✅ Java 11+
✅ MySQL 8.0+ (XAMPP)
✅ Maven 3.6+
✅ Gmail com App Password  

🚀 Instalação (5 min)
1. git clone <repo>
2. XAMPP → MySQL → START
3. Copie config.properties
4. mvn clean compile
5. mvn exec:java -Dexec.mainClass="main.Main"  

⚙️ Configuração
MySQL (Workbench/XAMPP)  
CREATE DATABASE controle_de_estoque;
-- Tabela criada automaticamente  

Gmail App Password
1. 2FA ativado
2. myaccount.google.com/apppasswords
3. Gerar senha 16 chars
4. Colar em config.properties  

projeto/  
├── .gitignore  
├── pom.xml  
├── config.properties  
├── src/main/java/  
│   ├── model/Produto.java  
│   ├── DataBase/DB.java  
│   ├── Services/NotificacaoService.java  
│   └── main/Main.java  

🎮 Como Usar  
1️⃣ Cadastrar: "Leite", "Loja A", "L001", "25/02/2025"  
2️⃣ Listar: Tabela com ✓⚠️  
3️⃣ 30 dias: Email alerta  
4️⃣ Vencidos: Email 🚨 URGENTE  
5️⃣ Limpar: "CONFIRMAR"  
6️⃣ Sair    

🛠️ Dependências (pom.xml)  
mysql-connector-j:8.0.33  
javax.mail:1.6.2


 