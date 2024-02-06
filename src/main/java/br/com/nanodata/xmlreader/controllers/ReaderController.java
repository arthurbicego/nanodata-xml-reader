package br.com.nanodata.xmlreader.controllers;

import br.com.nanodata.xmlreader.models.dtos.SaveRequestDTO;
import br.com.nanodata.xmlreader.services.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @PostMapping("/save")
    public SaveRequestDTO save(@RequestParam("files") List<MultipartFile> files) {
        return readerService.processFile(files);
    }
}
