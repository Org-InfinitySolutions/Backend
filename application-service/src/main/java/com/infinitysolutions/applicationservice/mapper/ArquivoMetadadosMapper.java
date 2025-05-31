package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;

public class ArquivoMetadadosMapper {

    public static ArquivoMetadados toEntity(String blobFileName, String blobUrl, String originalFilename, String mImeType, Long fileSize, TipoAnexo tipoAnexo) {
        ArquivoMetadados attachment = new ArquivoMetadados();
        attachment.setBlobName(blobFileName);
        attachment.setBlobUrl(blobUrl);
        attachment.setOriginalFilename(originalFilename);
        attachment.setMimeType(mImeType);
        attachment.setFileSize(fileSize);
        attachment.setTipoAnexo(tipoAnexo);
        return attachment;
    }

}
