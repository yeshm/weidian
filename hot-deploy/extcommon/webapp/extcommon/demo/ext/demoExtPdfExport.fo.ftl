<#escape x as x?xml>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               margin-right="1.5cm"
                               margin-left="0.2cm"
                               margin-bottom="2cm"
                               margin-top="1cm"
                               page-width="21cm"
                               page-height="29.7cm">
            <fo:region-body margin-top="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <fo:page-sequence master-reference="first">
        <fo:static-content flow-name="xsl-region-after">
            <fo:block line-height="14pt" font-size="10pt" text-align="end" font-family="Simsun">
                第
                <fo:page-number/>
                页
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">
            <fo:table border-style="solid" border-width="0.5pt" border-color="red" table-layout="auto" width="100%">
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="4.5cm"/>
                <fo:table-column column-width="1.5cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="2.5cm"/>

                <fo:table-body font-family="Simsun">
                    <fo:table-row>
                        <fo:table-cell border-style="solid" border-width="0.5pt">
                            <fo:block text-align="center">
                                partyId
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border-style="solid" border-width="0.5pt">
                            <fo:block text-align="center">
                                姓名
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>

                    <#list personList as person>
                        <fo:table-row>
                            <fo:table-cell border-style="solid" border-width="0.5pt">
                                <fo:block text-align="center">
                                ${person.partyId!}
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-style="solid" border-width="0.5pt">
                                <fo:block text-align="center">
                                ${person.firstName!}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </#list>
                </fo:table-body>
            </fo:table>
        </fo:flow>
    </fo:page-sequence>
</fo:root>

</#escape>
