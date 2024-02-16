package br.com.nanodata.xmlreader.services;

import br.com.nanodata.xmlreader.models.dtos.FileDataDTO;
import br.com.nanodata.xmlreader.models.dtos.SaveDTO;
import br.com.nanodata.xmlreader.models.entities.FileContent;
import br.com.nanodata.xmlreader.models.entities.FileData;
import br.com.nanodata.xmlreader.models.mappers.FileDataMapper;
import br.com.nanodata.xmlreader.repositories.FileDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReaderService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileDataMapper fileDataMapper;

    public SaveDTO processFiles(List<MultipartFile> files) {
        List<String> fileNameList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                this.processEachFile(file);
                fileNameList.add((file.getOriginalFilename()));
            } catch (Exception e) {
                log.error("Um erro ocorreu ao processar o seguinte arquivo: " + file.getName(), e);
            }
        }
        return new SaveDTO(fileNameList.size() + " arquivo(s) salvo(s) com sucesso: " + fileNameList);
    }

    private void processEachFile(MultipartFile file) throws IOException, ParserConfigurationException, SAXException,
            XPathExpressionException {
        FileContent fileContent = convertIntoFileContent(file);
        FileData fileData = convertIntoFileData(file, fileContent);
        fileDataRepository.save(fileData);
    }

    private FileContent convertIntoFileContent(MultipartFile file) throws IOException {
        FileContent fileContent = new FileContent();
        fileContent.setContent(file.getBytes());
        fileContent.setOriginalFileName(file.getOriginalFilename());
        return fileContent;
    }

    private FileData convertIntoFileData(MultipartFile file, FileContent fileContent) throws IOException, SAXException,
            ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        Document document = builder.parse(inputStream);

        FileData fileData = new FileData();

        fileData.setFileId(getTagValue(document, "/nfeProc/NFe/infNFe/@Id"));
        fileData.setIdedhEmi(OffsetDateTime.parse(getTagValue(document, "/nfeProc/NFe/infNFe/ide/dhEmi")));
        String ideCuf = getTagValue(document, "/nfeProc/NFe/infNFe/ide/cUF");
        fileData.setIdecUF(ideCuf);
        fileData.setIdenNF(getTagValue(document, "/nfeProc/NFe/infNFe/ide/nNF"));
        fileData.setEmitxFant(getTagValue(document, "/nfeProc/NFe/infNFe/emit/xFant"));
        fileData.setEmitCNPJ(getTagValue(document, "/nfeProc/NFe/infNFe/emit/CNPJ"));
        fileData.setDestCNPJ(getTagValue(document, "/nfeProc/NFe/infNFe/dest/CNPJ"));
        fileData.setDestxNome(getTagValue(document, "/nfeProc/NFe/infNFe/dest/xNome"));
        fileData.setICMSTotvTotTrib(getTagValue(document, "/nfeProc/NFe/infNFe/total/ICMSTot/vTotTrib"));
        fileData.setICMSTotvNF(getTagValue(document, "/nfeProc/NFe/infNFe/total/ICMSTot/vNF"));

        fileData.setFileContent(fileContent);

        return fileData;
    }

    private String getTagValue(Document document, String fileTag) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.compile(fileTag).evaluate(document, XPathConstants.STRING).toString();
    }

    public List<FileDataDTO> getAll() {
        List<FileData> listFileData = fileDataRepository.findAll();
        return listFileData.stream().map(fileDataMapper::toDTO).toList();
    }
}
