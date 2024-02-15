package br.com.nanodata.xmlreader.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDataDTO {

    private Long id;
    private String fileId;
    private OffsetDateTime ideDhEmi;
    private String ideCuf;
    private String ideNnf;
    private String emitXFant;
    private String emitCnpj;
    private String destXNome;
    private String destCnpj;
    private String IcmsTotVTotTrib;
    private String IcmsTotVnf;
    private Long fileContentId;
}
