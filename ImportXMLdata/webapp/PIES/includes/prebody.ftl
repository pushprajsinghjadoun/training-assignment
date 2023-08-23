<html>
<head>
  <title>${layoutSettings.companyName}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
  <#if webSiteFaviconContent?has_content>
    <link rel="shortcut icon" href="">
  </#if>
  <#list layoutSettings.styleSheets as styleSheet>
    <link rel="stylesheet" href="${StringUtil.wrapString(styleSheet)}" type="text/css"/>
  </#list>
<#list layoutSettings.javaScripts as javaScript>
    <script type="text/javascript" src="${StringUtil.wrapString(javaScript)}"></script>
  </#list>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
</head>
<body data-offset="125">

    <nav class="navbar navbar-expand-lg" style="background-color: #aee8ff;">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <a class="nav-item nav-link" href="../main">Home</a>
          <a class="nav-item nav-link active" href="../PIESMAIN">PIES</a>
        </div>

    </nav>
  </div>
