package com.infinitysolutions.applicationservice.core.domain.email;

public class EmailTemplateBuilder {

    public String gerarPatternData() {
        return "dd/MM/yyyy - HH:mm";
    }

    public String gerarTemplateVerificacaoEmail(String nome, String codigo){
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Código de Verificação</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .verification-box {
                            background-color: #f5f7fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #e1e4e8;
                        }
                
                        .verification-code {
                            font-size: 30px;
                            font-weight: 700;
                            color: #EE1C27;
                            padding: 10px 25px;
                            background-color: white;
                            border-radius: 4px;
                            display: inline-block;
                            margin: 10px 0;
                            border: 1px dashed #ccc;
                            letter-spacing: 2px;
                        }
                
                        .code-info {
                            font-size: 14px;
                            color: #666;
                            margin-top: 15px;
                        }
                
                        .security-note {
                            background-color: #fdf9e8;
                            border-left: 4px solid #f3d57a;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Verificação de Email</h1>
                        <p class="greeting">Olá <strong>%s</strong>,</p>
                
                        <p>Estamos quase acabando o seu processo de cadastro. Para garantir a segurança da sua conta, precisamos verificar seu endereço de email.</p>
                
                        <div class="verification-box">
                            <p>Seu código de verificação é:</p>
                            <div class="verification-code">%s</div>
                            <p class="code-info">Este código é válido por <strong>10 minutos</strong>.</p>
                        </div>
                
                        <p>Insira este código na página de verificação para completar seu cadastro.</p>
                
                        <div class="security-note">
                            <strong>Nota de Segurança:</strong> Se você não solicitou este código, por favor ignore este email.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nome, codigo);
    }

    public String gerarTemplateCadastroCompleto(String nome) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Cadastro Realizado com Sucesso</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .success-box {
                            background-color: #f0f8f0;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #d4e6d4;
                        }                        .success-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #017D1D;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 30px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;                        }

