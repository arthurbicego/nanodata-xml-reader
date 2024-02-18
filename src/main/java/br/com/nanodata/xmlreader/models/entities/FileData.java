package br.com.nanodata.xmlreader.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileId;
    private LocalDateTime idedhEmi;
    private String idenNF;
    private String idecUF;
    private String emitCNPJ;
    private String emitxFant;
    private String destCNPJ;
    private String destxNome;
    private Double icmstotvTotTrib;
    private Double icmstotvNF;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_content_id", referencedColumnName = "id")
    private FileContent fileContent;
}
