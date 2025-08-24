USE novalocacoes;

-- Populating Cargo table (if not already populated)
INSERT INTO cargo (descricao, nome) VALUES
('Administrador do sistema', 'ADMIN'),
('Funcionário da empresa', 'FUNCIONARIO'),
('Usuário pessoa física', 'USUARIO_PF'),
('Usuário pessoa jurídica', 'USUARIO_PJ')
ON DUPLICATE KEY UPDATE descricao = VALUES(descricao);

-- Populating Categories
INSERT INTO categorias (nome, is_ativo) VALUES 
('Projetores', 1),
('Notebooks', 1),
('Servidores', 1),
('Sistemas de Som', 1),
('Microfones', 1),
('Iluminação', 1),
('Telas de Projeção', 1),
('Equipamentos de Rede', 1),
('Monitores e TVs', 1),
('Acessórios', 1),
('Tablets', 1),
('Impressoras', 1),
('Sistemas de Videoconferência', 1),
('Palcos e Estruturas', 1),
('Câmeras', 1);

-- Get category IDs for reference in products
SET @cat_projetores = (SELECT id FROM categorias WHERE nome = 'Projetores');
SET @cat_notebooks = (SELECT id FROM categorias WHERE nome = 'Notebooks');
SET @cat_servidores = (SELECT id FROM categorias WHERE nome = 'Servidores');
SET @cat_som = (SELECT id FROM categorias WHERE nome = 'Sistemas de Som');
SET @cat_microfones = (SELECT id FROM categorias WHERE nome = 'Microfones');
SET @cat_iluminacao = (SELECT id FROM categorias WHERE nome = 'Iluminação');
SET @cat_telas = (SELECT id FROM categorias WHERE nome = 'Telas de Projeção');
SET @cat_rede = (SELECT id FROM categorias WHERE nome = 'Equipamentos de Rede');
SET @cat_monitores = (SELECT id FROM categorias WHERE nome = 'Monitores e TVs');
SET @cat_acessorios = (SELECT id FROM categorias WHERE nome = 'Acessórios');
SET @cat_tablets = (SELECT id FROM categorias WHERE nome = 'Tablets');
SET @cat_impressoras = (SELECT id FROM categorias WHERE nome = 'Impressoras');
SET @cat_videoconf = (SELECT id FROM categorias WHERE nome = 'Sistemas de Videoconferência');
SET @cat_palcos = (SELECT id FROM categorias WHERE nome = 'Palcos e Estruturas');
SET @cat_cameras = (SELECT id FROM categorias WHERE nome = 'Câmeras');

-- Populating Products

-- Projetores
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Projetor Full HD 5000 lumens com conectividade HDMI e WiFi', 1, 'Epson', 'PowerLite 5000', 15, 'https://www.epson.com.br', @cat_projetores),
('Projetor 4K com 7000 lumens ideal para grandes eventos', 1, 'Sony', 'VPL-VW700ES', 8, 'https://www.sony.com.br', @cat_projetores),
('Mini projetor portátil com bateria para pequenos ambientes', 1, 'LG', 'PH550G', 20, 'https://www.lg.com/br', @cat_projetores),
('Projetor de curta distância para salas de treinamento', 1, 'BenQ', 'TH671ST', 12, 'https://www.benq.com', @cat_projetores),
('Projetor laser de alta definição para eventos premium', 1, 'Panasonic', 'PT-RZ970', 5, 'https://www.panasonic.com/br', @cat_projetores);

-- Notebooks
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Notebook profissional i7 com 16GB RAM e SSD 512GB', 1, 'Dell', 'Latitude 7420', 25, 'https://www.dell.com.br', @cat_notebooks),
('Notebook com tela touch para apresentações interativas', 1, 'HP', 'Spectre x360', 18, 'https://www.hp.com.br', @cat_notebooks),
('MacBook Pro M2 para edição de vídeo e aplicações gráficas', 1, 'Apple', 'MacBook Pro 16"', 10, 'https://www.apple.com/br', @cat_notebooks),
('Notebook robusto para uso em ambientes externos', 1, 'Lenovo', 'ThinkPad T14s', 15, 'https://www.lenovo.com/br', @cat_notebooks),
('Notebook gamer para demonstrações de jogos em eventos', 1, 'Alienware', 'M15 R7', 8, 'https://www.alienware.com.br', @cat_notebooks),
('Notebook econômico para uso administrativo em eventos', 1, 'Acer', 'Aspire 5', 30, 'https://www.acer.com/br', @cat_notebooks);

-- Servidores
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Servidor rack 2U com processador Xeon e 64GB RAM', 1, 'Dell', 'PowerEdge R740', 5, 'https://www.dell.com.br', @cat_servidores),
('Servidor torre para pequenos eventos com backup local', 1, 'HP', 'ProLiant ML350 Gen10', 8, 'https://www.hp.com.br', @cat_servidores),
('Servidor de alto desempenho para processamento de dados em tempo real', 1, 'Lenovo', 'ThinkSystem SR650', 3, 'https://www.lenovo.com/br', @cat_servidores),
('Mini servidor portátil para eventos de médio porte', 1, 'Intel', 'NUC Enterprise', 12, 'https://www.intel.com.br', @cat_servidores);

