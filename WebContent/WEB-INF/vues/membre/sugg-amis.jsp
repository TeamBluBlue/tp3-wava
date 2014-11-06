<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Des suggestions d'amis</h2>

<c:choose>

	<c:when test="${empty requestScope.modSuggAmis.lstSuggAmis}">
		<p>Aucune suggestion d'ami</p>
	</c:when>

	<c:otherwise>

		<ul id="lst-suggAmis" class="lst-membres">
			<%-- Parcours et affichage des amis suggérés --%>
			<c:forEach var="sa" items="${requestScope.modSuggAmis.lstSuggAmis}">
				<li>
					<%-- Affichage de la photo de l'ami suggéré (dans une division) --%>
					<fmt:formatNumber var="noFormate" value="${sa.noAmi}" pattern="000" />
					<div class="div-img">
						<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${sa.nomAmi}" />
					</div>
					<p class="nom-ami-sugg">${sa.nomAmi}</p>
					<p>
						${sa.nbAmisEnCommun}
						<c:choose>
							<c:when test="${sa.nbAmisEnCommun == 1}">ami</c:when>
							<c:otherwise>amis</c:otherwise>
						</c:choose>
						en commun
					</p>
					<p>
						<a href="${pageContext.request.contextPath}/membre/dem-ami?no-ami=${sa.noAmi}">Ajouter comme ami</a>
					</p>
				</li>
			</c:forEach>
		</ul>

		<p id="lien-sugg-autres-amis">
			<a href="${pageContext.request.contextPath}/membre/sugg-amis?indice-prem=${requestScope.modSuggAmis.indicePrem}&nb-amis-sugg=${requestScope.modSuggAmis.nbAmisSugg}">Suggérer d'autres amis</a>
		</p>

	</c:otherwise>

</c:choose>
