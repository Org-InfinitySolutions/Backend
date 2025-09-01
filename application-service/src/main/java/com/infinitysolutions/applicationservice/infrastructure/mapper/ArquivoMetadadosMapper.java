package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;

public class ArquivoMetadadosMapper {

    public static ArquivoMetadadosEntity toEntity(String blobFileName, String blobUrl, String originalFilename, String mImeType, Long fileSize, TipoAnexo tipoAnexo) {
        ArquivoMetadadosEntity attachment = new ArquivoMetadadosEntity();
        attachment.setBlobName(blobFileName);
        attachment.setBlobUrl(blobUrl);
        attachment.setOriginalFilename(originalFilename);
        attachment.setMimeType(mImeType);
        attachment.setFileSize(fileSize);
        attachment.setTipoAnexo(tipoAnexo);
        return attachment;
    }

}
