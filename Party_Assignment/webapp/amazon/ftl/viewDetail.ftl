<br><br>

<h1 id="viewHeading">Person Details </h1>

<div class="container" id="container-box">
  <div class="row">
    <div class="col-6">
      <h4 class="box-heading">Basic Details</h4>

      <#if mainPageDetailList?has_content>
        <#list mainPageDetailList as mainPageDetail>
          <div class="edit_btn"><button class="person_button" onclick="logRowValues(this)"><svg
                xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil"
                viewBox="0 0 16 16">
                <path
                  d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
              </svg></button><button class="person_delete_button" id="divPersonDelete" onclick="deletePerson()"><svg
                xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="red" class="bi bi-trash3-fill"
                viewBox="0 0 16 16">
                <path
                  d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z" />
              </svg></button></p>
          </div>
          <div class="eachRow">
            <p class="labels">First Name :</p>
            <p class="values" id="firstNameF">${mainPageDetail.firstName}</p>
            <p class="edit_para">
          </div>
          <div class="eachRow">
            <p class="labels">Middle Name :</p>
            <p class="values" id="middleNameF">${mainPageDetail.middleName!!}</p>
            <p class="edit_para">
          </div>

          <div class="eachRow">
            <p class="labels">Last Name :</p>
            <p class="values" id="lastNameF">${mainPageDetail.lastName!!}</p>
            <p class="edit_para">
          </div>
          <div class="eachRow">
            <p class="labels">Role Type:</p>
            <p class="values" id="roleTypeIdF">${mainPageDetail.roleTypeId}</p>
            <p class="edit_para">
          </div>
          <div class="eachRow">
            <p class="labels">Party Type:</p>
            <p class="values" id="partyTypeF">${mainPageDetail.partyTypeId}</p><input type="hidden" class="form-control"
              value="${mainPageDetail.partyId}" name="partyIdPerson" id="PersonPartyId">
            <p class="edit_para">
              <#if partyStatusList?has_content>
                <#list partyStatusList as partyStatus><input type="hidden" class="form-control"
                    value="${partyStatus.statusDate}" name="partyStatusDate" id="partyStatusDate"> <input type="hidden"
                    class="form-control" value="${partyStatus.statusId}" name="partyStatus" id="partyStatus"></#list>
              </#if>
          </div>
        </#list>
      </#if>
    </div>
    <div class="col-6">
      <h4 class="box-heading">Postal Address Details</h4>


      <div class="edit_btn">
        <#if joinPostalList ?has_content><button class="person_button" id="divPostalEdit"
            onclick="postalbutton(this)"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
              fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16">
              <path
                d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
            </svg></button></p>
          <#else> <br><br>
        </#if>
      </div>
      <div class="eachRow">
        <p class="labels">To Name :</p>
        <#if joinPostalList ?has_content>
          <#list joinPostalList as joinPostal>
            <p class="values" id="toNameF">${joinPostal.toName!!}
          </#list>
        </#if>
        </p>
        <p class="edit_para">
      </div>
      <div class="eachRow">
        <p class="labels">Address 1 :</p>
        <#if joinPostalList ?has_content>
          <#list joinPostalList as joinPostal>
            <p class="values" id="address1F">${joinPostal.address1}</p>
          </#list>
        </#if>
        <p class="edit_para">
      </div>

      <div class="eachRow">
        <p class="labels">House Number :</p>
        <#if joinPostalList ?has_content>
          <#list joinPostalList as joinPostal>
            <p class="values" id="houseNumberF">${joinPostal.houseNumber!!}</p>
          </#list>
        </#if>
        <p class="edit_para">
      </div>
      <div class="eachRow">
        <p class="labels">City:</p>
        <#if joinPostalList ?has_content>
          <#list joinPostalList as joinPostal>
            <p class="values" id="cityF">${joinPostal.city}</p>
          </#list>
        </#if>
        <p class="edit_para">
      </div>
      <div class="eachRow">
        <p class="labels">Postal Code:</p>
        <#if joinPostalList ?has_content>
          <#list joinPostalList as joinPostal>
            <p class="values" id="postalCodeF">${joinPostal.postalCode}</p>
            <p class="values" style="display:none;" id="contactMechIdF">${joinPostal.contactMechId}</p>
          </#list>
        </#if>
        <p class="edit_para">
      </div>

    </div>
  </div>
</div>



<br><br><br>



