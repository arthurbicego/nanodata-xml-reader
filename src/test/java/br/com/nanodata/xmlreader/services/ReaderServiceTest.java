package br.com.nanodata.xmlreader.services;

import br.com.nanodata.xmlreader.exceptions.NotFoundException;
import br.com.nanodata.xmlreader.models.dtos.FileDataDTO;
import br.com.nanodata.xmlreader.models.dtos.SaveDTO;
import br.com.nanodata.xmlreader.models.entities.FileContent;
import br.com.nanodata.xmlreader.models.entities.FileData;
import br.com.nanodata.xmlreader.models.mappers.FileDataMapper;
import br.com.nanodata.xmlreader.repositories.FileDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @InjectMocks
    private ReaderService readerService;
    @Mock
    private FileDataRepository fileDataRepository;
    @Mock
    private FileDataMapper fileDataMapper;

    @Test
    public void shouldGetAll() {

        List<FileData> fileDataList = generateExpectedFileData();
        List<FileDataDTO> expectedDTOList = convertToDTOList(fileDataList);
        when(fileDataRepository.findAll()).thenReturn(fileDataList);

        for (int i = 0; i < fileDataList.size(); i++) {
            when(fileDataMapper.toDTO(fileDataList.get(i))).thenReturn(expectedDTOList.get(i));
        }

        List<FileDataDTO> resultDTOList = readerService.getAll();

        assertThat(resultDTOList.size()).isEqualTo(expectedDTOList.size());
        for (int i = 0; i < resultDTOList.size(); i++) {
            assertThat(resultDTOList.get(i)).isEqualTo(expectedDTOList.get(i));
        }
    }

    @Test
    public void shouldSaveFiles() throws IOException {
        File file1 = new File("src/test/resources/xmls/file_teste_01.xml");
        File file2 = new File("src/test/resources/xmls/file_teste_02.xml");
        List<File> filesInFolder = List.of(file1, file2);
        List<MultipartFile> files = new ArrayList<>();
        for (File file : filesInFolder) {
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", fileInputStream);
            files.add(multipartFile);
        }
        SaveDTO saveDTO = readerService.processFiles(files);
        assertThat(saveDTO.getMessage()).isEqualTo("2 arquivo(s) salvo(s) com sucesso: [file_teste_01.xml, file_teste_02.xml]");
    }

    @Test
    public void shouldDownloadFilesSuccessfully() throws Exception {
        List<FileData> listFileData = generateExpectedFileData();

        when(fileDataRepository.findById(1L)).thenReturn(Optional.of(listFileData.get(0)));

        ResponseEntity<byte[]> response = readerService.downloadFileById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(listFileData.get(0).getFileContent().getContent());
        assertThat(response.getHeaders().getContentDisposition().getFilename()).isEqualTo(listFileData.get(0).getFileContent().getOriginalFileName());
    }

    @Test
    public void shouldDownloadFail() throws NotFoundException {
        when(fileDataRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> readerService.downloadFileById(1L)).isInstanceOf(NotFoundException.class).hasStackTraceContaining("Nenhum arquivo encontrado com o ID: 1");
    }

    private List<FileData> generateExpectedFileData() {
        List<FileData> listFileData = new ArrayList<>();

        FileContent fileContent1 = createFileContent("file_teste_01", "ContentBytes 1".getBytes());
        FileContent fileContent2 = createFileContent("file_teste_02", "ContentBytes 2".getBytes());
        FileContent fileContent3 = createFileContent("file_teste_03", "ContentBytes 3".getBytes());

        FileData fileData1 = createFileData("1", fileContent1);
        FileData fileData2 = createFileData("2", fileContent2);
        FileData fileData3 = createFileData("3", fileContent3);

        listFileData.add(fileData1);
        listFileData.add(fileData2);
        listFileData.add(fileData3);

        return listFileData;
    }

    private FileContent createFileContent(String originalFileName, byte[] content) {
        FileContent fileContent = new FileContent();
        fileContent.setContent(content);
        fileContent.setOriginalFileName(originalFileName);
        return fileContent;
    }

    private FileData createFileData(String fileId, FileContent fileContent) {
        FileData fileData = new FileData();
        fileData.setFileId(fileId);
        fileData.setIdedhEmi(LocalDateTime.of(2024, 02, 18, 12, 0));
        fileData.setIdenNF("IdenNF " + fileId);
        fileData.setIdecUF("IdecUF " + fileId);
        fileData.setEmitCNPJ("emitCNPJ " + fileId);
        fileData.setEmitxFant("emitxFant " + fileId);
        fileData.setDestCNPJ("destCNPJ " + fileId);
        fileData.setDestxNome("destxNome " + fileId);
        fileData.setIcmstotvTotTrib(Double.parseDouble(fileId + ".00"));
        fileData.setIcmstotvNF(Double.parseDouble(fileId + ".10"));
        fileData.setFileContent(fileContent);
        return fileData;
    }

    private List<FileDataDTO> convertToDTOList(List<FileData> fileDataList) {
        List<FileDataDTO> fileDataDTOList = new ArrayList<>();

        for (FileData fileData : fileDataList) {
            FileDataDTO fileDataDTO = new FileDataDTO();
            fileDataDTO.setId(fileData.getId());
            fileDataDTO.setFileId(fileData.getFileId());
            fileDataDTO.setIdedhEmi(fileData.getIdedhEmi());
            fileDataDTO.setIdenNF(fileData.getIdenNF());
            fileDataDTO.setIdecUF(fileData.getIdecUF());
            fileDataDTO.setEmitCNPJ(fileData.getEmitCNPJ());
            fileDataDTO.setEmitxFant(fileData.getEmitxFant());
            fileDataDTO.setDestCNPJ(fileData.getDestCNPJ());
            fileDataDTO.setDestxNome(fileData.getDestxNome());
            fileDataDTO.setIcmstotvTotTrib(fileData.getIcmstotvTotTrib());
            fileDataDTO.setIcmstotvNF(fileData.getIcmstotvNF());
            fileDataDTOList.add(fileDataDTO);
        }

        return fileDataDTOList;
    }
}