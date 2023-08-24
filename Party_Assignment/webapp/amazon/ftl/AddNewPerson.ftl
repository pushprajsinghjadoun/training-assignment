<div class="container">
  <div class="row">
    <div class="col-2"></div>
    <div class="col-8">
      <br><br>
      <h1 id="viewHeading">Add New Person Details</h1>
      <br><br>
      <form id="AddPersonForm" onsubmit="handleSubmit(event)" method="post">
        <div>
          <h3 class="AddPersonH3">Add Person and Role Details</h3>
        </div>
        <div class="mainClass">
          <div class="form-group">
            <select class="form-control form-margin-bottom" id="statusIdF" name="statusId" required>
              <option disabled selected>Select Status Id</option>
              <option>PARTY_ENABLED</option>
              <option>PARTY_DISABLED</option>

            </select>
            <input type="text" class="form-control form-margin-bottom" id="middleNameF" placeholder="Enter Middle Name" pattern="[A-Za-z]+" title="Please enter alphabets only">
            <select class="form-control form-margin-bottom" id="roleTypeIdF" name="statusId" required>
              <option disabled selected>Select Role Type Id</option>
              <#if RoleType?has_content>
                <#list RoleType as RoleTypeList>
                  <option>${RoleTypeList.roleTypeId}</option>
                </#list>
              </#if>
            </select>


          </div>
          <div class="form-group inputLeftMargin">

            <input type="text" class="form-control form-margin-bottom" id="firstNameF" placeholder="Enter First Name" pattern="[A-Za-z]+" title="Please enter alphabets only"
              required>
            <input type="text" class="form-control form-margin-bottom" id="lastNameF" placeholder="Enter Last Name" pattern="[A-Za-z]+" title="Please enter alphabets only"
              required>
            <input type="email" class="form-control form-margin-bottom" id="emailF" placeholder="Enter email" required>

          </div>
        </div>
        <div>
          <h3 class="AddPersonH3">Add Postal Address</h3>
        </div>
        <div class="mainClass">
          <div class="form-group">
            <input type="text" class="form-control form-margin-bottom" id="toNameF" placeholder="Enter To Name"
              required>
            <input type="text" class="form-control form-margin-bottom" id="cityF" placeholder="Enter City" required>
            <input type="text" class="form-control form-margin-bottom" id="postalCodeF" placeholder="Enter Postal Code"
              required pattern="[0-9]+" title="Please enter only numeric digits">

          </div>
          <div class="form-group inputLeftMargin">
            <input type="text" class="form-control form-margin-bottom" id="address1F" placeholder="Enter Address"
              required>
            <input type="text" class="form-control form-margin-bottom" id="houseNumberF"
              placeholder="Enter House Number" required pattern="[0-9]+" title="Please enter only numeric digits">
            <select class="form-control form-margin-bottom" id="contactMechPurposeTypeIdPostal" required>
              <option disabled selected>Select Contact Purpose</option>
              <#if ContactMechPurposeTypeList?has_content>
                <#list ContactMechPurposeTypeList as ContactMechPurposeType>
                  <option>${ContactMechPurposeType.contactMechPurposeTypeId}</option>
                </#list>
              </#if>
            </select>
          </div>
        </div>
        <div>
          <h3 class="AddPersonH3">Add Phone Number Details</h3>
        </div>
        <div class="mainClass">
          <div class="form-group">

            <input type="text" class="form-control form-margin-bottom" id="countryCodeF"
              placeholder="Enter Country Code" pattern="[0-9]+" title="Please enter only numeric digits" required>
            <select class="form-control form-margin-bottom" id="contactMechPurposeTypeIdPhone" required>
              <option disabled selected>Select Contact Purpose</option>
              <#if ContactMechPurposeTypeList?has_content>
                <#list ContactMechPurposeTypeList as ContactMechPurposeType>
                  <option>${ContactMechPurposeType.contactMechPurposeTypeId}</option>
                </#list>
              </#if>
            </select>


          </div>
          <div class="form-group inputLeftMargin">
            <input type="text" class="form-control form-margin-bottom" id="contactNumberF"
              placeholder="Enter Contact Number" pattern="[0-9]{10}" title="Please enter a 10-digit phone number"
              required>
          </div>
        </div>
        <div>
          <h3 class="AddPersonH3">Add Relationship with Organization</h3>
        </div>

        <select class="form-control form-margin-bottom" id="PartyRelationshipTypeF" name="PartyRelationshipType"
          required>
          <option disabled selected>Select Relationship</option>
          <#if PartyRelationshipTypeList?has_content>
            <#list PartyRelationshipTypeList as PartyRelationshipType>
              <option>${ PartyRelationshipType.partyRelationshipTypeId}</option>
            </#list>
          </#if>
        </select>
        <button type="submit" class="btn btn-primary">Submit</button>
      </form>
    </div>
    <div class="col-2"></div>
  </div>
</div>