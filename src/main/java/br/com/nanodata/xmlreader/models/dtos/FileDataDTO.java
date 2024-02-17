package br.com.nanodata.xmlreader.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDataDTO {

    private Long id;
    private String fileId;
    private LocalDateTime idedhEmi;
    private String idenNF;
    private String idecUF;
    private String emitCNPJ;
    private String emitxFant;
    private String destCNPJ;
    private String destxNome;
    private String iCMSTotvTotTrib;
    private String iCMSTotvNF;
    private Long fileContentId;


}
