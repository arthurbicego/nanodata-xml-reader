# NANODATA - ARTHUR BICEGO QUINTANEIRA

### XML Reader: Interface Web com Spring Boot, PostgreSQL, HTML, CSS, JavaScript.

Este projeto corresponde à etapa técnica do processo seletivo para Desenvolvedor Full Stack da Nanodata (Campinas, SP).

- [Descrição](#descrição)
- [Requisitos](#requisitos)
- [Como usar](#como-usar)

[Veja este projeto em ambiente de desenvolvimento do Github](https://github.dev/arthurbicego/nanodata-xml-reader/)

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

```java
(insert code here)
```

</details>
<details>
  <summary>Backend: Java com Spring Boot (Obrigatório)</summary>

```java
(insert code here)
```

</details>
<details>
  <summary>Frontend: Javascript, HTML, CSS (Frameworks a escolha do candidato)</summary>

```html
HTML: (insert code here)
```

```css
css: (insert code here);
```

```javascript
Javascript:
(insert code here)
```

</details>
<details>
  <summary>XMLs para desenvolvimento: utilizar os arquivos recebidos juntamente com este descritivo (Obrigatório)</summary>

```java
(insert code here)
```

</details>

---

### Como usar

1 – Certifique-se de ter o Docker instalado em sua máquina. Caso não tenha, siga as instruções de instalação no site oficial: [Docker Documentation](https://docs.docker.com/get-docker/). E então, abra o Docker.

2 - Faça o download dos arquivos deste repositório ou clone o projeto utilizando git para a sua máquina local :

```
git clone https://github.dev/arthurbicego/nanodata-xml-reader/
```

3 - Navegue até a pasta raiz do projeto e construa a imagem Docker utilizando o terminal com o seguinte comando:

```
docker-compose up --build
```

Pronto! O servidor estará em execução e pronto para ser acessado em 'http://localhost:8080'.
