<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:pos="http://disim.univaq.it/services/postingservice">
   <soapenv:Header/>
   <soapenv:Body>
      <pos:availabilityRequest>
         <pos:applicant>
            <pos:name>${user.prop("name").stringValue()}</pos:name>
            <pos:surname>${user.prop("surname").stringValue()}</pos:surname>
            <pos:taxCode>${user.prop("taxCode").stringValue()}</pos:taxCode>
            <pos:address>${user.prop("address").stringValue()}</pos:address>
            <pos:city>${user.prop("city").stringValue()}</pos:city>
            <pos:zip>${user.prop("zipCode").numberValue()}</pos:zip>
            <pos:email>${user.prop("email").stringValue()}</pos:email>
         </pos:applicant>
         <pos:posting>
            <pos:posterFormat>${posterFormat}</pos:posterFormat>
            <#list selectedZones.elements() as zone>
            <pos:zone>
               <pos:id>${zone.prop("id").stringValue()}</pos:id>
               <pos:city>${zone.prop("city").stringValue()}</pos:city>
            </pos:zone>
            </#list>
         </pos:posting>
      </pos:availabilityRequest>
   </soapenv:Body>
</soapenv:Envelope>
