package br.com.nanodata.xmlreader.repositories;

import br.com.nanodata.xmlreader.models.entities.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {
}
