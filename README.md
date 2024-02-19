# NANODATA - ARTHUR BICEGO QUINTANEIRA

### XML Reader: Interface Web com Spring Boot, PostgreSQL, HTML, CSS, JavaScript.

Este projeto corresponde à etapa técnica do processo seletivo para Desenvolvedor Full Stack da Nanodata (Campinas, SP). Inteiramente desenvolvido por [Arthur Bicego Quintaneira](https://www.linkedin.com/in/arthurbicego/).

Neste repositório está todo o código necessário para rodar o projeto localmente, já com frontend, de acordo com as intruções [abaixo](#como-usar). Porém, toda a parte de frontend foi desenvolvida em Angular e distribuida para este projeto de Spring Boot. Para conferir o repositório específico da parte de frontend, [clique aqui](https://github.com/arthurbicego/xml-reader-frontend).

- [Descrição](#descrição)
- [Requisitos](#requisitos)
- [Como usar](#como-usar)

[Veja este projeto em ambiente de desenvolvimento do Github](https://github.dev/arthurbicego/xml-reader/)



---

### Descrição

O canditado deverá desenvolver uma aplicação onde:

```
1 – Construir uma interface web onde o usuário poderá fazer o Upload (individual ou em lote) de arquivos no formato “.xml”;
```

```
2 – Ao receber arquivos no Back a aplicação/sistema deverá ser capaz de extrair as tags relacionadas abaixo e gravar em uma tabela do banco de dados com o formato que o candidato julgar mais adequado para o tipo de informação (Texto, Data e hora, Número inteiro, Valor, etc):

  2.1 – Extrair as seguintes tags:
  /nfeProc/NFe/infNFe/Id
  /nfeProc/NFe/infNFe/ide/dhEmi
  /nfeProc/NFe/infNFe/ide/nNF
  /nfeProc/NFe/infNFe/ide/cUF
  /nfeProc/NFe/infNFe/emit/CNPJ
  /nfeProc/NFe/infNFe/emit/xFant
  /nfeProc/NFe/infNFe/dest/CNPJ
  /nfeProc/NFe/infNFe/dest/xNome
  /nfeProc/NFe/infNFe/total/ICMSTot/vTotTrib
  /nfeProc/NFe/infNFe/total/ICMSTot/vNF

  2.2 – Gravar todo o conteúdo do XML em um campo do tipo Binário no banco de dados (em outra tabela, relacionada com a tabela dos dados acima);
```

```
3 – Montar um tela no front onde serão listados os dados gravados no banco (exceto o Binário);
```

```
4 – Colocar um opção na tela de listagem onde o usuário poderá fazer o download individualmente dos arquivos XML que foram gravados no banco no formato Binário;
```

---

### Requisitos

<details>
  <summary>Banco de dados: PostgreSQL (Obrigatório)</summary>

```yaml
xml-reader_database:
  container_name: xml-reader_database
  image: postgres:latest
  restart: unless-stopped
  ports:
    - "5432:5432"
  environment:
    - POSTGRES_DB=******
    - POSTGRES_USER=******
    - POSTGRES_PASSWORD=******
```

</details>
<details>
  <summary>Backend: Java com Spring Boot (Obrigatório)</summary>

```java
@SpringBootApplication
public class XmlReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(XmlReaderApplication.class, args);
    }
}
```

</details>
<details>
  <summary>Frontend: Javascript, HTML, CSS (Frameworks a escolha do candidato)</summary>

Javascript/Typescript:

```javascript
removeFile(file: File) {
  this.selectedFiles = this.selectedFiles.filter(
    (selectedFile) => selectedFile !== file
  );
}
```

HTML:

```html
<div class="container select-container">
  <h2>Selecione os Arquivos .XML</h2>
  <input type="file" #fileInput style="display: none;" multiple (change)="onFileSelected($event)">
  <button mat-raised-button color="basic" class="select-button" (click)="fileInput.click()">Selecionar</button>
  <div *ngIf="selectedFiles.length > 0">
    <h2>Arquivos selecionados:</h2>
    <ul>
      <li *ngFor="let file of selectedFiles" class="file-item">
        <span class="file-name">{{ file.name }}</span>
        <div class="cancel-button">
          <button mat-mini-fab class="cancel-button-inner" (click)="removeFile(file)">
            <mat-icon class="">cancel</mat-icon>
          </button>
        </div>
      </li>
    </ul>
  </div>
  <button mat-raised-button color="primary" (click)="saveDocuments()"
    [disabled]="selectedFiles.length === 0">Salvar</button>
</div>
```

CSS:
```css
th {
  background-color: #f2f2f2;
  position: sticky;
  top: 75px;
  z-index: 99;
}
```
</details>
<details>
  <summary>XMLs para desenvolvimento: utilizar os arquivos recebidos juntamente com este descritivo (Obrigatório)</summary>

```java
fileData.setEmitCNPJ(getTagValue(document, "/nfeProc/NFe/infNFe/emit/CNPJ"));
fileData.setDestCNPJ(getTagValue(document, "/nfeProc/NFe/infNFe/dest/CNPJ"));
```

</details>

---

### Como usar

1 – Certifique-se de ter o Docker instalado em sua máquina. Caso não tenha, siga as instruções de instalação no site oficial: [Docker Documentation](https://docs.docker.com/get-docker/). E então, abra o Docker. É importante ressaltar que o Docker também é importante para rodar os testes, devido à maneira como foram desenvolvidos.

2 - Faça o download dos arquivos deste repositório ou clone o projeto utilizando git para a sua máquina local :

```
git clone https://github.dev/arthurbicego/xml-reader/
```

3 - Navegue até a pasta raiz do projeto e construa a imagem Docker utilizando o terminal com o seguinte comando:

```
docker-compose up --build
```

Pronto! O servidor estará em execução e pronto para ser acessado em 'http://localhost:8080'.