<div class="container" id="container-box">
  <div class="row">
    <div class="col-6">
      <h4 class="box-heading">TelePhone Details</h4>

      <div class="edit_btn">
        <#if teleNumber?has_content><button class="person_button" id="divTeleEdit" onclick="teleButton(this)"><svg
              xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil"
              viewBox="0 0 16 16">
              <path
                d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
            </svg></button>
          <#else> <br><br>
        </#if>
      </div>
      <div class="eachRow">
        <p class="labels">Country Code :</p>
        <p class="values" id="countryCodeF">
          <#if teleNumber?has_content>
            <#list teleNumber as teleNumberList>${teleNumberList.countryCode!!}</#list>
          </#if>
        </p>
      </div>

      <div class="eachRow">
        <p class="labels">Phone Number :</p>
        <p class="values" id="contactNumberF">
          <#if teleNumber?has_content>
            <#list teleNumber as teleNumberList>${teleNumberList.contactNumber!!}</#list>
          </#if>
        </p>
        <p style="display: none;" class="labels">Contact id :</p>
        <p style="display: none;" class="values" id="contactIdF">
          <#if teleNumber?has_content>
            <#list teleNumber as teleNumberList>${teleNumberList.contactMechId}</#list>
          </#if>
        </p>
      </div>
    </div>
    <div class="col-6">
      <h4 class="box-heading">Contact Type & Purpose</h4>
      <br>


      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="col" class="table-light">Contact Type</th>
            <th scope="col" class="table-light">Contact Purpose</th>
            <th scope="col" class="table-light">Action</th>
          </tr>
        </thead>
        <tbody id="tbodyPurpose">
          <#if jointPurpose ?has_content>
            <#list jointPurpose as jointPurposeList>
              <tr>
                <td id="contactMechTypeIdF">${jointPurposeList.contactMechTypeId}</td>
                <td>${jointPurposeList.contactMechPurposeTypeId}</td>
                <td style="display:none;">${jointPurposeList.contactMechId}</td>
                <td style="display:none;">${jointPurposeList.fromDate}</td>
                <td>
                  <button class="person_delete_button" id="divPurposeDelete" onclick="deletePurpose(this)"><svg
                      xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="red" class="bi bi-trash3-fill"
                      viewBox="0 0 16 16">
                      <path
                        d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z" />
                    </svg></button>
                </td>
              </tr>
            </#list>
          </#if>
        </tbody>
      </table>


    </div>
  </div>
</div>





<div class="container">
  <br><br><br>
  <h4 class="box-heading">Relationship</h4>
  <table class="table table-striped" id="relationship">
    <thead>
      <tr>
        <th scope="col" class="table-light">Role Type From</th>
        <th scope="col" class="table-light">Role Type To</th>
        <th scope="col" class="table-light">Relationship Type</th>
        <th scope="col" class="table-light">From Date</th>
        <th scope="col" class="table-light">Action</th>
      </tr>
    </thead>


    <tbody id="tbodyRel">
      <#if partyRelList ?has_content>
        <#list partyRelList as partyRel>
          <tr>
            <td>${partyRel.roleTypeIdFrom}</td>
            <td>${partyRel.roleTypeIdTo}</td>
            <td>${partyRel.partyRelationshipTypeId}</td>
            <td>${partyRel.fromDate}</td>
            <td style="display:none;">${partyRel.partyIdTo}</td>
            <td style="display:none;">${partyRel.partyIdFrom}</td>
            <td><button class="person_delete_button" onclick="relValues(this)"> <svg xmlns="http://www.w3.org/2000/svg"
                  width="16" height="16" fill="red" class="bi bi-trash3-fill" viewBox="0 0 16 16">
                  <path
                    d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z" />
                </svg></button></td>
          </tr>
        </#list>
    </tbody>
    </#if>
  </table>
</div>


<!-- Modal for basic details-->

<div class="modal fade" id="myModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Basic Details</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="updateForm" method="post">
          <#if mainPageDetailList?has_content>
            <#list mainPageDetailList as mainPageDetail>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">First Name</label>
                <input type="text" class="form-control" id="firstName" name="firstName"
                  value="${mainPageDetail.firstName}" placeholder="Enter First Name">
              </div>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">Middle Name</label>
                <input type="text" class="form-control" id="middleName" value="${mainPageDetail.middleName!!}"
                  name="middleName" placeholder="Enter Middle Name">
              </div>
              <div class="mb-3">
                <label for="partyTypeIdInput" class="form-label">Last Name</label>
                <input type="text" class="form-control" value="${mainPageDetail.lastName!!}" name="lastName"
                  id="lastName" placeholder="Enter Last Name">
                <input type="hidden" class="form-control" value="${mainPageDetail.partyId}" name="partyId" id="partyId"
                  placeholder="Enter Party ID">
              </div>

              <div class="mb-3">
                <input type="hidden" class="form-control" value="${mainPageDetail.partyTypeId}" name="partyType"
                  id="partyType" placeholder="Enter Party Type">
              </div>

              <div class="modal-footer">
                <button type="button" id="basicDetailsClose" class="btn btn-secondary"
                  data-bs-dismiss="modal">Close</button>
                <button type="button" id="saveButton" class="btn btn-primary">Save</button>
              </div>
            </#list>
          </#if>
        </form>
      </div>


    </div>
  </div>
