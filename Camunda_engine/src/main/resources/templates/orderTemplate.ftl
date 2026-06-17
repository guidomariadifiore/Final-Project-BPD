--- Confirmed Billposting Order ---
Request ID: ${requestId}
Invoice Number: ${invoiceNumber}
Amount Due: ${amountDue?string("0.00")} €

===================================
USER INFORMATION
===================================
Username: ${user.username!}
Name: ${user.name!} ${user.surname!}
Tax Code: ${user.taxCode!}
Email: ${user.email!}
Phone: ${user.phone!}
Address: ${user.address!}, ${user.zipCode!} ${user.city!}

===================================
REQUEST INFORMATION
===================================
Poster Format: ${posterFormat!"Default"}
City Budgets:
<#list cityBudgets as budget>
  - ${budget.city}: ${budget.maxBudget} €
</#list>

===================================
SELECTED ZONES
===================================
<#if selectedZones?has_content>
<#list selectedZones as zone>
  - [${zone.id}] ${zone.name} (${zone.city}) -> ${zone.price} €
</#list>
<#else>
  No zones selected.
</#if>
-----------------------------------
