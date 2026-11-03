<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Página de bienvenida: todo el tráfico entra por el Front Controller (/app).
     Se usa c:redirect (JSTL) en vez de un scriptlet. --%>
<c:redirect url="/app"/>
