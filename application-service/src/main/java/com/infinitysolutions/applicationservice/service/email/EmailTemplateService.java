package com.infinitysolutions.applicationservice.service.email;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {
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
}
