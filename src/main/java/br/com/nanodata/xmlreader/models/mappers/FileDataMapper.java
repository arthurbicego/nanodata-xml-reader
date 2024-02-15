package br.com.nanodata.xmlreader.models.mappers;

import br.com.nanodata.xmlreader.models.dtos.FileDataDTO;
import br.com.nanodata.xmlreader.models.entities.FileData;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
public class FileDataMapper {

    private final ModelMapper modelMapper;

    public FileDataMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.modelMapper = modelMapper;
    }

    public FileDataDTO toDTO(FileData fileData) {
        FileDataDTO fileDataDTO = modelMapper.map(fileData, FileDataDTO.class);
        fileDataDTO.setFileContentId(fileData.getFileContent().getId());
        return fileDataDTO;
    }

}
