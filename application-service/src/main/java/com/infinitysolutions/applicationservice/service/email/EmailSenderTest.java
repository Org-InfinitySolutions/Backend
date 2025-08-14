package com.infinitysolutions.applicationservice.service.email;

import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoMudancaStatusDTO;
import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoPedidoConcluidoAdminDTO;
import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoPedidoConcluidoDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAutenticacaoCadastroDTO;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderTest implements CommandLineRunner {

    @Autowired
    EnvioEmailService envioEmailService;

    private static final String EMAIL = "filipefranca876@gmail.com";

    @Override
    public void run(String... args) throws Exception {
//        envioEmailService.enviarCadastroSucessoUsuario(new UsuarioAutenticacaoCadastroDTO("Filipe", EMAIL));
//        envioEmailService.enviarNotificacaoNovoCadastro(new UsuarioAutenticacaoCadastroDTO("Filipe", EMAIL));
//        envioEmailService.enviarPedidoConcluidoUsuario(new EmailNotificacaoPedidoConcluidoDTO("Filipe", "100", EMAIL, "12"));
//        envioEmailService.enviarNotificacaoNovoPedido(new EmailNotificacaoPedidoConcluidoAdminDTO("Filipe", "100", EMAIL, "10", "(11) 96626-8140", ""));
//        envioEmailService.enviarNotificacaoMudancaStatusPedido(new EmailNotificacaoMudancaStatusDTO("Filipe", "100", SituacaoPedido.EM_EVENTO.getNome(), SituacaoPedido.FINALIZADO.getNome(), EMAIL));

    }
}
