<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.List"%>
<%@ page import="org.slf4j.LoggerFactory" %>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>

<body>

	<%
	    Logger log = Logger.getLogger("fr.rayquiz.findmyface.test");

	    log.fine("fine msg");
	    log.info("info msg");
	    log.warning("warn msg");
	    
	    org.slf4j.Logger slfLogger = LoggerFactory.getLogger("fr.rayquiz.findmyface.restest");
	    slfLogger.debug("slf4j debug msg");
	    slfLogger.info("slf4j  info msg");
	    slfLogger.error("slf4j error msg");
	%>
</body>
</html>
