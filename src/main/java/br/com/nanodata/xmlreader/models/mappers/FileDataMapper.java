package br.com.nanodata.xmlreader.models.mappers;

import br.com.nanodata.xmlreader.models.dtos.FileDataDTO;
import br.com.nanodata.xmlreader.models.entities.FileData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDataMapper {

    @Autowired
    private ModelMapper modelMapper;

    public FileDataDTO toDTO(FileData fileData) {
        FileDataDTO fileDataDTO = modelMapper.map(fileData, FileDataDTO.class);
        fileDataDTO.setFileContentId(fileData.getFileContent().getId());
        return fileDataDTO;
    }

}
