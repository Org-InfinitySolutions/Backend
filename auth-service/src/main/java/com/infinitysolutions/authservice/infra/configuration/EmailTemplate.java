package com.infinitysolutions.authservice.infra.configuration;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {
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