-- Sistemas de Som
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Sistema de som line array profissional para grandes eventos', 1, 'JBL', 'VTX A8', 4, 'https://www.jbl.com.br', @cat_som),
('Caixa de som ativa com 1000W e bluetooth', 1, 'QSC', 'K12.2', 15, 'https://www.qsc.com', @cat_som),
('Sistema de som compacto para pequenos eventos', 1, 'Bose', 'L1 Pro32', 10, 'https://www.bose.com.br', @cat_som),
('Mesa de som digital com 24 canais', 1, 'Yamaha', 'TF3', 8, 'https://www.yamaha.com.br', @cat_som),
('Subwoofer ativo de 18 polegadas com 1500W', 1, 'EV', 'EKX-18SP', 12, 'https://www.electrovoice.com', @cat_som);

-- Microfones
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Microfone sem fio profissional com receptor', 1, 'Shure', 'QLXD24/SM58', 20, 'https://www.shure.com.br', @cat_microfones),
('Kit de microfones para conferência com 4 unidades', 1, 'Audio-Technica', 'ATND1061', 8, 'https://www.audio-technica.com', @cat_microfones),
('Microfone de lapela sem fio para palestras', 1, 'Sennheiser', 'EW 112P G4', 15, 'https://www.sennheiser.com.br', @cat_microfones),
('Microfone headset para apresentadores', 1, 'AKG', 'C520', 12, 'https://www.akg.com', @cat_microfones),
('Microfone gooseneck para mesa de conferência', 1, 'Rode', 'Reporter', 10, 'https://www.rode.com', @cat_microfones);

-- Iluminação
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Refletor LED RGBW para palcos e eventos', 1, 'Avolites', 'Diamond 9', 25, 'https://www.avolites.com', @cat_iluminacao),
('Kit de iluminação com 4 moving heads', 1, 'Martin', 'MAC Aura XB', 8, 'https://www.martin.com', @cat_iluminacao),
('Strobo LED de alta potência', 1, 'Chauvet', 'Strike 4', 15, 'https://www.chauvetprofessional.com', @cat_iluminacao),
('Controlador DMX para sistemas de iluminação', 1, 'GrandMA', 'MA3 Compact', 5, 'https://www.malighting.com', @cat_iluminacao),
('Iluminação de ambiente com fitas LED RGB', 1, 'Philips', 'Hue Strip Plus', 30, 'https://www.philips-hue.com', @cat_iluminacao);

-- Telas de Projeção
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Tela de projeção tensionada 150" formato 16:9', 1, 'Elite Screens', 'Saker Tab-Tension', 12, 'https://www.elitescreens.com', @cat_telas),
('Tela de projeção portátil 100" com tripé', 1, 'Epson', 'ELPSC32', 20, 'https://www.epson.com.br', @cat_telas),
('Tela de projeção inflável para eventos ao ar livre', 1, 'Open Air Cinema', 'Pro 20\'', 5, 'https://www.openaircinema.com', @cat_telas),
('Tela de LED modular P3.9 para eventos indoor', 1, 'Absen', 'PL3.9 Pro', 4, 'https://www.absen.com', @cat_telas);

-- Equipamentos de Rede
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Switch gerenciável 24 portas Gigabit', 1, 'Cisco', 'Catalyst 2960-X', 10, 'https://www.cisco.com.br', @cat_rede),
('Roteador WiFi 6 para ambientes de alta densidade', 1, 'Ubiquiti', 'UniFi Dream Machine Pro', 15, 'https://www.ui.com', @cat_rede),
('Access Point WiFi 6 de longo alcance', 1, 'TP-Link', 'EAP670', 20, 'https://www.tp-link.com.br', @cat_rede),
('Firewall para segurança de rede em eventos', 1, 'FortiNet', 'FortiGate 100F', 8, 'https://www.fortinet.com', @cat_rede),
('Extensor de sinal WiFi para grandes espaços', 1, 'Netgear', 'Orbi Pro SXK80', 12, 'https://www.netgear.com.br', @cat_rede);

-- Monitores e TVs
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('TV LED 4K 65" para estandes e apresentações', 1, 'Samsung', 'QN65Q80T', 10, 'https://www.samsung.com.br', @cat_monitores),
('Monitor touchscreen 55" para interação com público', 1, 'LG', '55TA3E', 8, 'https://www.lg.com/br', @cat_monitores),
('Monitor profissional 4K 32" para edição de vídeo', 1, 'Dell', 'UltraSharp UP3221Q', 15, 'https://www.dell.com.br', @cat_monitores),
('TV OLED 77" para eventos premium', 1, 'Sony', 'XR-77A80J', 5, 'https://www.sony.com.br', @cat_monitores),
('Video wall 2x2 com 4 telas de 55"', 1, 'Philips', 'BDL5588XH', 3, 'https://www.philips.com.br', @cat_monitores);

