Confirmed Billposting Order
Request ID: ${requestId}
Invoice Number: ${invoiceNumber}
Amount Due: ${amountDue?string("0.00")} EUR

User Information
Username: ${user.username!}
Name: ${user.name!} ${user.surname!}
Tax Code: ${user.taxCode!}
Email: ${user.email!}
Phone: ${user.phone!}
Address: ${user.address!}, ${user.zipCode!} ${user.city!}

Request Information
Poster Format: ${posterFormat!"Default"}
City Budgets:
<#list cityBudgets as budget>
  - ${budget.city}: ${budget.maxBudget} EUR
</#list>

Selected Zones
<#if selectedZones?has_content>
<#list selectedZones as zone>
  - [${zone.id}] ${zone.name} (${zone.city}) -> ${zone.price} EUR
</#list>
<#else>
  No zones selected.
</#if>