                        .success-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #017D1D;
                            margin: 10px 0;
                        }
                
                        .benefits-section {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                        }
                
                        .benefits-title {
                            font-size: 18px;
                            font-weight: 500;
                            color: #333;
                            margin-bottom: 15px;
                            text-align: center;
                        }
                
                        .benefits-list {
                            list-style: none;
                            padding: 0;
                            margin: 0;
                        }                        .benefits-list li {
                            padding: 8px 0;
                            color: #555;
                            font-size: 14px;
                            position: relative;
                            padding-left: 0;
                        }

                        .cta-button {
                            display: inline-block;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            color: white !important;
                            padding: 12px 25px;
                            text-decoration: none !important;
                            border-radius: 4px;
                            font-weight: 500;
                            margin: 20px 0;
                            text-align: center;
                            border: none;
                            mso-line-height-rule: exactly;
                        }
                
                        .cta-button:hover {
                            color: white !important;
                            text-decoration: none !important;
                        }
                
                        .cta-button:visited {
                            color: white !important;
                        }
                
                        .cta-button:active {
                            color: white !important;
                        }
                
                        .cta-section {
                            text-align: center;
                            margin: 25px 0;
                        }
                
                        .info-note {
                            background-color: #e8f4f8;
                            border-left: 4px solid #3E5FA9;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Bem-vindo à NovaLocacoes!</h1>
                        <p class="greeting">Olá <strong>%s</strong>,</p>                        <div class="success-box">
                            <div class="success-icon">&#x2713;</div>
                            <p class="success-message">Cadastro realizado com sucesso!</p>
                            <p>Sua conta foi criada e verificada. Agora você já pode aproveitar os nossos serviços.</p>
                        </div>
                
                        <p>Estamos muito felizes em tê-lo(a) conosco! A partir de agora você tem acesso à plataforma NovaLocacoes.</p>
                
                        <div class="benefits-section">
                            <p class="benefits-title">O que você pode fazer agora:</p>                            <ul class="benefits-list">
                                <li>&#x2713; Navegar por nosso catálogo completo de equipamentos</li>
                                <li>&#x2713; Realizar locações de forma rápida e segura</li>
                                <li>&#x2713; Gerenciar suas locações no painel pessoal</li>
                                <li>&#x2713; Acompanhar o histórico de suas locações</li>
                            </ul>
                        </div>
                
                        <div class="cta-section">
                            <p>Pronto para começar sua primeira locação?</p>
                            <a href="#" class="cta-button">Explorar Equipamentos</a>
                        </div>
                
                        <div class="info-note">
                            <strong>Dica:</strong> Mantenha seus dados sempre atualizados em seu perfil para uma experiência ainda melhor. 
                            Se precisar de ajuda, nossa equipe de suporte está sempre disponível.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nome);
    }

    public String gerarTemplateNotificacaoNovoCadastro(String nomeUsuario, String emailUsuario, String dataHoraCadastro) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Novo Usuário Cadastrado</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .notification-box {
                            background-color: #e8f4f8;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #b8dce8;
                        }
                
                        .notification-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #3E5FA9;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 30px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .notification-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #3E5FA9;
                            margin: 10px 0;
                        }
                
                        .user-details {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #3E5FA9;
                        }
                
                        .user-details-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333;
                            margin-bottom: 15px;
                        }
                
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            padding: 8px 0;
                            border-bottom: 1px solid #e9ecef;
                        }
                
                        .detail-row:last-child {
                            border-bottom: none;
                        }
                
                        .detail-label {
                            font-weight: 500;
                            color: #555;
                            min-width: 120px;
                        }
                
                        .detail-value {
                            color: #333;
                            text-align: right;
                            word-break: break-word;
                        }
                
                        .stats-section {
                            background-color: #f0f8f0;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                        }
                
                        .stats-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #017D1D;
                            margin-bottom: 10px;
                        }
                
                        .stats-text {
                            font-size: 14px;
                            color: #555;
                        }
                
                        .cta-button {
                            display: inline-block;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            color: white !important;
                            padding: 12px 25px;
                            text-decoration: none !important;
                            border-radius: 4px;
                            font-weight: 500;
                            margin: 20px 0;
                            text-align: center;
                            border: none;
                        }
                
                        .cta-button:hover {
                            color: white !important;
                            text-decoration: none !important;
                        }
                
                        .cta-button:visited {
                            color: white !important;
                        }
                
                        .cta-button:active {
                            color: white !important;
                        }
                
                        .cta-section {
                            text-align: center;
                            margin: 25px 0;
                        }
                
                        .admin-note {
                            background-color: #fff3cd;
                            border-left: 4px solid #EE1C27;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        @media only screen and (max-width: 600px) {
                            .detail-row {
                                flex-direction: column;
                            }
                            
                            .detail-value {
                                text-align: left;
                                margin-top: 5px;
                            }
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Novo Usuário Cadastrado</h1>
                        <p class="greeting">Olá, <strong>Administrador</strong>,</p>
                
                        <div class="notification-box">
                            <div class="notification-icon">&#x1F44B;</div>
                            <p class="notification-message">Um novo usuário se cadastrou na plataforma!</p>
                            <p>A base de usuários da NovaLocacoes continua crescendo.</p>
                        </div>
                
                        <p>Um novo cliente acabou de completar o processo de cadastro e verificação em nossa plataforma. Confira os detalhes abaixo:</p>
                
                        <div class="user-details">
                            <p class="user-details-title">&#x1F464; Detalhes do Novo Usuário:</p>
                            <div class="detail-row">
                                <span class="detail-label">Nome:</span>
                                <span class="detail-value"><strong>%s</strong></span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Email:</span>
                                <span class="detail-value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Data do Cadastro:</span>
                                <span class="detail-value">%s</span>
                            </div>
                           <!-- <div class="detail-row"> -->
                               <!-- <span class="detail-label">Status:</span> -->
                                <!-- <span class="detail-value" style="color: #017D1D; font-weight: 500;">&#x2713; Verificado</span> -->
                            <!-- </div> -->
                        </div>
                
                        <div class="stats-section">
                            <p class="stats-title">&#x1F4C8; Crescimento da Plataforma</p>
                            <p class="stats-text">Mais um cliente se juntou à nossa comunidade de usuários satisfeitos!</p>
                        </div>
                
                        <div class="cta-section">
                            <p>Acesse o painel administrativo para mais detalhes:</p>
                            <a href="#" class="cta-button">Acessar Painel Admin</a>
                        </div>
                
                        <div class="admin-note">
                            <strong>Lembrete:</strong> O usuário já passou pela verificação de email e está pronto para utilizar todos os serviços da plataforma. 
                            Considere enviar um email de boas-vindas personalizado se necessário.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Sistema de Notificações<br><strong>NovaLocacoes - Painel Administrativo</strong></p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nomeUsuario, emailUsuario, dataHoraCadastro);
    }

    public String gerarTemplatePedidoConcluido(String nomeUsuario, String numeroPedido,
                                              String situacaoPedido, String dataHoraPedido, String qtdItens) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Pedido Realizado com Sucesso</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .order-confirmation-box {
                            background-color: #f0f8f0;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #d4e6d4;
                        }
                
                        .order-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #017D1D;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 25px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .order-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #017D1D;
                            margin: 10px 0;
                        }
                
                        .order-details {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #3E5FA9;
                        }
                
                        .order-details-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333;
                            margin-bottom: 15px;
                            display: flex;
                            align-items: center;
                        }
                
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            padding: 8px 0;
                            border-bottom: 1px solid #e9ecef;
                        }
                
                        .detail-row:last-child {
                            border-bottom: none;
                        }
                
                        .detail-label {
                            font-weight: 500;
                            color: #555;
                            min-width: 140px;
                        }
                
                        .detail-value {
                            color: #333;
                            text-align: right;
                            word-break: break-word;
                        }
                
                        .order-number {
                            font-weight: 700;
                            color: #EE1C27;
                            font-size: 18px;
                        }
                
                        .status-badge {
                            background-color: #017D1D;
                            color: white;
                            padding: 4px 8px;
                            border-radius: 4px;
                            font-size: 12px;
                            font-weight: 500;
                        }
                
                        .next-steps-section {
                            background-color: #e8f4f8;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                        }
                
                        .next-steps-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #3E5FA9;
                            margin-bottom: 15px;
                            display: flex;
                            align-items: center;
                        }
                
                        .steps-list {
                            list-style: none;
                            padding: 0;
                            margin: 0;
                        }
                
                        .steps-list li {
                            padding: 8px 0;
                            color: #555;
                            font-size: 14px;
                            position: relative;
                            padding-left: 25px;
                        }
                
                        .steps-list li::before {
                            content: counter(step-counter);
                            counter-increment: step-counter;
                            position: absolute;
                            left: 0;
                            top: 8px;
                            background-color: #3E5FA9;
                            color: white;
                            width: 18px;
                            height: 18px;
                            border-radius: 50%%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 11px;
                            font-weight: bold;
                        }
                
                        .steps-list {
                            counter-reset: step-counter;
                        }
                
                        .cta-button {
                            display: inline-block;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            color: white !important;
                            padding: 12px 25px;
                            text-decoration: none !important;
                            border-radius: 4px;
                            font-weight: 500;
                            margin: 20px 0;
                            text-align: center;
                            border: none;
                        }
                
                        .cta-button:hover {
                            color: white !important;
                            text-decoration: none !important;
                        }
                
                        .cta-button:visited {
                            color: white !important;
                        }
                
                        .cta-button:active {
                            color: white !important;
                        }
                
                        .cta-section {
                            text-align: center;
                            margin: 25px 0;
                        }
                
                        .support-note {
                            background-color: #fff3cd;
                            border-left: 4px solid #f3d57a;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        @media only screen and (max-width: 600px) {
                            .detail-row {
                                flex-direction: column;
                            }
                            
                            .detail-value {
                                text-align: left;
                                margin-top: 5px;
                            }
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Pedido Realizado com Sucesso!</h1>
                        <p class="greeting">Olá <strong>%s</strong>,</p>
                
                        <div class="order-confirmation-box">
                            <div class="order-icon">&#x1F4E6;</div>
                            <p class="order-message">Seu pedido foi realizado com sucesso!</p>
                            <p>Recebemos sua solicitação e ela já está sendo processada pela nossa equipe.</p>
                        </div>
                
                        <p>Obrigado por escolher a NovaLocacoes! Seu pedido foi registrado em nosso sistema e você receberá atualizações sobre o andamento.</p>
                
                        <!-- Order Details -->
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="background-color: #f8f9fa; border-radius: 6px; margin: 25px 0; border-left: 4px solid #3E5FA9;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="font-size: 16px; font-weight: 500; color: #333; margin-bottom: 15px;">
                                                        📋 Detalhes do Pedido:
                                                    </p>
                                                    
                                                    <!-- Detail Rows -->
                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%%">
                                                        <tr>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; font-weight: 500; color: #555; width: 140px;">
                                                                Número do Pedido:
                                                            </td>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; color: #333; text-align: right;">
                                                                <span style="font-weight: 700; color: #EE1C27; font-size: 18px;">#%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; font-weight: 500; color: #555; width: 140px;">
                                                                Status:
                                                            </td>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; color: #333; text-align: right;">
                                                                <span style="background-color: #017D1D; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 500;">%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; font-weight: 500; color: #555; width: 140px;">
                                                                Data do Pedido:
                                                            </td>
                                                            <td style="padding: 8px 0; border-bottom: 1px solid #e9ecef; color: #333; text-align: right;">
                                                                %s
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; font-weight: 500; color: #555; width: 140px;">
                                                                Quantidade de Itens:
                                                            </td>
                                                            <td style="padding: 8px 0; color: #333; text-align: right;">
                                                                <strong>%s itens</strong>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Next Steps Section -->
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="background-color: #e8f4f8; border-radius: 6px; margin: 25px 0;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="font-size: 16px; font-weight: 500; color: #3E5FA9; margin-bottom: 15px;">
                                                        🚀 Próximos Passos:
                                                    </p>
                                                    
                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%%">
                                                        <tr>
                                                            <td style="padding: 8px 0; color: #555; font-size: 14px; vertical-align: top; width: 25px;">
                                                                <span style="background-color: #3E5FA9; color: white; width: 18px; height: 18px; border-radius: 50%%; display: inline-block; text-align: center; font-size: 11px; font-weight: bold; line-height: 18px;">1</span>
                                                            </td>
                                                            <td style="padding: 8px 0 8px 7px; color: #555; font-size: 14px;">
                                                                Nossa equipe analisará seu pedido e verificará a disponibilidade dos equipamentos
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; color: #555; font-size: 14px; vertical-align: top; width: 25px;">
                                                                <span style="background-color: #3E5FA9; color: white; width: 18px; height: 18px; border-radius: 50%%; display: inline-block; text-align: center; font-size: 11px; font-weight: bold; line-height: 18px;">2</span>
                                                            </td>
                                                            <td style="padding: 8px 0 8px 7px; color: #555; font-size: 14px;">
                                                                Você receberá uma confirmação por email assim que o pedido for aprovado
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; color: #555; font-size: 14px; vertical-align: top; width: 25px;">
                                                                <span style="background-color: #3E5FA9; color: white; width: 18px; height: 18px; border-radius: 50%%; display: inline-block; text-align: center; font-size: 11px; font-weight: bold; line-height: 18px;">3</span>
                                                            </td>
                                                            <td style="padding: 8px 0 8px 7px; color: #555; font-size: 14px;">
                                                                Entraremos em contato para agendar a entrega ou retirada dos equipamentos
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0; color: #555; font-size: 14px; vertical-align: top; width: 25px;">
                                                                <span style="background-color: #3E5FA9; color: white; width: 18px; height: 18px; border-radius: 50%%; display: inline-block; text-align: center; font-size: 11px; font-weight: bold; line-height: 18px;">4</span>
                                                            </td>
                                                            <td style="padding: 8px 0 8px 7px; color: #555; font-size: 14px;">
                                                                Acompanhe o status do seu pedido através do seu painel pessoal
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                
                        <div class="cta-section">
                            <p>Acompanhe o status do seu pedido em tempo real:</p>
                            <a href="#" class="cta-button">Acompanhar Pedido</a>
                        </div>
                
                        <div class="support-note">
                            <strong>Precisa de ajuda?</strong> Nossa equipe de suporte está disponível para esclarecer dúvidas sobre seu pedido. 
                            Entre em contato conosco a qualquer momento.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nomeUsuario, numeroPedido, situacaoPedido, dataHoraPedido, qtdItens);
    }

    public String gerarTemplateNotificacaoNovoPedido(String nomeUsuario, String emailUsuario, String telefoneUsuario, String numeroPedido,
                                                    String situacaoPedido, String dataHoraPedido,
                                                    String qtdItens, String descricaoPedido) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Novo Pedido Realizado</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .notification-box {
                            background-color: #fff3cd;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #ffeaa7;
                        }
                
                        .notification-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #EE1C27;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 25px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .notification-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #EE1C27;
                            margin: 10px 0;
                        }
                
                        .customer-info {
                            background-color: #e8f4f8;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #3E5FA9;
                        }
                
                        .customer-info-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333;
                            margin-bottom: 15px;
                            display: flex;
                            align-items: center;
                        }
                
                        .order-info {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #017D1D;
                        }
                
                        .order-info-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333;
                            margin-bottom: 15px;
                            display: flex;
                            align-items: center;
                        }
                
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            padding: 8px 0;
                            border-bottom: 1px solid #e9ecef;
                        }
                
                        .detail-row:last-child {
                            border-bottom: none;
                        }
                
                        .detail-label {
                            font-weight: 500;
                            color: #555;
                            min-width: 140px;
                        }
                
                        .detail-value {
                            color: #333;
                            text-align: right;
                            word-break: break-word;
                        }
                
                        .order-number {
                            font-weight: 700;
                            color: #EE1C27;
                            font-size: 16px;
                        }
                
                        .status-badge {
                            background-color: #017D1D;
                            color: white;
                            padding: 4px 8px;
                            border-radius: 4px;
                            font-size: 12px;
                            font-weight: 500;
                        }
                
                        .priority-section {
                            background-color: #fdf2f2;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #EE1C27;
                        }
                
                        .priority-title {
                            font-size: 16px;
                            font-weight: 500;
                            color: #EE1C27;
                            margin-bottom: 10px;
                            display: flex;
                            align-items: center;
                        }
                
                        .priority-text {
                            font-size: 14px;
                            color: #555;
                        }
                
                        .description-section {
                            background-color: #f9f9f9;
                            border-radius: 6px;
                            padding: 15px;
                            margin: 15px 0;
                            border: 1px solid #e1e4e8;
                        }
                
                        .description-title {
                            font-size: 14px;
                            font-weight: 500;
                            color: #555;
                            margin-bottom: 8px;
                        }
                
                        .description-text {
                            font-size: 14px;
                            color: #333;
                            font-style: italic;
                            background-color: white;
                            padding: 10px;
                            border-radius: 4px;
                            border-left: 3px solid #3E5FA9;
                        }
                
                        .cta-button {
                            display: inline-block;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            color: white !important;
                            padding: 12px 25px;
                            text-decoration: none !important;
                            border-radius: 4px;
                            font-weight: 500;
                            margin: 20px 0;
                            text-align: center;
                            border: none;
                        }
                
                        .cta-button:hover {
                            color: white !important;
                            text-decoration: none !important;
                        }
                
                        .cta-button:visited {
                            color: white !important;
                        }
                
                        .cta-button:active {
                            color: white !important;
                        }
                
                        .cta-section {
                            text-align: center;
                            margin: 25px 0;
                        }
                
                        .admin-note {
                            background-color: #e8f4f8;
                            border-left: 4px solid #3E5FA9;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        @media only screen and (max-width: 600px) {
                            .detail-row {
                                flex-direction: column;
                            }
                            
                            .detail-value {
                                text-align: left;
                                margin-top: 5px;
                            }
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Novo Pedido Realizado!</h1>
                        <p class="greeting">Olá, <strong>Administrador</strong>,</p>
                
                        <div class="notification-box">
                            <div class="notification-icon">&#x1F4E6;</div>
                            <p class="notification-message">Um novo pedido foi realizado!</p>
                            <p>Um cliente acabou de fazer um pedido na plataforma NovaLocacoes.</p>
                        </div>
                
                        <p>Um novo pedido foi registrado em nossa plataforma e requer sua atenção. Confira os detalhes completos abaixo:</p>
                
                        <div class="customer-info">
                            <p class="customer-info-title">&#x1F464; Informações do Cliente:</p>
                            <div class="detail-row">
                                <span class="detail-label">Nome do Cliente:</span>
                                <span class="detail-value"><strong>%s</strong></span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Email:</span>
                                <span class="detail-value">%s</span>
                            </div>
                             <div class="detail-row">
                                                    <span class="detail-label">Telefone:</span>
                                                    <span class="detail-value">%s</span>
                             </div>
                        </div>
                
                        <div class="order-info">
                            <p class="order-info-title">&#x1F4CB; Detalhes do Pedido:</p>
                            <div class="detail-row">
                                <span class="detail-label">Número do Pedido:</span>
                                <span class="detail-value order-number">#%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Status Atual:</span>
                                <span class="detail-value"><span class="status-badge">%s</span></span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Data do Pedido:</span>
                                <span class="detail-value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Quantidade de Itens:</span>
                                <span class="detail-value"><strong>%s itens</strong></span>
                            </div>
                        </div>
                
                        <div class="description-section">
                            <p class="description-title">&#x1F4DD; Descrição/Observações do Cliente:</p>
                            <div class="description-text">
                                %s
                            </div>
                        </div>
                
                        <div class="priority-section">
                            <p class="priority-title">&#x26A0; Ação Necessária:</p>
                            <p class="priority-text">Este pedido está aguardando análise e precisa ser processado. 
                            Verifique a disponibilidade dos itens e entre em contato com o cliente para confirmar detalhes.</p>
                        </div>
                
                        <div class="cta-section">
                            <p>Acesse o painel administrativo para gerenciar este pedido:</p>
                            <a href="#" class="cta-button">Gerenciar Pedido</a>
                        </div>
                
                        <div class="admin-note">
                            <strong>Lembrete:</strong> O cliente receberá atualizações automáticas sobre o status do pedido. 
                            Certifique-se de atualizar o status conforme o andamento do processo.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Sistema de Notificações<br><strong>NovaLocacoes - Painel Administrativo</strong></p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nomeUsuario, emailUsuario, telefoneUsuario, numeroPedido, situacaoPedido, dataHoraPedido, qtdItens, descricaoPedido);
    }
      public String gerarTemplateMudancaStatusPedido(String nomeUsuario, String numeroPedido,
                                                  String statusAnterior, String novoStatus, String dataHoraMudanca) {
        // Lógica para determinar o estado de cada status na timeline
        String aprovadoDotClass = "pending";
        String aprovadoTextClass = "";
        String emEventoDotClass = "pending";
        String emEventoTextClass = "";
        String finalizadoDotClass = "pending";
        String finalizadoTextClass = "";
        
        switch (novoStatus.toUpperCase()) {
            case "APROVADO":
                aprovadoDotClass = "current";
                aprovadoTextClass = "current";
                break;
            case "EM_EVENTO":
            case "EM EVENTO":
                aprovadoDotClass = "completed";
                emEventoDotClass = "current";
                emEventoTextClass = "current";
                break;
            case "FINALIZADO":
                aprovadoDotClass = "completed";
                emEventoDotClass = "completed";
                finalizadoDotClass = "current";
                finalizadoTextClass = "current";
                break;
        }
        
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Atualização do Status do Pedido</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .status-update-box {
                            background-color: #f0f7ff;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #c3dfff;
                        }
                
                         .order-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #017D1D;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 25px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .status-icon {
                                    width: 60px;
                                    height: 60px;
                                    background-color: #3E5FA9;
                                    border-radius: 50%%;
                                    display: inline-block;
                                    margin-bottom: 15px;
                                    position: relative;
                                    line-height: 60px;
                                    text-align: center;
                                    color: white;
                                    font-size: 25px;
                                    font-weight: bold;
                                }
                        .order-details {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border-left: 4px solid #3E5FA9;
                        }
                
                        .order-details-title {
                            font-size: 16px;
                            font-weight: 600;
                            color: #333;
                            margin-bottom: 15px;
                            text-align: center;
                        }
                
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            padding: 8px 0;
                            border-bottom: 1px solid #e9ecef;
                        }
                
                        .detail-row:last-child {
                            border-bottom: none;
                        }
                
                        .detail-label {
                            font-weight: 500;
                            color: #555;
                        }
                
                        .detail-value {
                            color: #333;
                            font-weight: normal;
                        }
                
                        .status-change-highlight {
                            background-color: #fff3cd;
                            border: 1px solid #ffeaa7;
                            border-radius: 4px;
                            padding: 15px;
                            margin: 20px 0;
                            text-align: center;
                        }
                
                        .status-from {
                            color: #856404;
                            text-decoration: line-through;
                            font-weight: 500;
                            margin-right: 10px;
                        }
                
                        .status-arrow {
                            color: #3E5FA9;
                            font-weight: bold;
                            margin: 0 10px;
                        }
                
                        .status-to {
                            color: #017D1D;
                            font-weight: bold;
                            background-color: #d4f0d4;
                            padding: 5px 10px;
                            border-radius: 15px;
                            display: inline-block;
                        }
                
                        .next-steps {
                            background-color: #e8f4f8;
                            border-left: 4px solid #17a2b8;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 0 4px 4px 0;
                        }
                
                        .next-steps-title {
                            font-weight: 600;
                            color: #0c5460;
                            margin-bottom: 10px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .status-timeline {
                            margin: 20px 0;
                            padding: 20px;
                            background-color: #f8f9fa;
                            border-radius: 6px;
                        }
                
                        .timeline-item {
                            display: flex;
                            align-items: center;
                            margin: 10px 0;
                            font-size: 14px;
                        }
                
                        .timeline-dot {
                            width: 12px;
                            height: 12px;
                            border-radius: 50%%;
                            margin-right: 15px;
                            flex-shrink: 0;
                        }
                
                        .timeline-dot.completed {
                            background-color: #017D1D;
                        }
                
                        .timeline-dot.current {
                            background-color: #3E5FA9;
                            box-shadow: 0 0 0 3px rgba(62, 95, 169, 0.3);
                        }
                
                        .timeline-dot.pending {
                            background-color: #ddd;
                        }
                
                        .timeline-text {
                            color: #666;
                        }
                
                        .timeline-text.current {
                            color: #3E5FA9;
                            font-weight: 600;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Atualização do Seu Pedido</h1>
                        <p class="greeting">Olá <strong>%s</strong>,</p>
                
                        <p>Temos uma atualização importante sobre o seu pedido. O status foi alterado e queremos mantê-lo informado sobre o progresso.</p>
                
                        <div class="status-update-box">
                            <div class="status-icon">🔄</div>
                            <div class="status-message">Status do Pedido Atualizado!</div>
                        </div>
                
                        <div class="order-details">
                            <div class="order-details-title">📋 Detalhes do Pedido</div>
                            <div class="detail-row">
                                <span class="detail-label">Número do Pedido:</span>
                                <span class="detail-value"><strong>#%s</strong></span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">Data da Atualização:</span>
                                <span class="detail-value">%s</span>
                            </div>
                        </div>
                
                        <div class="status-change-highlight">
                            <p><strong>Mudança de Status:</strong></p>
                            <span class="status-from">%s</span>
                            <span class="status-arrow">→</span>
                            <span class="status-to">%s</span>
                        </div>                        <div class="status-timeline">
                            <h3 style="margin-top: 0; color: #333; font-size: 16px;">🔄 Linha do Tempo do Pedido</h3>
                            <div class="timeline-item">
                                <div class="timeline-dot completed"></div>
                                <div class="timeline-text">1. Em Análise - Pedido criado e aguardando aprovação</div>
                            </div>
                            <div class="timeline-item">
                                <div class="timeline-dot %s"></div>
                                <div class="timeline-text %s">2. Aprovado - Pedido aprovado pelo administrador</div>
                            </div>
                            <div class="timeline-item">
                                <div class="timeline-dot %s"></div>
                                <div class="timeline-text %s">3. Em Evento - Produtos entregues no destino</div>
                            </div>
                            <div class="timeline-item">
                                <div class="timeline-dot %s"></div>
                                <div class="timeline-text %s">4. Finalizado - Evento concluído com sucesso</div>
                            </div>
                        </div>
                
                        <div class="next-steps">
                            <div class="next-steps-title">📌 Próximos Passos</div>
                            <p>Dependendo do novo status, você pode acompanhar o progresso do seu pedido através da nossa plataforma. 
                            Se tiver alguma dúvida, nossa equipe está sempre disponível para ajudá-lo.</p>
                        </div>
                
                        <p>Agradecemos pela sua confiança e estamos trabalhando para garantir que sua experiência seja a melhor possível.</p>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nomeUsuario, numeroPedido, dataHoraMudanca, statusAnterior, novoStatus,
                      aprovadoDotClass, aprovadoTextClass, emEventoDotClass, emEventoTextClass, 
                      finalizadoDotClass, finalizadoTextClass);
    }

    public String confirmacaoResetEmail(String emailDestinatario) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Email Alterado com Sucesso</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .success-box {
                            background-color: #f0f8f0;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #d4e6d4;
                        }
                
                        .success-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #017D1D;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 30px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .success-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #017D1D;
                            margin: 10px 0;
                        }
                
                        .email-info-box {
                            background-color: #f8f9fa;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            border: 1px solid #e1e4e8;
                        }
                
                        .new-email {
                            font-size: 16px;
                            font-weight: 500;
                            color: #3E5FA9;
                            padding: 10px 15px;
                            background-color: white;
                            border-radius: 4px;
                            display: inline-block;
                            margin: 10px 0;
                            border: 1px solid #ccc;
                            word-break: break-all;
                        }
                
                        .security-note {
                            background-color: #fdf9e8;
                            border-left: 4px solid #f3d57a;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .info-note {
                            background-color: #e8f4f8;
                            border-left: 4px solid #3E5FA9;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Email Alterado com Sucesso</h1>
                        <div class="success-box">
                            <div class="success-icon">&#x2713;</div>
                            <p class="success-message">Alteração realizada com sucesso!</p>
                            <p>Seu endereço de email foi alterado e sua conta agora está vinculada ao novo email.</p>
                        </div>
                
                        <p>Confirmamos que a alteração do seu endereço de email foi processada com sucesso em nossa plataforma.</p>
                
                        <div class="email-info-box">
                            <p><strong>Novo endereço de email:</strong></p>
                            <div class="new-email">%s</div>
                            <p style="margin-top: 15px; font-size: 14px; color: #666;">
                                A partir de agora, todas as comunicações da NovaLocacoes serão enviadas para este endereço.
                            </p>
                        </div>
                
                        <div class="info-note">
                            <strong>Importante:</strong> Certifique-se de atualizar seus dados de acesso caso necessário. 
                            Agora você deve usar este novo email para fazer login em sua conta.
                        </div>
                
                        <div class="security-note">
                            <strong>Nota de Segurança:</strong> Se você não solicitou esta alteração, entre em contato 
                            conosco imediatamente através dos nossos canais de suporte.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
                """.formatted(emailDestinatario);
    }

    public String confirmacaoResetSenha() {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Senha Alterada com Sucesso</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%, #3E5FA9 50%, #017D1D 100%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .success-box {
                            background-color: #f0f8f0;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #d4e6d4;
                        }
                
                        .success-icon {
                            width: 60px;
                            height: 60px;
                            background-color: #017D1D;
                            border-radius: 50%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 60px;
                            text-align: center;
                            color: white;
                            font-size: 30px;
                            font-weight: bold;
                            font-family: Arial, sans-serif;
                        }
                
                        .success-message {
                            font-size: 18px;
                            font-weight: 500;
                            color: #017D1D;
                            margin: 10px 0;
                        }
                
                        .info-note {
                            background-color: #e8f4f8;
                            border-left: 4px solid #3E5FA9;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%, #3E5FA9 50%, #017D1D 100%);
                            margin: 0;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Senha Alterada com Sucesso</h1>
                
                        <div class="success-box">
                            <div class="success-icon">&#x2713;</div>
                            <p class="success-message">Sua senha foi alterada com sucesso!</p>
                            <p>Agora você já pode entrar na sua conta usando a nova senha.</p>
                        </div>
                
                        <div class="info-note">
                            <strong>Dica:</strong> Se não reconheceu esta alteração, entre em contato imediatamente com nosso suporte.
                        </div>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
                """;
    }

    public String gerarTemplateResetSenha(String nome, String codigo) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Código para Reset de Senha</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                        body {
                            font-family: 'Roboto', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333333;
                            background-color: #f9f9f9;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 4px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                
                        .email-header {
                            background-color: white;
                            color: black;
                            padding: 20px 30px;
                            text-align: left;
                            border-bottom: 1px solid #f1f1f1;
                            display: flex;
                            align-items: center;
                        }
                
                        .company-logo {
                            display: flex;
                            align-items: center;
                            font-weight: bold;
                        }
                
                        .logo-text {
                            font-size: 18px;
                            font-weight: 600;
                        }
                
                        .logo-icon {
                            display: inline-block;
                            width: 30px;
                            height: 30px;
                            background-color: black;
                            margin-right: 10px;
                            border-radius: 2px;
                            position: relative;
                        }
                
                        .logo-icon::after {
                            content: '';
                            position: absolute;
                            right: -2px;
                            top: 5px;
                            height: 15px;
                            width: 4px;
                            background-color: #ff6600;
                        }
                
                        .gradient-line {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                
                        .email-content {
                            padding: 30px;
                            background-color: #ffffff;
                        }
                
                        h1 {
                            color: #333;
                            font-size: 24px;
                            font-weight: 500;
                            margin: 0 0 20px;
                            text-align: center;
                        }
                
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #333;
                        }
                
                        .reset-box {
                            background-color: #fff5f5;
                            border-radius: 6px;
                            padding: 20px;
                            margin: 25px 0;
                            text-align: center;
                            border: 1px solid #fecaca;
                        }
                
                        .reset-icon {
                            width: 50px;
                            height: 50px;
                            background-color: #EE1C27;
                            border-radius: 50%%;
                            display: inline-block;
                            margin-bottom: 15px;
                            position: relative;
                            line-height: 50px;
                            text-align: center;
                            color: white;
                            font-size: 24px;
                            font-weight: bold;
                        }
                
                        .verification-code {
                            font-size: 30px;
                            font-weight: 700;
                            color: #EE1C27;
                            padding: 10px 25px;
                            background-color: white;
                            border-radius: 4px;
                            display: inline-block;
                            margin: 10px 0;
                            border: 1px dashed #ccc;
                            letter-spacing: 2px;
                        }
                
                        .code-info {
                            font-size: 14px;
                            color: #666;
                            margin-top: 15px;
                        }
                
                        .warning-note {
                            background-color: #fef3cd;
                            border-left: 4px solid #f3d57a;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .security-tips {
                            background-color: #e8f4f8;
                            border-left: 4px solid #3E5FA9;
                            padding: 12px 15px;
                            margin: 20px 0;
                            font-size: 14px;
                        }
                
                        .security-tips ul {
                            margin: 10px 0;
                            padding-left: 20px;
                        }
                
                        .security-tips li {
                            margin: 5px 0;
                        }
                
                        .email-footer {
                            background-color: white;
                            padding: 20px 30px;
                            color: #666666;
                            font-size: 14px;
                            text-align: center;
                            border-top: 1px solid #e1e4e8;
                        }
                
                        .bottom-gradient {
                            height: 4px;
                            background: linear-gradient(90deg, #EE1C27 0%%, #3E5FA9 50%%, #017D1D 100%%);
                            margin: 0;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="company-logo">
                            <!-- <div class="logo-icon"></div> -->
                            <!-- <div class="logo-text">nova locações</div> -->
                        </div>
                    </div>
                    <div class="gradient-line"></div>
                
                    <div class="email-content">
                        <h1>Redefinição de Senha</h1>
                        <p class="greeting">Olá <strong>%s</strong>,</p>
                
                        <p>Recebemos uma solicitação para redefinir a senha da sua conta NovaLocacoes. Para sua segurança, use o código abaixo para criar uma nova senha.</p>
                
                        <div class="reset-box">
                            <div class="reset-icon">🔒</div>
                            <p>Seu código de redefinição de senha é:</p>
                            <div class="verification-code">%s</div>
                            <p class="code-info">Este código é válido por <strong>15 minutos</strong>.</p>
                        </div>
                
                        <p>Insira este código na página de redefinição de senha para criar sua nova senha.</p>
                
                        <div class="warning-note">
                            <strong>⚠️ Importante:</strong> Se você não solicitou esta redefinição de senha, ignore este email. Sua conta permanecerá segura.
                        </div>
                
                        <div class="security-tips">
                            <strong>Dicas de Segurança:</strong>
                            <ul>
                                <li>Nunca compartilhe este código com outras pessoas</li>
                                <li>Use uma senha forte com letras, números e símbolos</li>
                                <li>Evite usar a mesma senha de outras contas</li>
                                <li>Considere usar um gerenciador de senhas</li>
                            </ul>
                        </div>
                
                        <p>Se você tiver dificuldades para redefinir sua senha ou suspeitar de atividade não autorizada em sua conta, entre em contato conosco imediatamente.</p>
                    </div>
                
                    <div class="email-footer">
                        <p>Atenciosamente,<br>Equipe NovaLocacoes</p>
                        <p>&copy; 2025 NovaLocacoes. Todos os direitos reservados.</p>
                    </div>
                    <div class="bottom-gradient"></div>
                </div>
                </body>
                </html>
        """.formatted(nome, codigo);
    }
}
