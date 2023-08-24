
<br> <br>
<h3 style="    text-align: center;">Person details</h3>
<br>
<div class="container">
  <div id="divAddNewPerson">
    <button class="btn" id="addNewPerson" onclick="addNewPerson()"><svg xmlns="http://www.w3.org/2000/svg" width="16"
        height="16" fill="green" class="bi bi-person-plus-fill" viewBox="0 0 16 16">
        <path d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
        <path fill-rule="evenodd"
          d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5z" />
      </svg></button>
  </div>
  <table class="table table-striped">
    <thead>
      <tr>
        <th scope="col" class="table-dark">ID</th>
        <th scope="col" class="table-dark">Role Type</th>
        <th scope="col" class="table-dark">First Name</th>
        <th scope="col" class="table-dark">Relationship Type</th>
        <th scope="col" class="table-dark">Action</th>
        <th scope="col" class="table-dark">Delete</th>

      </tr>
    </thead>
    <tbody>
      <#if personList?has_content>
        <#list personList as person>
          <tr>
            <td>${person.partyId!!}</td>
            <td>${person.roleTypeId!!}</td>
            <td>${person.firstName}</td>
            <td>${person.partyRelationshipTypeId}</td>
            <td>
              <form action="/amazon/control/view_details" method="get">
                <input type="hidden" name="partyIdx" id="partyIdx" value="${person.partyId}">
                <input type="hidden" name="roleTypeId" id="roleTypeId" value="${person.roleTypeId!!}">
                <input type="hidden" name="firstName" id="firstName" value="${person.firstName}">
                <input type="hidden" name="middleName" id="middleName" value="${person.middleName!!}">
                <input type="hidden" name="lastName" id="lastName" value="${person.lastName!!}">
                <button type="submit">View Details</button>
              </form>
            </td>
            <td>
              <form id="deleteRelationship" onsubmit="deleteRelationship(event)" method="get">
                <input type="hidden" name="partyIdTo" id="partyIdTo" value="${person.partyId}">
                <input type="hidden" name="partyIdFrom" id="partyIdFrom" value="${person.partyIdFrom}">
                <input type="hidden" name="roleTypeIdFrom" id="roleTypeIdFrom" value="${person.roleTypeIdFrom}">
                <input type="hidden" name="roleTypeIdTo" id="roleTypeIdTo" value="${person.roleTypeIdTo}">
                <input type="hidden" name="fromDate" id="fromDate" value="${person.fromDate}">

                <button class="person_delete_button" id="divPersonDelete" type="submit"><svg
                                xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="red" class="bi bi-trash3-fill"
                                viewBox="0 0 16 16">
                                <path
                                  d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z" />
                              </svg></button>
              </form>
            </td>
          </tr>
        </#list>
      </#if>
    </tbody>
  </table>
</div>