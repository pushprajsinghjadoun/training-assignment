  <div class="container">
  <br><br>
  <h1 style="text-align: center;">Upload a file to import data<h1>
  <br><br>
  <div class="mb-3">
    <input class="form-control" type="file" id="formFile">
    <form action="/PIES/control/readXmlData" method="post">
            <input type="hidden" name="fileName" id="fileNameValue" value="">
            <br>
            <button type="submit" style="margin-left: 45%;" class="btn btn-primary">Upload</button>

        </form>
  </div>

  </div>