-- Acessórios
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Adaptador HDMI para VGA', 1, 'StarTech', 'HD2VGAE2', 50, 'https://www.startech.com', @cat_acessorios),
('Hub USB-C com HDMI, Ethernet e USB 3.0', 1, 'Anker', 'PowerExpand+', 40, 'https://www.anker.com', @cat_acessorios),
('Tripé para câmera profissional', 1, 'Manfrotto', 'MT055XPRO3', 15, 'https://www.manfrotto.com', @cat_acessorios),
('Cabos HDMI 4K 5 metros', 1, 'Belkin', 'Ultra HD', 60, 'https://www.belkin.com', @cat_acessorios),
('Carregador portátil 26800mAh para dispositivos móveis', 1, 'RAVPower', 'PD Pioneer', 30, 'https://www.ravpower.com', @cat_acessorios),
('Apontador laser com controle de slides', 1, 'Logitech', 'R800', 25, 'https://www.logitech.com.br', @cat_acessorios);

-- Tablets
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Tablet 12.9" para assinaturas e interação', 1, 'Apple', 'iPad Pro', 15, 'https://www.apple.com/br', @cat_tablets),
('Tablet Android 10" para controle de sistemas', 1, 'Samsung', 'Galaxy Tab S7', 20, 'https://www.samsung.com.br', @cat_tablets),
('Tablet resistente à água e quedas para eventos outdoor', 1, 'Panasonic', 'Toughpad FZ-A3', 10, 'https://www.panasonic.com/br', @cat_tablets),
('Tablet Windows para aplicações empresariais', 1, 'Microsoft', 'Surface Pro 8', 12, 'https://www.microsoft.com/pt-br', @cat_tablets);

-- Impressoras
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Impressora térmica para credenciais e ingressos', 1, 'Zebra', 'ZC300', 8, 'https://www.zebra.com', @cat_impressoras),
('Impressora multifuncional para escritório temporário', 1, 'HP', 'LaserJet Pro MFP M428fdw', 12, 'https://www.hp.com.br', @cat_impressoras),
('Impressora portátil para crachás', 1, 'Brother', 'VC-500W', 15, 'https://www.brother.com.br', @cat_impressoras),
('Impressora de grande formato para banners', 1, 'Epson', 'SureColor T5470', 5, 'https://www.epson.com.br', @cat_impressoras);

-- Sistemas de Videoconferência
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Sistema de videoconferência para salas médias', 1, 'Poly', 'Studio X50', 8, 'https://www.poly.com', @cat_videoconf),
('Câmera de conferência 4K com tracking automático', 1, 'Logitech', 'Rally Plus', 10, 'https://www.logitech.com.br', @cat_videoconf),
('Sistema completo para transmissão ao vivo', 1, 'Blackmagic Design', 'ATEM Mini Pro', 6, 'https://www.blackmagicdesign.com', @cat_videoconf),
('Speakerphone para conferências', 1, 'Jabra', 'Speak 750', 15, 'https://www.jabra.com.br', @cat_videoconf);

-- Palcos e Estruturas
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Palco modular 6x4m com altura ajustável', 1, 'Prolyte', 'StageDex', 5, 'https://www.prolyte.com', @cat_palcos),
('Sistema de treliça para iluminação 10x8m', 1, 'Global Truss', 'F34 Square', 8, 'https://www.globaltruss.com', @cat_palcos),
('Praticável para palco 2x1m', 1, 'Duratruss', 'DT Stage', 20, 'https://www.duratruss.com', @cat_palcos),
('Backdrop telescópico para fundo de palco', 1, 'OnStage', 'TLS6000', 10, 'https://www.on-stage.com', @cat_palcos);

-- Câmeras
INSERT INTO produtos (descricao, is_ativo, marca, modelo, qtd_estoque, url_fabricante, categoria_id) VALUES
('Câmera DSLR profissional para fotografia de eventos', 1, 'Canon', 'EOS 5D Mark IV', 8, 'https://www.canon.com.br', @cat_cameras),
('Câmera de vídeo 4K para transmissões ao vivo', 1, 'Sony', 'PXW-Z190', 6, 'https://www.sony.com.br', @cat_cameras),
('Webcam 4K para streaming de alta qualidade', 1, 'Logitech', 'BRIO', 15, 'https://www.logitech.com.br', @cat_cameras),
('Câmera 360° para tour virtual de eventos', 1, 'Insta360', 'Pro 2', 4, 'https://www.insta360.com', @cat_cameras),
('Drone com câmera 4K para filmagens aéreas', 1, 'DJI', 'Phantom 4 Pro V2.0', 5, 'https://www.dji.com', @cat_cameras);