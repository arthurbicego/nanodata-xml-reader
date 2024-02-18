package br.com.nanodata.xmlreader.controllers;

import br.com.nanodata.xmlreader.DatabaseConfiguration;
import br.com.nanodata.xmlreader.models.dtos.FileDataDTO;
import br.com.nanodata.xmlreader.models.entities.FileContent;
import br.com.nanodata.xmlreader.models.entities.FileData;
import br.com.nanodata.xmlreader.repositories.FileDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DatabaseConfiguration.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileDataRepository fileDataRepository;

    @BeforeEach
    void resetDataBase() {
        fileDataRepository.deleteAll();
    }

    @Test
    public void shouldGetAllFilesSuccessfully() throws Exception {
        List<FileData> listFileData = this.generateExpectedFileData();

        fileDataRepository.saveAll(listFileData);

        List<FileDataDTO> fileDataDTOList = convertToDTOList(listFileData);

        mockMvc.perform(MockMvcRequestBuilders.get("/reader/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(fileDataDTOList)));
    }

    @Test
    public void shouldSaveFilesSuccessfully() throws Exception {
        File file1 = new File("src/test/resources/xmls/file_teste_01.xml");
        File file2 = new File("src/test/resources/xmls/file_teste_02.xml");
        List<File> filesInFolder = List.of(file1, file2);
        List<MockMultipartFile> multipartFiles = new ArrayList<>();
        for (File file : filesInFolder) {
            FileInputStream fileInputStream = new FileInputStream(file);
            MockMultipartFile multipartFile = new MockMultipartFile("files", file.getName(), "text/plain", fileInputStream);
            multipartFiles.add(multipartFile);
        }
        mockMvc.perform(MockMvcRequestBuilders.multipart("/reader/save").file(multipartFiles.get(0)).file(multipartFiles.get(1))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("2 arquivo(s) salvo(s) com sucesso: [file_teste_01.xml, file_teste_02.xml]"));
        assertThat(fileDataRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void shouldSaveFilesExceptErrorFiles() throws Exception {
        File file1 = new File("src/test/resources/xmls/file_teste_01.xml");
        File file2 = new File("src/test/resources/xmls/file_teste_02.xml");
        File file3 = new File("src/test/resources/xmls/file_teste_error.xml");
        List<File> filesInFolder = List.of(file1, file2, file3);
        List<MockMultipartFile> multipartFiles = new ArrayList<>();
        for (File file : filesInFolder) {
            FileInputStream fileInputStream = new FileInputStream(file);
            MockMultipartFile multipartFile = new MockMultipartFile("files", file.getName(), "text/plain", fileInputStream);
            multipartFiles.add(multipartFile);
        }
        mockMvc.perform(MockMvcRequestBuilders.multipart("/reader/save").file(multipartFiles.get(0)).file(multipartFiles.get(1)).file(multipartFiles.get(2))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("2 arquivo(s) salvo(s) com sucesso: [file_teste_01.xml, file_teste_02.xml]"));
        assertThat(fileDataRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void shouldDownloadFilesSuccessfully() throws Exception {
        List<FileData> listFileData = this.generateExpectedFileData();

        fileDataRepository.saveAll(listFileData);

        mockMvc.perform(MockMvcRequestBuilders.get("/reader/download/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_teste_01\""))
                .andExpect(content().string("ContentBytes 1"));
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
