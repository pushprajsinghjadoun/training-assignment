import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.util.EntityQuery

// Obtain the delegator
Delegator delegator = DelegatorFactory.getDelegator("default")
// Find organizations by the given condition
List<GenericValue> OrganizationRoleList = EntityQuery.use(delegator).from("PartyRole").where("roleTypeId", "ORGANIZATION_ROLE").queryList();



Map<String, String> OrganizationGroupName = [:]
Map<String, BigDecimal> OrganizationAnnualRevenue = [:]

OrganizationRoleList.each { organizationData ->
    List<GenericValue> OrganizationPartyGroup = EntityQuery.use(delegator).from("PartyGroup").where("partyId", organizationData.partyId).queryList();

    if (OrganizationPartyGroup) {
        GenericValue partyGroup = OrganizationPartyGroup.get(0)
        OrganizationGroupName[organizationData.partyId] = partyGroup.groupName
        OrganizationAnnualRevenue[organizationData.partyId] = partyGroup.get("annualRevenue")
    }
}

List<Map> OrganizationList = []

OrganizationRoleList.each { organizationRoleData ->
    OrganizationList.add([partyId: organizationRoleData.partyId, groupName: OrganizationGroupName[organizationRoleData.partyId], annualRevenue: OrganizationAnnualRevenue[organizationRoleData.partyId]])
}

// Now, the OrganizationList contains the required partyId, groupName, and annualRevenue for each organization.
context.OrganizationList = OrganizationList