</div>


<!-- Modal for postal details-->

<div class="modal fade" id="myModalForPostal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Postal Details</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="PostalForm" method="post">
          <#if joinPostalList ?has_content>
            <#list joinPostalList as joinPostal>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">To Name</label>
                <input type="text" class="form-control" id="toName" name="toName" value="${joinPostal.toName!!}"
                  placeholder="Enter To Name">
              </div>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">Address 1</label>
                <input type="text" class="form-control" id="address1" value="${joinPostal.address1}" name="address1"
                  placeholder="Enter Address">
              </div>
              <div class="mb-3">
                <label for="partyTypeIdInput" class="form-label">House Number</label>
                <input type="text" class="form-control" value="${joinPostal.houseNumber!!}" name="houseNumber"
                  id="houseNumber" placeholder="Enter House Number">
              </div>
              <div class="mb-3">
                <label for="partyTypeIdInput" class="form-label">City</label>
                <input type="text" class="form-control" value="${joinPostal.city}" name="city" id="city"
                  placeholder="Enter City">
                <input type="hidden" class="form-control" value="${joinPostal.contactMechId}" name="contactMechId"
                  id="contactMechId" placeholder="Enter Contact ID">
              </div>
              <div class="mb-3">
                <label for="partyTypeIdInput" class="form-label">Postal Code</label>
                <input type="text" class="form-control" value="${joinPostal.postalCode}" name="postalCode"
                  id="postalCode" placeholder="Enter Postal Code">
              </div>

              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" id="basicDetailsClosePostal"
                  data-bs-dismiss="modal">Close</button>
                <button type="button" id="savePostalButton" class="btn btn-primary">Save</button>
              </div>
            </#list>
          </#if>
        </form>
      </div>


    </div>
  </div>
</div>


<!-- Modal for TelePhone Details -->
<div class="modal fade" id="myModalForTele" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">TelePhone Details</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="TeleForm" method="post">
          <#if teleNumber?has_content>
            <#list teleNumber as teleNumberList>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">Country Name</label>
                <input type="text" class="form-control" id="countryCode" name="countryCode"
                  value="${teleNumberList.countryCode!!}" placeholder="Enter To Country Code">
              </div>
              <div class="mb-3">
                <label for="partyIdInput" class="form-label">Contact Number</label>
                <input type="text" class="form-control" id="contactNumber" value="${teleNumberList.contactNumber!!}"
                  name="contactNumber" placeholder="Enter Address">
                <input type="hidden" class="form-control" id="telecontactMechId" name="contactMechId"
                  value="${teleNumberList.contactMechId}">
              </div>
            </#list>
          </#if>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="teleCloseButton" data-bs-dismiss="modal">Close</button>
            <button type="button" id="teleButton" class="btn btn-primary">Save</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>


<script>



  $(document).ready(function () {
    // Test jQuery by hiding the test paragraph on page load
    $("#saveButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#updateForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          document.getElementById("firstNameF").innerHTML = document.getElementById("firstName").value;
          document.getElementById("middleNameF").innerHTML = document.getElementById("middleName").value;
          document.getElementById("lastNameF").innerHTML = document.getElementById("lastName").value;
          document.getElementById("partyTypeF").innerHTML = document.getElementById("partyType").value;
          $("#basicDetailsClose").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });
    });
    $("#savePostalButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#PostalForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_PostalAddress",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          document.getElementById("toNameF").innerHTML = document.getElementById("toName").value;
          document.getElementById("address1F").innerHTML = document.getElementById("address1").value;
          document.getElementById("houseNumberF").innerHTML = document.getElementById("houseNumber").value;
          document.getElementById("cityF").innerHTML = document.getElementById("city").value;
          document.getElementById("postalCodeF").innerHTML = document.getElementById("postalCode").value;
          $("#basicDetailsClosePostal").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });

    });
    $("#teleButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#TeleForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_TelePhone",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");

          document.getElementById("countryCodeF").innerHTML = document.getElementById("countryCode").value;
          document.getElementById("contactNumberF").innerHTML = document.getElementById("contactNumber").value;
          $("#teleCloseButton").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });


    });

    $("#purposeButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#purposeForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_Purpose",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          var firstColumnTd = updateFirstColumn();
          if (firstColumnTd) {

            firstColumnTd.textContent = document.getElementById("contactMechTypeId").value;
          }
          $("#purposeClose").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });


    });


  });



</script>