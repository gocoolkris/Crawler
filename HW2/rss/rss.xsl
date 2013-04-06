<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/documentcollection">
<html>
<head><title>RSS aggregator</title></head>
<body style="font-family:Arial;font-size:12pt;background-color:#EEEEEE">
<xsl:for-each select="document/rss/channel">
<div>
 <div style="background-color:teal;color:white;padding:4px">
  <span style="font-weight:bold">
   <xsl:element name="a">
    <xsl:attribute name="href"> 
     <xsl:value-of select="link" />
    </xsl:attribute>
    <xsl:value-of select="title" />
   </xsl:element>
  </span>
 </div>
 <xsl:for-each select="item">
  <div>
   <xsl:if test="contains(title,'war')">
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:value-of select="link" />
      </xsl:attribute>
     <xsl:value-of select="title" />
   </xsl:element>
   <xsl:value-of select="description" />
   <br></br>
   </xsl:if>
   <xsl:if test="contains(description,'war')">
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:value-of select="link" />
      </xsl:attribute>
     <xsl:value-of select="title" />
   </xsl:element>
   <xsl:value-of select="description" />
   <br></br>
   </xsl:if>
    <xsl:if test="contains(title,'peace')">
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:value-of select="link" />
      </xsl:attribute>
     <xsl:value-of select="title" />
   </xsl:element>
   <xsl:value-of select="description" />
   <br></br>
   </xsl:if>
    <xsl:if test="contains(description,'peace')">
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:value-of select="link" />
      </xsl:attribute>
     <xsl:value-of select="title" />
   </xsl:element>
   <xsl:value-of select="description" />
   <br></br>
   </xsl:if>
  </div>
 </xsl:for-each>
</div>
</xsl:for-each>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
