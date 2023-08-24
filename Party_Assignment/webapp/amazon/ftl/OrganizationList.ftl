<br>
<br>
<h3 style="    text-align: center;">Organization</h3>
<br>
<div class="container">
  <#if OrganizationList?has_content>
    <table class="table table-striped">
      <thead>
        <tr>
          <th scope="col" class="table-dark">ID</th>
          <th scope="col" class="table-dark">Organization Name</th>
          <th scope="col" class="table-dark">Annual Income</th>
          <th scope="col" class="table-dark">Action</th>
        </tr>
      </thead>
      <tbody>
        <#list OrganizationList as Organization>
          <tr>
            <td>${Organization.partyId}</td>
            <td id="tdGroupName">${Organization.groupName!!}</td>
            <td id="tdannualRevenue">${Organization.annualRevenue!!}</td>
            <td> <form action="/amazon/control/personDetails" method="GET">
                   <input type="hidden"  name="partyId" value="${Organization.partyId}">
                   <button type="submit" >View Details</button>
                 </form> </td>
          </tr>
        </#list>
      </tbody>
    </table>
  </#if>
</div>

